package com.xstore.tms.android.presenter

import android.text.TextUtils
import com.leinyo.httpclient.retrofit.NetworkStringResponse
import com.xstore.tms.android.BuildConfig
import com.xstore.tms.android.base.BasePresenter
import com.xstore.tms.android.contract.DeliveryUnusualContract
import com.xstore.tms.android.core.Const
import com.xstore.tms.android.entity.delivery.*
import com.xstore.tms.android.repository.HomeDeliveryRepository
import com.xstore.tms.android.utils.LocationxUtil
import com.xstore.tms.android.utils.LogUtils
import com.xstore.tms.android.utils.MonitorUtil
import com.xstore.tms.android.utils.ToastUtils
import junit.framework.Assert
import org.json.JSONObject
import java.math.BigDecimal

/**
 * Created by hly on 2018/7/20.
 * email hly910206@gmail.com
 */
class DeliveryUnusualPresenter(private var mView: DeliveryUnusualContract.IView) : BasePresenter(), DeliveryUnusualContract.IPresenter, NetworkStringResponse {

    private var mRepository: HomeDeliveryRepository? = null
    private var customerDistance:BigDecimal?=null
    private var homeNo:String?=null


    override fun buildUnusualEntity(entity: ResponseExceptionDelivery): DeliveryUnusualEntity {
        val unusualEntity = DeliveryUnusualEntity()
        unusualEntity.list = listOf()
        if (entity.deliveryOrderItems != null) {
            unusualEntity.list = entity.deliveryOrderItems.map {
                val unusualListEntity = DeliveryUnusualDataListEntity()
                unusualListEntity.expectedQty = it.expectedQty
                if (!TextUtils.isEmpty(it.imagePathSuffix)) {
                    unusualListEntity.imageUrl = BuildConfig.URL_HOST + "s400x400_" + it.imagePathSuffix
                    unusualListEntity.thumbUrl = BuildConfig.URL_HOST + it.imagePathSuffix
                }
                unusualListEntity.productName = it.productName
                unusualListEntity.shippedQty = it.shippedQty
                unusualListEntity.unit = it.unit
                unusualListEntity.saleMode = it.saleMode
                unusualListEntity.skuId = it.skuId
                unusualListEntity.skuUuid = it.skuUuid
                unusualListEntity.abnormalCompleteFlag = it.abnormalCompleteFlag
                unusualListEntity
            }
        }
        return unusualEntity
    }


    override fun commitExceptionDelivery(homeNo: String, skuList: ArrayList<AbnormalCompleteSkus>, customerDistance: BigDecimal) {
        if (mRepository == null) {
            mRepository = HomeDeliveryRepository()
        }
        val request = ExceptionDeliveryRoot()
        request.doNo = homeNo
        this.homeNo = homeNo
        request.abnormalCompleteSkus = skuList
        request.customerDistance = customerDistance
        this.customerDistance = customerDistance
        mView.showLoading()
        mRepository!!.commitExceptionDelivery(mTag, request, this)
    }


    override fun onDataError(requestCode: Int, responseCode: Int, message: String?) {
        if(isCancelTag)return
        mView.hideLoading()
        ToastUtils.showToast(message)
        //埋点
        val json = JSONObject()
        try {
            var pair= LocationxUtil.getCurLocation()
            json.put("latitude", pair.first)
            json.put("longitude", pair.second)
            json.put("mapType", "2")
            json.put("realDistance", customerDistance)
            json.put("customerDistance", customerDistance)
        } catch (e: Exception) {
            LogUtils.e("json", e.message, e)
        }

        homeNo.let {
            MonitorUtil.addMonitor(Const.FLAG_MONITOR_EXCEPTION, homeNo!!, json.toString())
        }

    }

    override fun onDataReady(response: String?, requestCode: Int) {
        if(isCancelTag)return
        mView.hideLoading()
        mView.handSuccess();
    }
}