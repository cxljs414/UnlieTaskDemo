package com.xstore.tms.android.presenter

import android.annotation.SuppressLint
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.RideRouteResult
import com.jd.ace.utils.NetworkUtil
import com.leinyo.httpclient.retrofit.NetworkStringResponse
import com.xstore.tms.android.R
import com.xstore.tms.android.base.BasePresenter
import com.xstore.tms.android.contract.DeliveryDetailContract
import com.xstore.tms.android.core.Const
import com.xstore.tms.android.core.net.HttpConstants
import com.xstore.tms.android.entity.LoginedCarrier
import com.xstore.tms.android.entity.MonitorInfo
import com.xstore.tms.android.entity.common.RequestDecryptSearch
import com.xstore.tms.android.entity.common.ResponseDecryptMobile
import com.xstore.tms.android.entity.delivery.*
import com.xstore.tms.android.entity.net.performance.RequestDeliveryOrderDetail
import com.xstore.tms.android.entity.net.request.TaskHttpRequest
import com.xstore.tms.android.inerfaces.OnRideRouteSearched
import com.xstore.tms.android.repository.DecryptMobileRepository
import com.xstore.tms.android.repository.DeliverOrderRepository
import com.xstore.tms.android.utils.*
import com.xstore.tms.taskunline.Task
import com.xstore.tms.taskunline.TaskDBManager
import io.reactivex.functions.Consumer
import java.lang.Exception
import java.math.BigDecimal

/**
 * Created by hly on 2018/3/26.
 * email hly910206@gmail.com
 */

