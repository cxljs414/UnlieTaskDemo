package com.xstore.tms.android.entity.delivery

/**
 * 根据集合单号获取待配送列表
 * Created by wangwenming1 on 2018/3/28.
 */
data class RequestCollectionOrders(
        /**
         * 集合单号
         */
        val collectionId: String
)