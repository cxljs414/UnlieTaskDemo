package com.xstore.tms.android.presenter

import com.alibaba.fastjson.JSON
import com.leinyo.httpclient.retrofit.NetworkStringResponse
import com.xstore.tms.android.base.BasePresenter
import com.xstore.tms.android.contract.PerformanceTvContract
import com.xstore.tms.android.core.net.HttpConstants
import com.xstore.tms.android.entity.net.performance.RequestCompleteDoAndRoCount
import com.xstore.tms.android.entity.net.performance.ResponseCompleteDoAndRoCount
import com.xstore.tms.android.repository.CourierPerformanceRepository
import com.xstore.tms.android.utils.ToastUtils
import org.apache.commons.lang3.StringUtils

class PerformanceTvPresenter(val iView: PerformanceTvContract.IView)
    : BasePresenter(), PerformanceTvContract.IProcenter, NetworkStringResponse {


    val repository = CourierPerformanceRepository()

    override fun loadCompleteCount(dateType: String?) {
        repository.getCompleteDoAndRoCount(HttpConstants.COMPLETE_DO_AND_RO_COUNT_QN, mTag, RequestCompleteDoAndRoCount(dateType), this)
    }

    /**
     * 调用失败回调
     */
    override fun onDataError(requestCode: Int, responseCode: Int, message: String?) {
        if(isCancelTag)return
        ToastUtils.showToast(message)
    }

    /**
     * 服务器返回成功回调 自己反序列化
     *
     * @param response 网络请求返信息
     */
    override fun onDataReady(response: String?, requestCode: Int) {
        if(isCancelTag)return
        if (StringUtils.isBlank(response)) {
            ToastUtils.showToast("请求结果为空， - $requestCode")
            return
        }
        var data = JSON.parseObject(response, ResponseCompleteDoAndRoCount::class.java)
        iView.completeReady(data)
    }

}