class DeliveryDetailPresenter(private var mView: DeliveryDetailContract.IView) : BasePresenter(),
        DeliveryDetailContract.IPresenter, NetworkStringResponse {
    private var repository: DeliverOrderRepository? = null
    private val decryptMobileRepository = DecryptMobileRepository()
    private var completeEntity:RequestDeliveryComplete?=null
    private var rejectEntity:RequestDeliveryReject?=null
    private var unlineTask:Task?=null
    private var storePint:LatLonPoint?= null
    private var delayEntity:DelayOrderEntity?=null

    override fun getDeliveryOrderDetail(stringExtra: String?) {
        if (repository == null) {
            repository = DeliverOrderRepository()
        }
        stringExtra.let {
            if(!NetworkUtil.isConnected()){
                ToastUtils.showToast(R.string.no_network)
                return
            }
            repository!!.getDeliveryOrderDetail(mTag, RequestDeliveryOrderDetail(stringExtra), this)
            loadUnlineTask(stringExtra!!)
        }

    }

    @SuppressLint("CheckResult")
    override fun loadUnlineTask(doNo: String) {
        TaskDBManager.getIntance().queryTaskById(doNo)
                .subscribe(Consumer {
                    if(it.isNotEmpty()){
                        unlineTask = it[0]
                        mView.showUnlineFailureTip(unlineTask?.getType())
                    }
                })
    }

    override fun orderConfirm(entity: RequestDeliveryComplete,storePoint: LatLonPoint) {
        if (repository == null) {
            repository = DeliverOrderRepository()
        }
        this.storePint = storePoint
        completeEntity = entity
        mView.showLoading()
        repository!!.orderConfirmSafe(mTag, entity, this)
    }

    override fun orderConfirmUnlineTask() {
        if (repository == null) {
            repository = DeliverOrderRepository()
        }
        mView.showLoading()
        delayEntity = parseUnlineTaskContent()
        if(delayEntity != null){
            repository?.orderUnlineTask(false,Const.UNLINETASK_TYPE_COMPLETE,mTag,delayEntity,this)
        }else{
            ToastUtils.showToast("解析错误")
        }
    }

    override fun orderReject(entity: RequestDeliveryReject, storePoint: LatLonPoint) {
        if (repository == null) {
            repository = DeliverOrderRepository()
        }
        rejectEntity = entity
        this.storePint = storePoint
        mView.showLoading()
        repository!!.orderRejectSafe(mTag, entity, this)
    }

    override fun orderRejectUnlineTask(reason:String) {
        if (repository == null) {
            repository = DeliverOrderRepository()
        }
        mView.showLoading()
        delayEntity = parseUnlineTaskContent()
        if(delayEntity != null){
            delayEntity?.failReason = reason
            repository?.orderUnlineTask(false,Const.UNLINETASK_TYPE_REJECT,mTag,delayEntity,this)
        }else{
            ToastUtils.showToast("解析错误")
        }
    }

    override fun orderExceptionDelivery(entity: RequestDeliveryException) {
        if (repository == null) {
            repository = DeliverOrderRepository()
        }
        mView.showLoading()
        repository!!.orderExceptionDelivery(mTag, entity, this)
    }

    /**
     * 解密客户电话号码
     */
    override fun decryptMobileNumber(doNo: String) {
        decryptMobileRepository.decryptMobileNumberSafe(mTag, RequestDecryptSearch(doNo = doNo), this)
    }

    override fun onDataReady(response: String?, requestCode: Int) {
        if(isCancelTag)return
        mView.hideLoading()
        when (requestCode) {
            HttpConstants.REQUEST_DELIVERY_ORDER_DETAIL -> {
                val detailEntity = JSON.parseObject(response, ResponseDeliveryOrder::class.java)
                mView.updateView(buildDetailResult(detailEntity))
            }
            HttpConstants.COMPLETE_DELIVERY_ORDER -> {
                val delivery: ResponseDeliveryComplete = JSON.parseObject(response, ResponseDeliveryComplete::class.java)
                if (delivery.success!!) {

                } else {
                    ToastUtils.showToast(delivery.errorMsg)
                }
                mView.orderConfirm(delivery.success!!)

            }
            HttpConstants.REJECT_DELIVERY_ORDER -> {
                val reject: ResponseDeliveryReject = JSON.parseObject(response, ResponseDeliveryReject::class.java)
                if (reject.success!!) {

                } else {
                    ToastUtils.showToast(reject.errorMsg)
                }
                mView.orderReject(reject.success!!)
            }
            HttpConstants.DECRYPT_MOBILE_NUMBER -> {
                val decryptMobile = JSON.parseObject(response, ResponseDecryptMobile::class.java)
                if (decryptMobile.success) {
                    mView.backMobileNumber(decryptMobile.mobile ?: "")
                } else {
                    ToastUtils.showToast(decryptMobile.errorMsg)
                }
            }
            HttpConstants.EXCEPTION_DELIVERY_ORDER -> {
                val excepDelivResponse = JSON.parseObject(response, ResponseExceptionDelivery::class.java)
                mView.orderExceptionDelivery(excepDelivResponse)
            }
            HttpConstants.DELAY_DELIVERY_ORDER ->{
                val delivery: ResponseDeliveryComplete = JSON.parseObject(response, ResponseDeliveryComplete::class.java)
                if (delivery.success!!) {
                    //离线任务重试成功
                    if(unlineTask != null && unlineTask?.getState() == 2){
                        //删除本地任务缓存
                        TaskDBManager.getIntance().deleteTask(unlineTask?.getId()!!)
                        when(unlineTask?.getType()){
                            Const.UNLINETASK_TYPE_COMPLETE->mView.orderConfirm(true)
                            Const.UNLINETASK_TYPE_REJECT ->mView.orderReject(true)
                        }
                    }
                } else {
                    ToastUtils.showToast(delivery.errorMsg)
                }
            }
        }
    }

    override fun onDataError(requestCode: Int, responseCode: Int, message: String?) {
        if(isCancelTag)return
        when(requestCode){
            HttpConstants.REQUEST_DELIVERY_ORDER_DETAIL -> {
                mView.updateError()
                mView.hideLoading()
            }
            HttpConstants.COMPLETE_DELIVERY_ORDER ->{
                //正常妥投失败，则开启离线任务
                ToastUtils.showToast("网络错误，正在开启离线任务")
                startUnlineTaskComplete()
            }
            HttpConstants.REJECT_DELIVERY_ORDER ->{
                //正常拒收失败
                ToastUtils.showToast("网络错误，正在开启离线任务")
                startUnlineTaskReject()
            }
            HttpConstants.DELAY_DELIVERY_ORDER ->{
                mView.hideLoading()
                if(Const.UNLINETASK_TYPE_COMPLETE== unlineTask?.getType()){
                    ToastUtils.showToast(R.string.unline_failure_complete_force)
                }
                if(Const.UNLINETASK_TYPE_REJECT == unlineTask?.getType()){
                    ToastUtils.showToast(R.string.unline_failure_reject_force)
                }
               addMonitor_delay()
            }
            else->{
                mView.hideLoading()
                ToastUtils.showToast(message)
            }
        }
    }

    private fun addMonitor_delay() {
        //埋点
        val json = org.json.JSONObject()
        try {
            json.put("latitude", delayEntity?.latitude)
            json.put("longitude", delayEntity?.longitude)
            json.put("mapType", "2")
            json.put("realDistance", delayEntity?.realDistance)
            json.put("customerDistance", delayEntity?.customerDistance)
        } catch (e: Exception) {
            LogUtils.e("json", e.message, e)
        }

        var flag = Const.FLAG_MONITOR_DELAY_COMPLETE
        if (unlineTask?.getType() == Const.UNLINETASK_TYPE_REJECT) {
            flag = Const.FLAG_MONITOR_DELAY_REJECT
            try {
                var pair = LocationxUtil.getCurLocation()
                json.put("latitude", pair.first)
                json.put("longitude", pair.second)
                json.put("mapType", "2")
                json.put("realDistance", "-1000")
                json.put("customerDistance", delayEntity?.customerDistance)
            } catch (e: Exception) {
                LogUtils.e("json", e.message, e)
            }
        }
        try {
            MonitorUtil.addMonitor(flag,delayEntity?.doNo!!, json.toString())
        }catch (e:Exception){
        }
    }


    private fun parseUnlineTaskContent():DelayOrderEntity?{
        return try {
            var entity:DelayOrderEntity ?=null
            val content= unlineTask?.getContent()
            val json = JSON.parseObject(content)
            if (json.containsKey("object")) {
                val jso = JSONObject.parseObject(json.getString("object"))
                entity = JSONObject.toJavaObject(jso, DelayOrderEntity::class.java)
            }
            entity
        }catch (e:Exception){
            null
        }
    }

    private fun startUnlineTaskComplete(){
        var pair= LocationxUtil.getCurLocation()
        val delayEntity= DelayOrderEntity()
        delayEntity.doNo = completeEntity?.doNo
        delayEntity.customerDistance = completeEntity?.customerDistance
        delayEntity.processStatus = 4
        delayEntity.completeTime = System.currentTimeMillis()
        delayEntity.carrierName = LoginedCarrier.carrierName
        delayEntity.carrierPhone = LoginedCarrier.carrierPhone
        delayEntity.carrierPin = LoginedCarrier.carrierPin
        delayEntity.mapType = 2
        delayEntity.locationStatus = -2
        delayEntity.latitude = "${pair.first}"
        delayEntity.longitude = "${pair.second}"
        delayEntity.realDistance = BigDecimal(-1000)
        repository?.orderUnlineTask(true,Const.UNLINETASK_TYPE_COMPLETE,
                mTag,delayEntity,this@DeliveryDetailPresenter)
        mView.startUnlineTask(delayEntity)
        //埋点
        val json = org.json.JSONObject()
        try {
            json.put("latitude", delayEntity.latitude)
            json.put("longitude", delayEntity.longitude)
            json.put("mapType", "2")
            json.put("realDistance", delayEntity.realDistance)
            json.put("customerDistance", delayEntity.customerDistance)
        } catch (e: Exception) {
            LogUtils.e("json", e.message, e)
        }

        var flag = Const.FLAG_MONITOR_COMPLETE
        MonitorUtil.addMonitor(flag, delayEntity.doNo!!, json.toString())

        mView.hideLoading()
        mView.orderConfirm(true)
    }

    private fun startUnlineTaskReject(){
        val delayEntity= DelayOrderEntity()
        delayEntity.doNo = rejectEntity?.doNo
        delayEntity.customerDistance = rejectEntity?.customerDistance
        delayEntity.processStatus = 5
        delayEntity.rejectTime = System.currentTimeMillis()
        delayEntity.failReason = rejectEntity?.failReason
        delayEntity.carrierName = LoginedCarrier.carrierName
        delayEntity.carrierPhone = LoginedCarrier.carrierPhone
        delayEntity.carrierPin = LoginedCarrier.carrierPin
        delayEntity.mapType = 2
        repository?.orderUnlineTask(true,Const.UNLINETASK_TYPE_REJECT,mTag,
                delayEntity,this@DeliveryDetailPresenter)
        //埋点
        try {
            MonitorUtil.addMonitor(Const.FLAG_MONITOR_REJECT, rejectEntity?.doNo!!, "")
        }catch (e:Exception){}

        mView.hideLoading()
        mView.orderReject(true)
    }

    private fun buildDetailResult(detail: ResponseDeliveryOrder): DeliveryDetailEntity {
        val entity = DeliveryDetailEntity()
        entity.orderId = detail.orderId!!.toString()
        entity.consignee = detail.customerName
        entity.mobilePhone = detail.customerMobile
        entity.address = detail.address
        entity.waybillNumber = detail.shippedQty
        entity.orderCreateDate = detail.orderCreateDate
        entity.doNo = detail.doNo?.toLong()
        entity.storeId = detail.storeId.toString()
        if (detail.leaveTime != null) {
            entity.leaveTime = BigDecimal(detail.leaveTime!!)
        }
        if (detail.lat != null) {
            entity.target[0] = detail.lat!!.toDouble()
        }
        if (detail.lng != null) {
            entity.target[1] = detail.lng!!.toDouble()
        }
        if(detail.storeLongitude != null){
            entity.store[0] = detail.storeLatitude!!.toDouble()
        }
        if(detail.storeLongitude != null){
            entity.store[1] = detail.storeLongitude!!.toDouble()
        }
        entity.lastDate = detail.expectedArriveTime
        entity.collectionId = detail.collectionId
        entity.infoList = detail.dOrderItemList?.map {
            val orderInfo: DeliveryDetailEntity.DeliveryOrderInfo = DeliveryDetailEntity.DeliveryOrderInfo()
            orderInfo.name = it.productName
            orderInfo.shippedQty = it.shippedQty
            orderInfo.expectedQty = it.expectedQty
            orderInfo.url = it.imagePathSuffix
            orderInfo.unit = it.uom
            orderInfo.saleMode = it.saleMode
            orderInfo.skuId = it.skuId
            orderInfo.oosFlag = it.oosFlag
            orderInfo
        }
        if(detail.transportPriority != null){
            entity.transportPriority = detail.transportPriority
        }
        entity.receivedTime = detail.receivedTime
        return entity
    }
}
