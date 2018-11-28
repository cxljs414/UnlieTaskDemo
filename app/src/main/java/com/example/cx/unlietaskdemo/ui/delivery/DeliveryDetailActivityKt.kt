package com.xstore.tms.android.ui.delivery

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.support.design.widget.BottomSheetBehavior
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.UiSettings
import com.amap.api.maps2d.model.BitmapDescriptorFactory
import com.amap.api.maps2d.model.MarkerOptions
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.*
import com.jd.ace.utils.NetworkUtil
import com.leinyo.android.appbar.AppBar
import com.xstore.tms.android.R
import com.xstore.tms.android.base.BaseTitleActivityKt
import com.xstore.tms.android.contract.DeliveryDetailContract
import com.xstore.tms.android.core.Const
import com.xstore.tms.android.core.event.EventDispatchManager
import com.xstore.tms.android.core.location.LocationService
import com.xstore.tms.android.entity.LoginedCarrier
import com.xstore.tms.android.entity.delivery.*
import com.xstore.tms.android.presenter.DeliveryDetailPresenter
import com.xstore.tms.android.utils.*
import com.xstore.tms.android.utils.DateUtil.DATETIME_PATTERN
import com.xstore.tms.android.widget.popup.CommonPopupWindow
import kotlinx.android.synthetic.main.activity_delivery_detail.*
import org.apache.commons.lang3.StringUtils
import org.json.JSONObject
import java.math.BigDecimal
import java.util.*


/**
 * Created by hly on 2018/3/15.
 * email hly910206@gmail.com
 */
