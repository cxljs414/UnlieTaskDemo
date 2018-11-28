package com.xstore.tms.android.presenter

import com.alibaba.fastjson.JSON
import com.leinyo.httpclient.retrofit.NetworkStringResponse
import com.xstore.tms.android.base.BasePresenter
import com.xstore.tms.android.contract.CollectionOrderContract
import com.xstore.tms.android.core.net.HttpConstants
import com.xstore.tms.android.entity.collection.ResponseReceiveOrRefuse
import com.xstore.tms.android.entity.delivery.ResponseCollectionOrder
import com.xstore.tms.android.repository.CollectionOrderRepository
import com.xstore.tms.android.utils.ToastUtils
import org.apache.commons.lang3.StringUtils

/**
 * Created by wangwenming1 on 2018/3/31.
 */
class CollectionOrderPresenter(
        /**
         * 视图层
         */
        private val mView: CollectionOrderContract.IView

) : BasePresenter(), CollectionOrderContract.IPresenter, NetworkStringResponse {

    private val collectionOrderRepository = CollectionOrderRepository()

    override fun loadCollectionOrderList(collectionId: String) {
        mView.showLoading()
        collectionOrderRepository.getOrderListByCollectionId(mTag, collectionId, this)
    }

    override fun receiveCollectionOrder(collectionId: String) {
        mView.showLoading()
        collectionOrderRepository.receiveCollectionOrder(mTag, collectionId, this)
    }

    override fun refuseCollectionOrder(collectionId: String, rejectList: String?) {
        mView.showLoading()
        collectionOrderRepository.refuseCollectionOrder(mTag, collectionId, rejectList, this)
    }

    override fun onDataError(requestCode: Int, responseCode: Int, message: String?) {
        if(isCancelTag)return
        mView.hideLoading()
        when (requestCode) {
            HttpConstants.RECEIVE_COLLECTION_ORDER -> mView.receiveResponse(ResponseReceiveOrRefuse(false, message))
            HttpConstants.GET_COLLECTION_ORDER -> mView.collectionOrderListError(message!!)
            else -> ToastUtils.showToast(message)
        }
    }

    override fun onDataReady(response: String?, requestCode: Int) {
        if(isCancelTag)return
        mView.hideLoading()
        if (StringUtils.isBlank(response)) {
            ToastUtils.showToast("请求结果为空， - $requestCode")
            return
        }
        when (requestCode) {
            HttpConstants.GET_COLLECTION_ORDER -> {
                val collectionOrder = JSON.parseObject(response, ResponseCollectionOrder::class.java)
                mView.collectionOrderListReady(collectionOrder)
            }
            HttpConstants.RECEIVE_COLLECTION_ORDER -> {
                val receive = JSON.parseObject(response, ResponseReceiveOrRefuse::class.java)
                mView.receiveResponse(receive)
            }
            HttpConstants.REFUSE_COLLECTION_ORDER -> {
                val refuse = JSON.parseObject(response, ResponseReceiveOrRefuse::class.java)
                mView.refuseResponse(refuse)
            }
        }
    }
}