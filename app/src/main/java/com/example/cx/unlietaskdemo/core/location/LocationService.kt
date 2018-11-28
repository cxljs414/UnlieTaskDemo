package com.xstore.tms.android.core.location

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import android.os.Vibrator
import android.util.Log
import com.alibaba.fastjson.JSON
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.*
import com.leinyo.httpclient.retrofit.NetworkBeanResponse
import com.xstore.tms.android.R
import com.xstore.tms.android.core.Const
import com.xstore.tms.android.core.event.EventDispatchManager
import com.xstore.tms.android.entity.LoginedCarrier
import com.xstore.tms.android.entity.PushLocationEntity
import com.xstore.tms.android.entity.delivery.DelayOrderEntity
import com.xstore.tms.android.entity.delivery.DeliveryDetailEntity
import com.xstore.tms.android.entity.delivery.RequestLocation
import com.xstore.tms.android.entity.delivery.ResponseLocationResult
import com.xstore.tms.android.repository.DeliverOrderRepository
import com.xstore.tms.android.repository.PushLocationRepository
import com.xstore.tms.android.service.AlarmService
import com.xstore.tms.android.utils.JDMaUtils
import com.xstore.tms.android.utils.LogUtils
import com.xstore.tms.android.utils.MonitorUtil
import org.json.JSONObject
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import kotlin.collections.HashMap


/**
 * Created by hly on 2018/6/28.
 * email hly910206@gmail.com
 */
class LocationService : Service(), AMapLocationListener, RouteSearch.OnRouteSearchListener, NetworkBeanResponse<ResponseLocationResult>{
    private val TAG:String= "LocationService"
    //声明AMapLocationClient类对象
    private var mLocationClient: AMapLocationClient? = null

    private var mLocationOption: AMapLocationClientOption? = null

