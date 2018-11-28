package com.xstore.tms.android.core

object Const {
    /**
     * 妥投
     */
    const val UNLINETASK_TYPE_COMPLETE = "complete"
    /**
     * 拒收
     */
    const val UNLINETASK_TYPE_REJECT = "reject"
    /**
     * 埋点
     */
    const val UNLINETASK_TYPE_MONITOR = "monitor"


    /**
     * 埋点类型
     */
    /**
     * 运单妥投
     */
    const val FLAG_MONITOR_COMPLETE = 1
    //运单异常妥投
    const val FLAG_MONITOR_EXCEPTION = 2
    //运单离线妥投
    const val FLAG_MONITOR_DELAY_COMPLETE = 3
    //运单拒收
    const val FLAG_MONITOR_REJECT = 4
    //运单离线拒收
    const val FLAG_MONITOR_DELAY_REJECT = 5
    //取件完成
    const val FLAG_MONITOR_RECEIVE_COMPLETE = 24
    //取件终止
    const val FLAG_MONITOR_RECEIVE_TERMINATED = 25
    //宅配完成
    const val FLAG_MONITOR_HOME_COMPLETE = 34
    //宅配终止
    const val FLAG_MONITOR_HOME_TERMINATED = 35
    //订单详情页面路径规划
    const val FLAG_MONITOR_ORDER_ROUTE = 36
    //配送员定位
    const val FLAG_MONITEOR_LOCATION = 99
}