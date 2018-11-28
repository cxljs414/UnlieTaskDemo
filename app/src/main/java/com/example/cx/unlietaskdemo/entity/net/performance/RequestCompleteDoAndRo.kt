package com.xstore.tms.android.entity.net.performance

/**
 * Created by wangwenming1 on 2018/3/19.
 */
data class RequestCompleteDoAndRo (
    /**
     * 查询时间维度（thisMonth:查询本月  lastMonth:查询上月  today:查询当天）
     */
    var dateType: String? = null,

    var page: Int? = null
)