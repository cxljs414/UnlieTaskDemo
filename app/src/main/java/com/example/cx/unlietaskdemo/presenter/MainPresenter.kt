package com.xstore.tms.android.presenter

import com.alibaba.fastjson.JSON
import com.leinyo.httpclient.exception.HttpErrorEnum
import com.leinyo.httpclient.retrofit.NetworkStringResponse
import com.xstore.tms.android.base.BasePresenter
import com.xstore.tms.android.contract.MainContract
import com.xstore.tms.android.core.net.HttpConstants
import com.xstore.tms.android.entity.main.ResponseDeliveryCount
import com.xstore.tms.android.repository.MainRepository
import com.xstore.tms.android.utils.ToastUtils
import org.apache.commons.lang3.StringUtils

/**
 * Created by wangwenming1 on 2018/3/27.
 */
class MainPresenter(
        /**
         * 视图层
         */
        var iView: MainContract.IView
) : BasePresenter(), MainContract.IPresenter, NetworkStringResponse {

    val repository: MainRepository = MainRepository()

    /**
     * 加载今日配送数量
     */
    override fun loadDeliveryCount() {
        iView.showLoading()
        repository.getDeliveryCount(mTag, this)
    }

    override fun onDataError(requestCode: Int, responseCode: Int, message: String?) {
        iView.hideLoading()
        if(responseCode == 0||
                responseCode == HttpErrorEnum.RESPONSE_ERR_CONNECT.type ||
                responseCode == HttpErrorEnum.RESPONSE_ERR_TIME_OUT.type){
            iView.showNoNet()
        }else{
            ToastUtils.showToast(message)
        }
    }

    override fun onDataReady(response: String?, requestCode: Int) {
        iView.hideLoading()
        if (StringUtils.isBlank(response)) {
            ToastUtils.showToast("请求结果为空， - $requestCode")
            return
        }
        when(requestCode) {
            HttpConstants.DELIVERY_COUNT -> {
                var deliveryCount = JSON.parseObject(response, ResponseDeliveryCount::class.java)
                iView.onDeliveryReady(deliveryCount)
            }
        }
    }
}