class DeliveryDetailActivityKt : BaseTitleActivityKt<DeliveryDetailPresenter>(),
        DeliveryDetailContract.IView, RouteSearch.OnRouteSearchListener, View.OnClickListener {

    private var isRegistService = false
    private var aMap: AMap? = null
    private lateinit var targetPoint: LatLonPoint
    private lateinit var storePoint: LatLonPoint
    private lateinit var mUiSettings: UiSettings
    private lateinit var mDetailEntity: DeliveryDetailEntity
    private val ROUTE_TYPE_RIDE = 4
    private var mRouteSearch: RouteSearch? = null
    private var mRideRouteResult: RideRouteResult? = null
    private lateinit var mBinder: LocationService.MyBinder
    private var mPopCommonPopupWindow: CommonPopupWindow? = null
    private lateinit var mRightView: ImageView
    private var mDialog: Dialog? = null
    private var txt_DialogText: TextView? = null
    private var distance: BigDecimal = BigDecimal(-1000)
    private var unlineFailureType:String?=null

    companion object {
        private const val KEY_ORDERID: String = "KEY_ORDERID"
        //用companion object包裹方法，实现java中static的效果
        fun startDeliveryDetailActivity(context: Context, orderId: String) {
            val intent = Intent(context, DeliveryDetailActivityKt::class.java)
            intent.putExtra(KEY_ORDERID, orderId)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_delivery_detail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.map.onCreate(savedInstanceState)
        initDialogView()
    }

    override fun initData() {
        super.initData()
        if (aMap == null) {
            aMap = this.map.map
            mUiSettings = aMap!!.uiSettings
            mUiSettings.isZoomGesturesEnabled = true
        }

        if (intent != null) {
            checkNetAndLoadData()
        }

        this.detail_nonet_retry.onClick {
            if(NetworkUtil.isConnected()){
                this.detail_loadinglayout.visibility = View.VISIBLE
                this.detail_nonetlayout.visibility = View.GONE
                mPresenter?.getDeliveryOrderDetail(intent.getStringExtra(KEY_ORDERID))
            }
        }

        val behavior = BottomSheetBehavior.from(scrollView)
        behavior.isHideable = false
        behavior.peekHeight = DensityUtil.dip2px(this,150f)
    }

    private fun checkNetAndLoadData(){
        if (NetworkUtil.isConnected()) {
            this.detail_loadinglayout.visibility = View.VISIBLE
            mPresenter?.getDeliveryOrderDetail(intent.getStringExtra(KEY_ORDERID))
        } else {
            this.detail_loadinglayout.visibility = View.GONE
            this.detail_nonetlayout.visibility = View.VISIBLE
        }
    }

    @SuppressLint("InflateParams")
    override fun updateView(buildDetailResult: DeliveryDetailEntity) {
        mDetailEntity = buildDetailResult
        this.tv_confirm.visibility = View.VISIBLE
        this.detail_loadinglayout.visibility = View.GONE
        initMap()
        val view: View = LayoutInflater.from(this).inflate(R.layout.view_pop_window, null)
        (view.findViewById(R.id.tv_services) as View).setOnClickListener(this)
        (view.findViewById(R.id.tv_reject_confirm) as View).setOnClickListener(this)
        (view.findViewById(R.id.tv_error) as View).setOnClickListener(this)
        mPopCommonPopupWindow = CommonPopupWindow.Builder(this)
                .setView(view)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewOnclickListener { _, _ -> }
                .setOutsideTouchable(true)
                .create()
        startService(Intent(this, LocationService::class.java))
        bindService(Intent(this, LocationService::class.java), connection, Context.BIND_AUTO_CREATE)

        this.tv_order_id.text = getString(R.string.item_delivery_list_order_id, mDetailEntity.doNo)
        this.tv_consignee.text = getString(R.string.item_delivery_list_consignee, mDetailEntity.consignee)
        this.tv_mobile_phone.text = getString(R.string.item_delivery_list_mobilePhone, mDetailEntity.mobilePhone)
        this.tv_address.text = getString(R.string.item_delivery_list_address, mDetailEntity.address
                ?: "")
        this.tv_order_create_time.text = getString(R.string.detail_create_order_time, DateUtil.dateToString(mDetailEntity.orderCreateDate, DATETIME_PATTERN))
        val isTopSpeed= mDetailEntity.transportPriority == 99
        this.detail_jsd.visibility = if(isTopSpeed){View.VISIBLE}else{View.GONE}
        if (mDetailEntity.leaveTime == null) {
            this.order_date.visibility = View.GONE
        } else {
            this.order_date.setData(mDetailEntity.leaveTime)
            this.order_date.visibility = View.VISIBLE
        }
        var stockout = false
        mDetailEntity.infoList?.forEach {
            val textView = TextView(this)
            if (StringUtils.isNotBlank(it.name) && StringUtils.isNotBlank(it.shippedQty?.toString())) {
                val text = getString(R.string.detail_order_list, it.name, it.shippedQty)
                if (it.oosFlag == 1) {
                    stockout = true
                    setRichText(textView, "【缺货】$text", 0, 4)
                } else {
                    textView.text = text
                }
            }
            textView.textSize = 16F
            this.ll_order_list.addView(textView)
        }
        val countText = getString(R.string.detail_count, mDetailEntity.waybillNumber?.toString()
                ?: "")
        if (stockout) {
            setRichText(this.tv_count, "$countText　　缺货", countText.length + 2, countText.length + 4)
        } else {
            this.tv_count.text = countText
        }
        this.tv_confirm.setOnClickListener(this)
        this.iv_close.setOnClickListener(this)
        //this.tv_refuse.setOnClickListener(this)
        this.iv_phone_button.setOnClickListener(this)
        this.order_id_layout.onClick {
            copyDono()
        }
    }

    private fun copyDono() {
        val content = tv_order_id.text.toString()
        if (content.isNotEmpty()){
            this.copy(content)
            ToastUtils.showToast("运单号已复制到剪贴板")
        }
    }

    override fun updateError() {
        this.detail_nonetlayout.visibility = View.VISIBLE
        this.detail_loadinglayout.visibility = View.GONE
    }

    @Suppress("DEPRECATION")
    private fun initMap(){
        targetPoint = LatLonPoint(mDetailEntity.target[0], mDetailEntity.target[1])
        storePoint = LatLonPoint(mDetailEntity.store[0], mDetailEntity.store[1])
        mRouteSearch = RouteSearch(this)
        mRouteSearch!!.setRouteSearchListener(this)
        setfromandtoMarker()
        searchRouteResult(ROUTE_TYPE_RIDE, RouteSearch.RidingDefault)
    }

    private fun setfromandtoMarker() {
        aMap!!.addMarker(MarkerOptions()
                .position(AMapUtil.convertToLatLng(storePoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_start)))
        aMap!!.addMarker(MarkerOptions()
                .position(AMapUtil.convertToLatLng(targetPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_end)))
    }

    /**
     * 开始搜索路径规划方案
     */
    @Suppress("DEPRECATION")
    private fun searchRouteResult(routeType: Int, mode: Int) {
        val fromAndTo = RouteSearch.FromAndTo(storePoint, targetPoint)
        if (routeType == ROUTE_TYPE_RIDE) {// 骑行路径规划
            val query = RouteSearch.RideRouteQuery(fromAndTo, mode)
            mRouteSearch!!.calculateRideRouteAsyn(query)// 异步路径规划骑行模式查询
        }
    }

    override fun getTitleViewConfig(): AppBar.TitleConfig {
        val titleConfig: AppBar.TitleConfig =
                buildDefaultConfig(R.string.title_delivery_detail)
        mRightView = ImageView(this)
        mRightView.setImageResource(R.drawable.ic_more)
        mRightView.setOnClickListener {
            mPopCommonPopupWindow?.showAsDropDown(mRightView, 0, -60)
        }
        titleConfig.setRightView(mRightView)
        return titleConfig
    }

    private fun setRichText(tv: TextView, text: String, start: Int, end: Int) {
        val sp = SpannableStringBuilder(text)
        sp.setSpan(ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv.text = sp
    }

    override fun orderConfirm(result: Boolean) {
        if (result) {
            SoundPoolUtil.getInstance().playJinbi()
            EventDispatchManager.getInstance().dispatchEvent(
                    EventDispatchManager.Event(
                            EventDispatchManager.EventType.EVENT_DELIVERY_STATUS_CHANGED,
                            mDetailEntity.doNo.toString()))
            if (DeviceUtil.isLocServiceEnable(this)) {
                mBinder.startLocation(storePoint, mDetailEntity.doNo)
                finish()
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.alert_title)
                builder.setMessage(R.string.location_hint)

                builder.setPositiveButton(R.string.alert_confirm) { _, _ ->
                    val intent = Intent()
                    intent.action = android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
                    startActivityForResult(intent, 1)
                }
                builder.setNegativeButton(R.string.alert_cancel) { _, _ ->
                    mBinder.startLocation(storePoint, mDetailEntity.doNo)
                    finish()
                }
                builder.setCancelable(false)
                val dialog: AlertDialog = builder.show()
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mBinder.startLocation(storePoint, mDetailEntity.doNo)
        finish()
    }

    override fun orderReject(result: Boolean) {
        if (result) {
            finish()
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_close -> this.rl_tip.visibility = View.GONE
            R.id.iv_phone_button -> diallPhone()
            R.id.tv_error -> {
                if(unlineFailureType.isNullOrBlank()){
                    if (mDetailEntity.doNo.toString() == "" ||
                            mDetailEntity.storeId.toString() == "" ||
                            mDetailEntity.orderId.toString() == "") {
                        ToastUtils.showToast("数据异常")
                        mPopCommonPopupWindow!!.dismiss()
                        return
                    }

                    mPresenter?.orderExceptionDelivery(RequestDeliveryException(mDetailEntity.doNo.toString(), mDetailEntity.storeId, mDetailEntity.orderId))
                }
            }
            R.id.tv_reject_confirm -> {
                if(unlineFailureType.isNullOrBlank() ||Const.UNLINETASK_TYPE_REJECT==unlineFailureType) {
                    DeliveryRefuseActivity.startDeliveryRefuseActivity(this, mDetailEntity)
                    mPopCommonPopupWindow!!.dismiss()
                }
            }
            R.id.tv_services -> {
                val intent = Intent(Intent.ACTION_DIAL)
                val data = Uri.parse("tel:4006068768")
                intent.data = data
                startActivity(intent)
            }
            R.id.tv_confirm -> {
                if(unlineFailureType.isNullOrBlank() ||Const.UNLINETASK_TYPE_COMPLETE==unlineFailureType) {
                    if(mDetailEntity.receivedTime!= null && System.currentTimeMillis() < mDetailEntity.receivedTime){
                        ToastUtils.showToast(R.string.delivery_complete_time_exception)
                    }else{
                        showAlert()
                    }
                }
            }
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.alert_title)
        builder.setMessage(R.string.alert_delivery_send)
        builder.setPositiveButton(R.string.alert_confirm
        ) { _, _ ->
            checkPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE))
            //校验是否离线妥投失败任务
            if(unlineFailureType.isNullOrBlank() ||Const.UNLINETASK_TYPE_COMPLETE==unlineFailureType){
                //校验时间
                if(Const.UNLINETASK_TYPE_COMPLETE==unlineFailureType){
                    //有离线失败任务
                    mPresenter?.orderConfirmUnlineTask()
                }else{
                    //正常妥投
                    mPresenter?.orderConfirm(RequestDeliveryComplete(mDetailEntity.collectionId, mDetailEntity.doNo.toString(), distance),storePoint)
                }
            }

            try{
                val json= JSONObject()
                json.put("pin",LoginedCarrier.carrierPin)
                json.put("time",DateUtil.getNowDate(DateUtil.DATETIME_PATTERN))
                json.put("orderId",mDetailEntity.doNo)
                json.put("curLeaveTime",order_date.curLeaveTime())
                //埋点
                JDMaUtils.sendCommonData(this,
                        "xstore_tms_1540273172816|1",
                        json.toString(),
                        "orderConfirm",
                        this,
                        "${mDetailEntity.doNo}",
                        "",
                        "DeliveryDetailActivityKt")
            }catch (e:Exception){
                LogUtils.i(TAG,"埋点错误")
            }
        }

        builder.setNegativeButton(R.string.cancel
        ) { _, _ -> }


        builder.setCancelable(false)
        val dialog: AlertDialog = builder.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
    }

    private fun diallPhone() {
        if (mPresenter != null) {
            this.phone_progressbar.visibility = View.VISIBLE
            mPresenter!!.decryptMobileNumber(mDetailEntity.doNo.toString())
        }
    }

    /**
     * 返回电话号码
     * @param phoneNo
     */
    override fun backMobileNumber(phoneNo: String) {
        this.phone_progressbar.visibility = View.GONE
        val intent = Intent(Intent.ACTION_DIAL)
        val data = Uri.parse("tel:$phoneNo")
        intent.data = data
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        this.map.onDestroy()
        if(isRegistService){
            unbindService(connection)
        }
    }

    override fun onResume() {
        super.onResume()
        this.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        this.map.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        this.map.onSaveInstanceState(outState)
    }

    @SuppressLint("InflateParams")
    fun initDialogView() {
        mDialog = Dialog(this, R.style.BottomDialog)
        mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.view_prompt_dialog, null)
        txt_DialogText = dialogView.findViewById(R.id.txt_DialogText)

        mDialog!!.setCanceledOnTouchOutside(true)
        val submitBtn = dialogView.findViewById<Button>(R.id.bt_submit)
        submitBtn.setOnClickListener {
            mDialog!!.dismiss()
        }
        mDialog!!.setContentView(dialogView)
        mDialog!!.setCancelable(false)
    }

    private fun showDialog(msg: String) {
        txt_DialogText!!.text = msg
        mDialog!!.show()
        val lp = WindowManager.LayoutParams()
        val window = mDialog!!.window
        lp.copyFrom(window.attributes)
        val scrrenWidth = DensityUtil.getScreenWidthpx(this)
        val dialogWidth = scrrenWidth * 0.8f + 0.5
        lp.width = dialogWidth.toInt()
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = lp
    }

    override fun hideLoading() {
        super.hideLoading()
        this.phone_progressbar.visibility = View.GONE
    }

    override fun showUnlineFailureTip(type: String?) {
        unlinetask_fail_tip.visibility = View.VISIBLE
        unlineFailureType = type
        when(type){
            Const.UNLINETASK_TYPE_COMPLETE->unlinetask_fail_tip.text = getString(R.string.unline_failure_complete)
            Const.UNLINETASK_TYPE_REJECT->unlinetask_fail_tip.text = getString(R.string.unline_failure_reject)
        }
    }

    override fun startUnlineTask(delayEntity: DelayOrderEntity) {
        mBinder.startLocationUnlineTask(JSON.toJSONString(delayEntity),delayEntity.doNo!!.toLong(),storePoint)
    }

    override fun onDriveRouteSearched(p0: DriveRouteResult?, p1: Int) {
    }

    override fun onBusRouteSearched(p0: BusRouteResult?, p1: Int) {
    }

    override fun onWalkRouteSearched(p0: WalkRouteResult?, p1: Int) {
    }

    override fun onRideRouteSearched(result: RideRouteResult?, errorCode: Int) {
        aMap!!.clear()// 清理地图上的所有覆盖物
        var reason= ""
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if(result != null) {
                if (result.paths != null) {
                    if (result.paths.size > 0) {
                        mRideRouteResult = result
                        val ridePath = mRideRouteResult!!.paths[0]
                        val rideRouteOverlay = RideRouteOverlay(
                                this, aMap, ridePath,
                                mRideRouteResult!!.startPos,
                                mRideRouteResult!!.targetPos)
                        rideRouteOverlay.removeFromMap()
                        rideRouteOverlay.addToMap()
                        rideRouteOverlay.zoomToSpan()
                        distance = BigDecimal(result.paths[0].distance.toDouble()).divide(BigDecimal(1000)).setScale(2, BigDecimal.ROUND_HALF_DOWN)
                        mDetailEntity.customerDistance = distance
                        if (result.paths[0].distance > 1000) {
                            this.tv_distance.text = getString(R.string.detail_distance_km, result.paths[0].distance / 1000)
                        } else {
                            this.tv_distance.text = getString(R.string.detail_distance, result.paths[0].distance)
                        }
                    }else{
                        reason = "骑行路径规划失败,result.paths.size <=0"
                    }
                } else {
                    reason = "骑行路径规划失败,result.paths = null"
                }
            }else{
                reason = "骑行路径规划失败,result=null"
            }
        }else{
            reason = "骑行路径规划失败，errorcode=$errorCode"
        }
        if(reason.isNotEmpty()){
            LogUtils.i(TAG,reason)
            MonitorUtil.addMonitor(Const.FLAG_MONITOR_ORDER_ROUTE,"${mDetailEntity.doNo}",reason)
        }
    }

    override fun orderExceptionDelivery(entity: ResponseExceptionDelivery) {
        val isSucess = entity.success!!.toBoolean()
        if (isSucess) {
            entity.doNo = mDetailEntity.doNo
            entity.storeId = mDetailEntity.storeId
            entity.orderId = mDetailEntity.orderId
            entity.storePoint = storePoint
            entity.customerDistance = mDetailEntity.customerDistance
            DeliveryUnusualActivity.startDeliveryUnusualActivity(this, entity)
        } else {
            val msg = entity.errorMsg
            showDialog(msg)
        }
        mPopCommonPopupWindow!!.dismiss()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val getY = ev.y.toInt()
        mUiSettings.isZoomGesturesEnabled = true
        if (getY + this.scrollView.scrollY < this.map.measuredHeight + this.mToolbar.measuredHeight) {
            this.scrollView.requestDisallowInterceptTouchEvent(true)
        } else {
            this.scrollView.requestDisallowInterceptTouchEvent(false)
        }
        return super.dispatchTouchEvent(ev)
    }


    private val connection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName) {
            isRegistService = false
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mBinder = service as LocationService.MyBinder
            isRegistService = true
        }
    }

}