    private val mBinder: MyBinder = MyBinder()
    private val mQueue: LinkedBlockingQueue<Long> = LinkedBlockingQueue()
    private lateinit var mStorePoint: LatLonPoint
    private var mRouteSearch: RouteSearch? = null
    private val repository: DeliverOrderRepository = DeliverOrderRepository()
    private val pushRepository: PushLocationRepository = PushLocationRepository()
    private var mOrderId: Long? = null
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var pushEntity: PushLocationEntity? = null
    private var pushEntityMap:MutableMap<Long,PushLocationEntity>? = hashMapOf()
    private var vibrator:Vibrator?= null
    private var pm:PowerManager?= null
    private var wakeLock:PowerManager.WakeLock?=null
    private var locationType= 0 //0 正常妥投上传定位 1Gis定位 2离线妥投定位
    private var unlineTaskMap:MutableMap<Long,DelayOrderEntity> = HashMap()
    private var delayOrderEntity:DelayOrderEntity?=null
    private lateinit var mUnlineStorePoint: LatLonPoint
    override fun onCreate() {
        super.onCreate()
        LogUtils.i(TAG,"onCreate")
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        mLocationClient = AMapLocationClient(applicationContext)
        //启动后台定位，第一个参数为通知栏ID，建议整个APP使用一个
        mLocationClient!!.enableBackgroundLocation(2001, buildNotification())

        mLocationClient!!.setLocationListener(this)
        mLocationOption = AMapLocationClientOption()
        //签到模式
        mLocationOption!!.locationPurpose = AMapLocationClientOption.AMapLocationPurpose.SignIn
        //高精度定位
        mLocationOption!!.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption!!.isOnceLocation = true
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption!!.httpTimeOut = 8000
        //关闭缓存机制
        mLocationOption!!.isMockEnable = false
        mLocationOption!!.isLocationCacheEnable = false
        mLocationClient!!.setLocationOption(mLocationOption)
        mLocationClient!!.stopLocation()

        mRouteSearch = RouteSearch(this)
        mRouteSearch!!.setRouteSearchListener(this)
        Executors.newFixedThreadPool(1).execute {
            while (true) {
                try {
                    val orderId: Long = mQueue.take()
                    mOrderId = orderId
                    locationType = 0
                    if(mOrderId?.compareTo(0L) == -1){
                        pushEntity = pushEntityMap?.get(mOrderId!!)
                        locationType = 1
                    }
                    if(unlineTaskMap.containsKey(orderId)){
                        locationType = 2
                        delayOrderEntity = unlineTaskMap[orderId]
                    }
                    mLocationClient!!.startLocation()
                } catch (e: Exception) {
                    e.printStackTrace()
                    LogUtils.i(TAG,"mqueue take 出错")
                }
            }
        }
        pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        //保持cpu一直运行，不管屏幕是否黑屏
        wakeLock = pm?.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, AlarmService::class.java.canonicalName);
        wakeLock?.acquire(1000*60*30)
    }

    override fun onLocationChanged(amapLocation: AMapLocation?) {
        if (amapLocation != null) {
            if (amapLocation.errorCode == 0) {
                //可在其中解析amapLocation获取相应内容。

                latitude = amapLocation.latitude
                longitude = amapLocation.longitude
                when(locationType){
                    1->{
                        mStorePoint= LatLonPoint(
                                pushEntity?.storeLatitude!!.toDouble(),
                                pushEntity?.storeLongitude!!.toDouble()
                        )
                    }
                    2->{
                        mStorePoint = mUnlineStorePoint
                        delayOrderEntity?.locationStatus = 1
                        delayOrderEntity?.latitude = "$latitude"
                        delayOrderEntity?.longitude = "$longitude"
                    }
                }
                val fromAndTo = RouteSearch.FromAndTo(
                        mStorePoint, LatLonPoint(latitude!!, longitude!!))

                val query = RouteSearch.RideRouteQuery(fromAndTo, RouteSearch.RidingDefault)
                mRouteSearch!!.calculateRideRouteAsyn(query)// 异步路径规划骑行模式查询
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.errorCode + ", errInfo:"
                        + amapLocation.errorInfo)
                try {
                    if(locationType == 1) {
                        LogUtils.i(TAG, "定位失败 errorcode=${amapLocation.errorCode}")
                        //MonitorUtil.addMonitor(Const.FLAG_MONITEOR_LOCATION, "定位失败 errorcode=${amapLocation.errorCode}", 1L)
                    }
                } catch (e: Exception) {
                    throw e
                }

                when(locationType){
                    0->{
                        val request = RequestLocation(0f, 0f, 2, mOrderId.toString(), "-1000")
                        request.status = -2
                        repository.postLocation(LocationService::javaClass.name, request, this)
                        sendLocationJdma("妥投","0","0",mOrderId.toString(),"-1000","定位失败")
                    }
                    1->{
                        pushEntity.let {
                            pushEntity?.lat = BigDecimal(0)
                            pushEntity?.lng = BigDecimal(0)
                            pushEntity?.carrierDistance = BigDecimal(-1000)
                            pushRepository.postPushLocation(
                                    LocationService::javaClass.name,
                                    pushEntity!!,
                                    this
                            )
                        }
                        LogUtils.i(TAG,"distance=-1000 定位失败")
                        sendLocationJdma("GIS","0","0","","-1000","定位失败")
                    }
                    2->{
                        delayOrderEntity.let {
                            delayOrderEntity?.locationStatus = -2
                            delayOrderEntity?.latitude = "0"
                            delayOrderEntity?.longitude = "0"
                            delayOrderEntity?.realDistance = BigDecimal(-1000)
                            repository.orderUnlineTask(true,
                                    Const.UNLINETASK_TYPE_COMPLETE,"",
                                    delayOrderEntity,null)
                        }
                    }
                }
            }
        }else{
            try {
                if(locationType == 1) {
                    LogUtils.i(TAG, "定位失败 amapLocation=null")
                    //MonitorUtil.addMonitor(Const.FLAG_MONITEOR_LOCATION, "定位失败 amapLocation=null", 1L)
                }
            } catch (e: Exception) {
                throw e
            }

            when(locationType){
                0->{
                    val request = RequestLocation(0f, 0f, 2, mOrderId.toString(), "-1000")
                    request.status = -2
                    repository.postLocation(LocationService::javaClass.name, request, this)
                    sendLocationJdma("妥投","0","0",mOrderId.toString(),"-1000","定位失败")
                }
                1->{
                    pushEntity.let {
                        pushEntity?.lat = BigDecimal(0)
                        pushEntity?.lng = BigDecimal(0)
                        pushEntity?.carrierDistance = BigDecimal(-1000)
                        pushRepository.postPushLocation(
                                LocationService::javaClass.name,
                                pushEntity!!,
                                this
                        )
                    }
                    LogUtils.i(TAG,"distance=-1000 定位失败")
                    sendLocationJdma("GIS","0","0","","-1000","定位失败")
                }
                2->{
                    delayOrderEntity.let {
                        delayOrderEntity?.locationStatus = -2
                        delayOrderEntity?.latitude = "0"
                        delayOrderEntity?.longitude = "0"
                        delayOrderEntity?.realDistance = BigDecimal(-1000)
                        repository.orderUnlineTask(true,
                                Const.UNLINETASK_TYPE_COMPLETE,"",
                                delayOrderEntity,null)
                    }
                }
            }
        }
    }

    override fun onRideRouteSearched(result: RideRouteResult?, errorCode: Int) {
        if (result != null && result.paths != null && result.paths.size > 0) {
            val distance: Float = result.paths[0].distance
            when(locationType){
                0->{
                    repository.postLocation(
                            LocationService::javaClass.name,
                            RequestLocation(latitude!!.toFloat(),
                                    longitude!!.toFloat(),
                                    2,
                                    mOrderId.toString(),
                                    distance.toString()),
                            this)
                    sendLocationJdma("妥投","$latitude","$longitude",mOrderId.toString(),"$distance","成功")
                    LogUtils.i(TAG,"妥投 distance=${distance.toString()}")
                }
                1->{
                    pushEntity.let {
                        pushEntity?.lat = BigDecimal(latitude!!)
                        pushEntity?.lng = BigDecimal(longitude!!)
                        pushEntity?.carrierDistance = BigDecimal(distance.toDouble())
                        pushRepository.postPushLocation(
                                LocationService::javaClass.name,
                                pushEntity!!,
                                this
                        )
                    }
                    sendLocationJdma("GIS","$latitude","$longitude","","$distance","成功")
                    LogUtils.i(TAG,"Gis distance=${distance.toString()}")
                }
                2->{
                    delayOrderEntity.let {
                        delayOrderEntity?.realDistance = BigDecimal(distance.toDouble())
                        repository.orderUnlineTask(true,
                                Const.UNLINETASK_TYPE_COMPLETE,"",
                                delayOrderEntity,null)
                    }

                }
            }

        } else {
            try {
                if(locationType == 1) {
                    var reaseon = "result= null"
                    if (result != null) {
                        if (result?.paths == null) {
                            reaseon = "result.paths = null"
                        } else if (result.paths.size <= 0) {
                            reaseon = "result.paths.size <=0"
                        }
                    }
                    LogUtils.i(TAG, "计算路线失败 reason=$reaseon")
                    //MonitorUtil.addMonitor(Const.FLAG_MONITEOR_LOCATION, "计算路线失败 $reaseon ", pushEntity?.version!!)
                }
            } catch (e: Exception) {
                throw e
            }
            when(locationType){
                0->{
                    val request = RequestLocation(latitude!!.toFloat(), longitude!!.toFloat(), 2, mOrderId.toString(),"-1000")
                    request.status = -2
                    repository.postLocation(LocationService::javaClass.name, request, this)
                    sendLocationJdma("妥投","$latitude","$longitude",mOrderId.toString(),"-1000","路线计算失败")
                }
                1->{
                    pushEntity.let {
                        pushEntity?.lat = BigDecimal(latitude!!)
                        pushEntity?.lng = BigDecimal(longitude!!)
                        pushEntity?.carrierDistance = BigDecimal(-1000)
                        pushRepository.postPushLocation(
                                LocationService::javaClass.name,
                                pushEntity!!,
                                this@LocationService
                        )
                    }
                    sendLocationJdma("GIS","$latitude","$longitude","","-1000","路线计算失败")
                    LogUtils.i(TAG,"distance=-1000 路线计算失败")
                }
                2->{
                    delayOrderEntity.let {
                        delayOrderEntity?.realDistance = BigDecimal(-1000)
                        repository.orderUnlineTask(true,
                                Const.UNLINETASK_TYPE_COMPLETE,"",
                                delayOrderEntity,null)
                    }
                }
            }
        }
    }

    override fun onDataError(requestCode: Int, responseCode: Int, message: String?) {
        LogUtils.i(TAG,"定位上传失败:$responseCode-$message")
        //vibrator?.vibrate(3000)
        if(mOrderId?.compareTo(0L) == -1){
            pushEntityMap?.remove(mOrderId!!)
        }
        mLocationClient!!.stopLocation()

        //配送员定位失败添加埋点
        if(locationType == 1){
            //埋点
            try {
                LogUtils.i(TAG,"定位上传失败:$responseCode-$message  添加埋点")
                MonitorUtil.addMonitor(Const.FLAG_MONITEOR_LOCATION, "接口调用失败 content=${JSON.toJSONString(pushEntity)}",pushEntity?.version!!)
            }catch (e: java.lang.Exception){
            }
        }
    }

    override fun onDataReady(response: ResponseLocationResult?, requestCode: Int) {
        //vibrator?.vibrate(300)
        LogUtils.i(TAG,"定位上传成功:$requestCode")
        if(mOrderId?.compareTo(0L) == -1){
            pushEntityMap?.remove(mOrderId!!)
        }
        mLocationClient!!.stopLocation()
    }

    override fun onDestroy() {
        super.onDestroy()
        //关闭后台定位，参数为true时会移除通知栏，为false时不会移除通知栏，但是可以手动移除
        mLocationClient!!.stopLocation()
        mLocationClient!!.disableBackgroundLocation(true)
        mLocationClient!!.onDestroy()
        LogUtils.i(TAG,"onDestroy")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        if(mQueue.size!= 0)return false
        return super.onUnbind(intent)
    }

    override fun onDriveRouteSearched(p0: DriveRouteResult?, p1: Int) {
    }

    override fun onBusRouteSearched(p0: BusRouteResult?, p1: Int) {
    }

    override fun onWalkRouteSearched(p0: WalkRouteResult?, p1: Int) {
    }

    fun sendLocationJdma(type:String,latitude:String,longitude:String,orderId: String,distance:String,result:String){
        try {
            var json = JSONObject()
            json.put("pin", LoginedCarrier.carrierPin)
            json.put("type", type)
            json.put("latitude", latitude)
            json.put("longitude", longitude)
            json.put("orderId", orderId)
            json.put("distance", distance)
            json.put("result", result)
            JDMaUtils.sendCustomData(this,
                    "xstore_tms_1540273172816|4",
                    "LocationService",
                    json.toString(),
                    "发送定位")
        }catch (e:java.lang.Exception){

        }
    }

    internal inner class MyBinder : Binder() {
        //正常妥投后上传定位
        fun startLocation(storePoint: LatLonPoint, orderId: Long) {
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mStorePoint = storePoint
            mQueue.put(orderId)
        }

        //上传GIS定位
        fun startLocation(content:String){
            try {
                val orderId:Long = 0- Math.abs(Random().nextLong())
                val pushEntity = JSON.parseObject(content,PushLocationEntity::class.java)
                pushEntityMap?.put(orderId,pushEntity)
                mQueue.put(orderId)
            }catch (e:Exception){
                try {
                    LogUtils.i(TAG,"push event解析出错  添加埋点")
                    MonitorUtil.addMonitor(Const.FLAG_MONITEOR_LOCATION, "push event解析出错 content=$content",1L)
                }catch (e: java.lang.Exception){
                }
            }
        }

        /**
         * 正常妥投失败，启用离线妥投，需要获取实时定位
         */
        fun startLocationUnlineTask(json:String,orderId: Long,storePoint: LatLonPoint){
            mUnlineStorePoint = storePoint
            val delayOrderEntity = JSON.parseObject(json, DelayOrderEntity::class.java)
            unlineTaskMap.put(orderId,delayOrderEntity)
        }
    }

    private fun buildNotification(): Notification? {
        var builder: Notification.Builder? = null
        var notificationManager: NotificationManager? = null
        var notification: Notification? = null

        if (android.os.Build.VERSION.SDK_INT >= 26) {
            //Android O上对Notification进行了修改，如果设置的targetSDKVersion>=26建议使用此种方式创建通知栏
            if (null == notificationManager) {
                notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            val channelId = packageName
            val notificationChannel = NotificationChannel(channelId,
                    "backgroundLocation", NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.enableLights(true)//是否在桌面icon右上角展示小圆点
            notificationChannel.lightColor = Color.BLUE //小圆点颜色
            notificationChannel.setSound(null, null)
            notificationChannel.setShowBadge(true) //是否在久按桌面图标时显示此渠道的通知
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(applicationContext, channelId)
        } else {
            builder = Notification.Builder(applicationContext)
        }
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("请勿关闭")
                .setContentText("正在后台运行")
                .setWhen(System.currentTimeMillis())

        notification = builder.build()

        return notification
    }
}

