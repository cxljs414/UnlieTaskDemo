package com.xstore.tms.android.presenter

import com.alibaba.fastjson.JSON
import com.leinyo.httpclient.retrofit.NetworkStringResponse
import com.xstore.tms.android.base.BasePresenter
import com.xstore.tms.android.contract.HomeDeliveryReceiveContract
import com.xstore.tms.android.core.net.HttpConstants
import com.xstore.tms.android.entity.LoginedCarrier
import com.xstore.tms.android.entity.home.*
import com.xstore.tms.android.repository.HomeDeliveryRepository
import com.xstore.tms.android.utils.ToastUtils

/**
 * Created by hly on 2018/3/30.
 * email hly910206@gmail.com
 */

class HomeDeliveryPresenter(private var mView: HomeDeliveryReceiveContract.IView) : BasePresenter(), HomeDeliveryReceiveContract.IPresenter, NetworkStringResponse {

    private var mRepository: HomeDeliveryRepository? = null
    private val mPageSize = 10
    private var mPageNo: Long = 1

    override fun getHomeReceiveList(isPull: Boolean) {
        if (mRepository == null) {
            mRepository = HomeDeliveryRepository()
        }
        val request = RequestHomeDeliveryList()
        request.carrierPin = LoginedCarrier.carrierPin
        if (isPull) {
            request.pageNo = mPageNo
        } else {
            request.pageNo = mPageNo + 1
        }
        request.pageSize = mPageSize
        request.searchType = 1
        mRepository!!.getHomeDeliveryList(mTag, request, this)
    }

    override fun onDataError(requestCode: Int, responseCode: Int, message: String?) {
        if(isCancelTag)return
        ToastUtils.showToast(message)
        when (requestCode) {
            HttpConstants.HOME_DELIVERY_LIST -> {
                mView.getHomeDeliveryList(null, false)
            }
        }
    }

    override fun sendBack(isCancel: Boolean, list: List<HomeDeliveryOrder>) {
        val request = RequestHomeDeliveryHandle()
        val shipbuilder = StringBuilder()
        request.handleValue = if (isCancel) 2 else 3
        list.filter { it.isCheck }.forEach {
            if (list.indexOf(it) != list.size - 1) {
                shipbuilder.append(it.homeNo + ",")
            } else {
                shipbuilder.append(it.homeNo)
            }
        }
        request.homeNos = shipbuilder.toString()
        request.storeId = list[0].storeId
        request.modifier = LoginedCarrier.carrierPin
        mRepository!!.sendBackDeliveryListSafe(mTag, request, this)
    }

    override fun getHomeDeliveryList(isPull: Boolean) {
        if (mRepository == null) {
            mRepository = HomeDeliveryRepository()
        }
        val request = RequestHomeDeliveryList()
        request.carrierPin = LoginedCarrier.carrierPin
        if (isPull) {
            request.pageNo = mPageNo
        } else {
            request.pageNo = mPageNo + 1
        }
        request.pageSize = mPageSize
        request.searchType = 2
        mRepository!!.getHomeDeliveryList(mTag, request, this)
    }

    override fun onDataReady(response: String?, requestCode: Int) {
        if(isCancelTag)return
        when (requestCode) {
            HttpConstants.HOME_DELIVERY_LIST -> {
                val deliveryList = JSON.parseObject(response, ResponseHomeDeliveryList::class.java)
                if (deliveryList?.returnPage != null) {
                    mView.getHomeDeliveryList(deliveryList.returnPage!!.result!!, deliveryList.returnPage!!.result!!.size == 10)
                    mPageNo = deliveryList.returnPage!!.pageIndex!!
                }
            }
            HttpConstants.HOME_DELIVERY_HANDLE -> {
                mView.sendBack(true)
            }
            HttpConstants.HOME_DELIVERY_DETAIL -> {
                val detail = JSON.parseObject(response, HomeDeliveryOrder::class.java)
                if (detail != null) {
                    mView.getDeliveryDetail(detail)
                }
            }
        }
    }


    override fun getHomeDeliveryDetail(homeNo: String) {
        if (mRepository == null) {
            mRepository = HomeDeliveryRepository()
        }
        mRepository!!.getHomeDeliveryDetial(mTag, homeNo, this)
    }
}
