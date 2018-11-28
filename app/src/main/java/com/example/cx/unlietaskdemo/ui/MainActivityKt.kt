package com.xstore.tms.android.ui

import android.Manifest
import android.app.AlertDialog
import android.content.*
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.IBinder
import android.text.TextUtils
import android.view.View
import com.alibaba.fastjson.JSON
import com.amap.api.services.core.LatLonPoint
import com.jd.ace.utils.NetworkUtil
import com.jd.push.lib.MixPushManager
import com.jingdong.jdpush_new.JDPushManager
import com.xstore.tms.android.R
import com.xstore.tms.android.base.BaseActivityKt
import com.xstore.tms.android.contract.MainContract
import com.xstore.tms.android.core.Const
import com.xstore.tms.android.core.ScreenReceiver
import com.xstore.tms.android.core.event.EventDispatchManager
import com.xstore.tms.android.core.location.LocationService
import com.xstore.tms.android.entity.LoginedCarrier
import com.xstore.tms.android.entity.PushLocationEntity
import com.xstore.tms.android.entity.main.ResponseDeliveryCount
import com.xstore.tms.android.inerfaces.OnUnlineTaskStateChange
import com.xstore.tms.android.presenter.MainPresenter
import com.xstore.tms.android.receiver.AlarmMsgReceiver
import com.xstore.tms.android.receiver.MyPushReceiver
import com.xstore.tms.android.receiver.NetWorkStateReceiver
import com.xstore.tms.android.receiver.UnlineTaskReceiver
import com.xstore.tms.android.service.AlarmService
import com.xstore.tms.android.ui.collection.CollectionOrderActivity
import com.xstore.tms.android.ui.delivery.DeliveryListActivityKt
import com.xstore.tms.android.ui.homedelivery.HomeDeliveryActivity
import com.xstore.tms.android.ui.performance.PerformanceActivity
import com.xstore.tms.android.ui.ro.ReturnOrderActivity
import com.xstore.tms.android.utils.*
import com.xstore.tms.taskunline.TaskDBManager
import kotlinx.android.synthetic.main.activity_main.*
import org.apache.commons.lang3.StringUtils
import java.util.*
import java.util.regex.Pattern


/**
 * Created by hly on 2018/3/15.
 * email hly910206@gmail.com
 */
