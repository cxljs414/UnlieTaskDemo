package com.xstore.tms.android.entity.collection

/**
 * Created by wangwenming1 on 2018/3/31.
 */
data class RequestReceiveOrRefuse(
        /**
         * 集合单号
         */
        val collectionId: String,
        /**
         * 拒绝接单原因集合
         */
        //val rejectList: List<String>? = null

        /**
         * 拒绝接单原因集合
         */
        val rejectionReason: String? = null
)