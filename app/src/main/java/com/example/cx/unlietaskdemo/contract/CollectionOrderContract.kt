package com.xstore.tms.android.contract

import com.xstore.tms.android.base.IBaseView
import com.xstore.tms.android.entity.collection.ResponseReceiveOrRefuse
import com.xstore.tms.android.entity.delivery.ResponseCollectionOrder

/**
 * Created by wangwenming1 on 2018/3/31.
 */
interface CollectionOrderContract {

    interface IPresenter {
        /**
         * 载入集合单列表
         */
        fun loadCollectionOrderList(collectionId: String)
        /**
         * 确认接受集合单
         */
        fun receiveCollectionOrder(collectionId: String)
        /**
         * 拒绝接受集合单
         */
        fun refuseCollectionOrder(collectionId: String, rejectList: String?)
    }

    interface IView : IBaseView{
        /**
         * 返回集合单列表
         */
        fun collectionOrderListReady(responseCollectionOrder: ResponseCollectionOrder)
        /**
         * 请求集合单异常
         */
        fun collectionOrderListError(message: String)
        /**
         * 确认接受集合单 请求结果
         */
        fun receiveResponse(responseReceiveOrRefuse: ResponseReceiveOrRefuse)
        /**
         * 拒绝接受集合单 请求结果
         */
        fun refuseResponse(responseReceiveOrRefuse: ResponseReceiveOrRefuse)
    }
}