class MainActivityKt : BaseActivityKt<MainPresenter>(), MainContract.IView,
        View.OnClickListener, OnUnlineTaskStateChange {

    //静态变量定义
    companion object Constant {
        const val RESULT_CODE_SCAN = 0X222
    }

    //常量定义
    private val REQUEST_CODE_SCAN = 0X111

    private var mIsExit = false
    private var locatBinder: LocationService.MyBinder?=null
    private var isNetWorkEnableRefresh= false  //是否是网络断开重连后进行的刷新
    private var receiver= NetWorkStateReceiver()
    private var alarmReceiver = AlarmMsgReceiver()
    private var screenReceiver = ScreenReceiver()
    private var unlineTaskReceiver = UnlineTaskReceiver(this);

    override fun initWindows() {
    }

    override fun initData() {
        super.initData()
        this.ll_delivery.setOnClickListener(this)
        this.ll_performance.setOnClickListener(this)
        this.ll_pick_up.setOnClickListener(this)
        this.ll_scan.setOnClickListener(this)
        this.ll_home_delivery.setOnClickListener(this)
        this.logout.setOnClickListener(this)
        this.tv_version.text = "V${DeviceUtil.getAppVersionName(this)}"
        bindService(Intent(this, AlarmService::class.java),alarmConnection,Context.BIND_AUTO_CREATE)
        bindService(Intent(this,LocationService::class.java),locationConnection,Context.BIND_AUTO_CREATE)
        MixPushManager.register(this,MyPushReceiver::class.java)
        JDPushManager.getDeviceToken(this) {
            LogUtils.i("Mix","deviceToken=$it")
        }
        registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        registerReceiver(alarmReceiver, IntentFilter("com.xstore.tms.android.clarmmsg"))
        var intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_SCREEN_ON )
        intentFilter.addAction(Intent.ACTION_USER_PRESENT)
        registerReceiver(screenReceiver,intentFilter)
        var filter= IntentFilter()
        filter.addAction("xstore_unlinetask_success")
        filter.addAction("xstore_unlinetask_failure")
        registerReceiver(unlineTaskReceiver,filter)
    }

    override fun onStart() {
        super.onStart()
        checkPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION))
    }

    override fun onResume() {
        super.onResume()
        val token= LoginedCarrier.token
        if(token.isNullOrBlank() || token?.length!! !=32){
            LogUtils.i("token", "main needrelogin : token=$token")
            LoginedCarrier.token = ""
            LoginActivityKt.startActivity()
            return
        }
        checkNet()
    }

    private fun checkNet(){
        if(NetworkUtil.isConnected()) {
            mPresenter!!.loadDeliveryCount()
        }else{
            root.showNoNet { checkNet() }
        }
    }

    override fun showNoNet() {
        root.showNoNet { checkNet() }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ll_performance -> startActivity(Intent(this, PerformanceActivity::class.java))
            R.id.ll_delivery -> DeliveryListActivityKt.startDeliveryListActivity(this)
            R.id.ll_pick_up -> startActivity(Intent(this, ReturnOrderActivity::class.java))
            R.id.ll_scan -> startActivityForResult(Intent(this, ScanActivity::class.java), REQUEST_CODE_SCAN)
            R.id.ll_home_delivery -> startActivity(Intent(this, HomeDeliveryActivity::class.java))
            R.id.logout -> showLogoutDialog()
        }
    }

    private fun showLogoutDialog() {
        val dialog: AlertDialog = AlertDialog.Builder(this)
                .setTitle(R.string.alert_title)
                .setMessage(R.string.logout)
                .setPositiveButton(R.string.alert_confirm) { _, _ ->
                    MixPushManager.unBindClientId(this,LoginedCarrier.carrierPin)
                    PreferencesUtils.safePut("l_c_token","")
                    LoginedCarrier.token = ""
                    TimeOutUtil.getInstance().clearAll()
                    LoginActivityKt.startActivity()
                }
                .setNegativeButton(R.string.cancel,null)
                .setCancelable(true)
                .show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_SCAN -> {
                if (resultCode == RESULT_CODE_SCAN) {
                    val scanResult: String? = data?.getStringExtra("result")
                    val regEx = "7FRESH\\-*\\-*"
                    val pattern = Pattern.compile(regEx)
                    val matcher = pattern.matcher(scanResult)
                    val rs = matcher.find()
                    if (rs) {
                        if (!TextUtils.isEmpty(scanResult)) {
                            CollectionOrderActivity.startActivityForResult(this, scanResult!!, CollectionOrderActivity.REQUEST_CODE)
                            //DeliveryListActivityKt.startDeliveryListActivity(this, scanResult!!)
                        }
                    } else {
                        ToastUtils.showToast("无效的二维码！")
                    }
                }
            }
            CollectionOrderActivity.REQUEST_CODE -> {
                val receive = data?.getBooleanExtra("receive", false)
                if (receive == null || !receive) {
                    return
                }
                val collectionId = data?.getStringExtra("result")
                if (StringUtils.isNotBlank(collectionId)) {
                    DeliveryListActivityKt.startDeliveryListActivity(this, collectionId)
                }
            }
        }

    }

    override fun onDeliveryReady(deliveryCount: ResponseDeliveryCount) {
        LogUtils.i(TAG,"刷新数据")
        this.tv_today_delivery_number.text = deliveryCount.doCompletedNum.toString()
        this.tv_shop_address.text = LoginedCarrier.storeName
        if (deliveryCount.doReceivedNum!! > 0) {
            this.tv_delivery_order_tip.text = deliveryCount.doReceivedNum.toString()
            this.tv_delivery_order_tip.visibility = View.VISIBLE
        } else {
            this.tv_delivery_order_tip.text = "0"
            this.tv_delivery_order_tip.visibility = View.GONE
        }
        if (deliveryCount.roNum!! > 0) {
            //
            if(isNetWorkEnableRefresh && this.tv_return_order_tip.text.toString().isNotBlank()){
                var preReceiveCount= this.tv_return_order_tip.text.toString().toInt()
                if(deliveryCount.roNum!! > preReceiveCount){
                    TimeOutUtil.getInstance().addReceiveCount()
                }
            }
            this.tv_return_order_tip.text = deliveryCount.roNum.toString()
            this.tv_return_order_tip.visibility = View.VISIBLE
        } else {
            this.tv_return_order_tip.text = "0"
            this.tv_return_order_tip.visibility = View.GONE
        }
        if (deliveryCount.homeNum!! > 0) {
            if(isNetWorkEnableRefresh && this.tv_home_delivery_tip.text.toString().isNotBlank()){
                var preHomeCount= this.tv_home_delivery_tip.text.toString().toInt()
                if(deliveryCount.homeNum!! > preHomeCount){
                    TimeOutUtil.getInstance().addHomeCount()
                }
            }
            this.tv_home_delivery_tip.text = deliveryCount.homeNum.toString()
            this.tv_home_delivery_tip.visibility = View.VISIBLE
        } else {
            this.tv_home_delivery_tip.text = "0"
            this.tv_home_delivery_tip.visibility = View.GONE
        }
        this.tv_welcome_name.text = LoginedCarrier.carrierName
        this.tv_shop_user_pin.text = LoginedCarrier.carrierPin

        isNetWorkEnableRefresh = false
    }

    override fun onBackPressed() {
        val tExit: Timer
        if (!mIsExit) {
            mIsExit = true // 准备退出
            ToastUtils.showToast(getString(R.string.app_back_hint))
            tExit = Timer()
            tExit.schedule(object : TimerTask() {
                override fun run() {
                    mIsExit = false // 取消退出
                }
            }, 2000) // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            ActivityManager.exit()
        }
    }

    override fun onEventMain(event: EventDispatchManager.Event) {
        super.onEventMain(event)
        when (event.eventType) {
            EventDispatchManager.EventType.EVENT_HINT_HOME_DELIVERY,
            EventDispatchManager.EventType.EVENT_HINT_RECEVING-> mPresenter!!.loadDeliveryCount()
            EventDispatchManager.EventType.EVENT_HINT_PUSHLOCATION ->{
                if(LocationxUtil.isStartPointExist()) {
                    if (event.data != null && event.data.toString().isNotBlank()) {
                        if (locatBinder == null) {
                            locationConnection.setContent(event.data.toString())
                            bindService(Intent(this, LocationService::class.java), locationConnection, Context.BIND_AUTO_CREATE)
                        } else {
                            locatBinder?.startLocation(event.data.toString())
                        }
                    }
                }else{
                    try {
                        val pushEntity = JSON.parseObject(event.data.toString(), PushLocationEntity::class.java)
                        LocationxUtil.setStartPoint(LatLonPoint(pushEntity.storeLatitude!!.toDouble(),pushEntity.storeLongitude!!.toDouble()))
                    }catch (e:Exception){
                        LogUtils.i(TAG,"push event解析出错  添加埋点")
                    }
                }
            }
            EventDispatchManager.EventType.EVENT_HINT_NETWORK_ENABLE ->{
                isNetWorkEnableRefresh = true
                mPresenter!!.loadDeliveryCount()
            }
        }
    }

    override fun showLoading() {
        loadwheel.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadwheel.visibility = View.GONE
    }

    override fun onUnlineTaskSuccess(id: String, type: String?) {
    }

    override fun onUnlineTaskFailure(id: String, type: String?) {
        type.let {
            if(type== Const.UNLINETASK_TYPE_MONITOR){
                TaskDBManager.getIntance().deleteTask(id)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SoundPoolUtil.getInstance().release()
        TimeOutUtil.getInstance().exit()
        unbindService(locationConnection)
        unbindService(alarmConnection)
        unregisterReceiver(receiver)
        unregisterReceiver(alarmReceiver)
        unregisterReceiver(screenReceiver)
        unregisterReceiver(unlineTaskReceiver)
    }

    private val locationConnection = object : ServiceConnection {

        private var content:String?= null
        fun setContent(content:String){
            this.content = content
        }
        override fun onServiceDisconnected(name: ComponentName) {
            locatBinder = null
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            locatBinder = service as LocationService.MyBinder
            if(content != null){
                locatBinder?.startLocation(content!!)
            }
        }
    }

    private val alarmConnection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName) {
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
        }
    }
}