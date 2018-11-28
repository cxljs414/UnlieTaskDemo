package com.xstore.tms.android.presenter

import com.alibaba.fastjson.JSON
import com.leinyo.httpclient.retrofit.NetworkStringResponse
import com.xstore.tms.android.base.BasePresenter
import com.xstore.tms.android.contract.HomeDeliveryDetailContract
import com.xstore.tms.android.core.Const
import com.xstore.tms.android.core.net.HttpConstants
import com.xstore.tms.android.entity.LoginedCarrier
import com.xstore.tms.android.entity.common.RequestDecryptSearch
import com.xstore.tms.android.entity.common.ResponseDecryptMobile
import com.xstore.tms.android.entity.delivery.AbnormalCompleteSkus
import com.xstore.tms.android.entity.delivery.ExceptionDeliveryRoot
import com.xstore.tms.android.entity.home.RequestHomeDeliveryHandle
import com.xstore.tms.android.repository.DecryptMobileRepository
import com.xstore.tms.android.repository.HomeDeliveryRepository
import com.xstore.tms.android.utils.MonitorUtil
import com.xstore.tms.android.utils.ToastUtils
import org.apache.commons.lang3.StringUtils

/**
 * Created by hly on 2018/4/2.
 * email hly910206@gmail.com
 */

class HomeDeliveryDetailPresenter(private var mView: HomeDeliveryDetailContract.IView) : BasePresenter(), HomeDeliveryDetailContract.IPresenter, NetworkStringResponse {


    private var mRepository: HomeDeliveryRepository? = null
    private var homeNo:String?=null
    private var isConfirm = true

    private val decryptMobileRepository = DecryptMobileRepository()

    override fun confirm(homeNo: String, storeId: Long) {
        if (mRepository == null) {
            mRepository = HomeDeliveryRepository()
        }
        isConfirm = true
        this.homeNo = homeNo
        val request = RequestHomeDeliveryHandle()
        request.handleValue = 4
        request.homeNos = homeNo
        request.modifier = LoginedCarrier.carrierPin
        request.storeId = storeId
        mView.showLoading()
        mRepository!!.sendBackDeliveryListSafe(mTag, request, this)
    }

    override fun reject(homeNo: String, reason: String, storeId: Long) {
        if (mRepository == null) {
            mRepository = HomeDeliveryRepository()
        }
        isConfirm = false
        this.homeNo = homeNo
        val request = RequestHomeDeliveryHandle()
        request.handleValue = 5
        request.homeNos = homeNo
        request.modifier = LoginedCarrier.carrierPin
        request.rejectReason = reason
        request.storeId = storeId
        mView.showLoading()
        mRepository!!.sendBackDeliveryListSafe(mTag, request, this)
    }

    override fun decryptMobileNumber(homeNo: String)
            = decryptMobileRepository.decryptMobileNumberSafe(mTag, RequestDecryptSearch(homeNo = homeNo), this)

    override fun onDataError(requestCode: Int, responseCode: Int, message: String?) {
        if(isCancelTag)return
        mView.hideLoading()
        ToastUtils.showToast(message)
        if(requestCode == HttpConstants.HOME_DELIVERY_HANDLE){
            try {
                val flag= if(isConfirm)Const.FLAG_MONITOR_HOME_COMPLETE
                else Const.FLAG_MONITOR_HOME_TERMINATED
                MonitorUtil.addMonitor(flag, homeNo!!, "")
            } catch (e: Exception) {
            }
        }
    }

    override fun onDataReady(response: String?, requestCode: Int) {
        if(isCancelTag)return
        if (StringUtils.isBlank(response)) {
            mView.hideLoading()
            ToastUtils.showToast("请求结果为空， - $requestCode")
            return
        }
        when (requestCode) {
            HttpConstants.HOME_DELIVERY_HANDLE -> mView.sendBack(true)
            HttpConstants.DECRYPT_MOBILE_NUMBER -> {
                val decryptMobile = JSON.parseObject(response, ResponseDecryptMobile::class.java)
                if (decryptMobile.success) {
                    mView.backMobileNumber(decryptMobile.mobile ?: "")
                } else {
                    mView.hideLoading()
                    ToastUtils.showToast(decryptMobile.errorMsg)
                }
            }
        }

    }
}
