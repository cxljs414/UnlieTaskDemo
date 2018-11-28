package com.xstore.tms.android.entity.main

/**
 * Created by wangwenming1 on 2018/3/27.
 */
data class ResponseDeliveryCount (
        /**
         * 完成配送数量
         */
        var doCompletedNum: Int? = null,
        /**
         * 取件单数量
         */
        var roNum: Int? = null,
        /**
         * 待配送数量
         */
        var doReceivedNum: Int? = null,
        /**
         * 宅配单数量
         */
        var homeNum: Int? = null
)