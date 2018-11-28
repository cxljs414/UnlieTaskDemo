package com.xstore.tms.android.entity.delivery

/**
 * 一键外呼接口入参
 */
data class RequestOutbound (
        val doNos: String,
        val address: String
)