package com.xstore.tms.android.entity.delivery

/**
 * Created by wangwenming1 on 2018/3/28.
 */
data class ResponseCollectionOrder(
        /**
         * 是否成功
         */
        var success: Boolean? = null,
        /**
         * 请求时候返回的异常信息
         */
        var errorMsg: String? = null,
        /**
         * 根据集合单ID获取到的运单列表
         */
        var deliveryOrderList: List<ResponseDeliveryOrder>? = null,
        /**
         * 集合单信息
         */
        var collectionInfo: ResponseCollectionOrderResult? = null
)