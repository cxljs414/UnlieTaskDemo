package com.xstore.tms.android.entity.home

import java.util.*

/**
 * Created by wangwenming1 on 2018/3/30.
 */
data class RequestHomeDeliveryList(
        /**
         * 当前页数
         */
        var pageNo: Long = 1,
        /**
         * 每页显示条数
         */
        var pageSize: Int = 15,
        /**
         * 搜索的状态(1:配送员未确认接单 2,配送员已接单查询 3,配送员业绩查询)
         */
        var searchType: Int? = null,
        /**
         * 配送员 PIN
         */
        var carrierPin: String? = null,
        /**
         * 查询时间维度（thisMonth:查询本月  lastMonth:查询上月  today:查询当天）
         */
        var dateType: String? = null,
        /**
         * 妥投开始时间
         */
        var fromDate: Date? = null,
        /**
         * 妥投结束时间
         */
        var toDate: Date? = null,
        /**
         * 拒收开始时间
         */
        var rejectFromDate: Date? = null,
        /**
         * 拒收结束时间
         */
        var rejectToDate: Date? = null
)