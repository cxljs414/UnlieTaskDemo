package com.xstore.tms.android.entity.net.ro

/**
 * Created by wangwenming1 on 2018/3/20.
 */
data class RequestReturnOrderList (
        /**
         * 取件单状态：1.未确认接单  2.已取消接单  3.待取件  4.取件完成 5.取件终止
         */
        var returnOrderStatus: Int? = null
)