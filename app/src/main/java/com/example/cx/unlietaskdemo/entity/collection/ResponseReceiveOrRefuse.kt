package com.xstore.tms.android.entity.collection

/**
 * Created by wangwenming1 on 2018/3/31.
 */
data class ResponseReceiveOrRefuse(
        /**
         * 是否成功
         */
        var success: Boolean = false,
        /**
         * 失败消息
         */
        var errorMsg: String? = null
)