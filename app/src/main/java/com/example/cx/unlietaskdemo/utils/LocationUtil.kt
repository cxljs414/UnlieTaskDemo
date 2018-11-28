package com.xstore.tms.android.utils

import android.annotation.SuppressLint
import android.content.Context
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.*
import com.xstore.tms.android.AppApplication
import com.xstore.tms.android.inerfaces.OnRideRouteSearched

@SuppressLint("StaticFieldLeak")
object LocationxUtil: AMapLocationListener, RouteSearch.OnRouteSearchListener {
    private val TAG= "LocationxUtil"
    private var mLocationClient: AMapLocationClient? = null
    private var mLocationOption: AMapLocationClientOption? = null
    private var pair:Pair<Double,Double> = Pair(0.0,0.0)
    private var mRouteSearch: RouteSearch? = null
    private var mContext:Context = AppApplication.instance!!.applicationContext
    private var startPoint:LatLonPoint?= null
    private var curDistance:Float = -1000f
    init {
        mLocationClient = AMapLocationClient(mContext)
        mLocationClient!!.setLocationListener(this)
        mLocationOption = AMapLocationClientOption()
        //签到模式
        mLocationOption!!.locationPurpose = AMapLocationClientOption.AMapLocationPurpose.SignIn
        //高精度定位
        mLocationOption!!.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption!!.isOnceLocation = false
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption!!.httpTimeOut = 30*1000
        //关闭缓存机制
        mLocationOption!!.isMockEnable = false
        mLocationOption!!.isLocationCacheEnable = false
        mLocationClient!!.setLocationOption(mLocationOption)

    }

    fun startLocation(){
        mLocationClient!!.startLocation()
        mRouteSearch = RouteSearch(mContext)
        mRouteSearch!!.setRouteSearchListener(this)
    }

    override fun onLocationChanged(p0: AMapLocation?) {
        if(p0!=null){
            pair = Pair(p0.latitude,p0.longitude)
            LogUtils.i(TAG,"${pair?.first} : ${pair?.second}")
            LogUtils.i(TAG,"curDistance=$curDistance")
            if(startPoint!=null){
                serachRideRoute(startPoint!!,LatLonPoint(p0.latitude,p0.longitude))
            }
        }
    }

    fun getCurLocation():Pair<Double,Double>{
        return pair
    }

    fun getCurDistance():Float{
        return curDistance
    }

    fun setStartPoint(startPoint:LatLonPoint){
        this.startPoint = startPoint
    }

    fun serachRideRoute(startPoint: LatLonPoint,endPoint:LatLonPoint){
        val fromAndTo = RouteSearch.FromAndTo(startPoint, endPoint)
        val query = RouteSearch.RideRouteQuery(fromAndTo, RouteSearch.RidingDefault)
        mRouteSearch!!.calculateRideRouteAsyn(query)// 异步路径规划骑行模式查询
    }

    override fun onDriveRouteSearched(p0: DriveRouteResult?, p1: Int) {
    }

    override fun onBusRouteSearched(p0: BusRouteResult?, p1: Int) {
    }

    override fun onRideRouteSearched(result: RideRouteResult?, p1: Int) {
        if(result != null){
            curDistance = result.paths[0].distance
            LogUtils.i(TAG,"searched curDistance=$curDistance")
        }

    }

    override fun onWalkRouteSearched(p0: WalkRouteResult?, p1: Int) {
    }

    fun isStartPointExist(): Boolean {
        return startPoint != null
    }
}