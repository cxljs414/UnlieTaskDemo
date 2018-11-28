package com.xstore.tms.android.entity.delivery

/**
 * 拒收请求参数
 */
data class RequestDeliveryException(


        /**
         * 发货单号/运单号/DO单号
         */
        var doNo: String? = null,
        /**
         * 门店id
         */
        var storeId: String? = null,
        /**
         * 订单号
         */
        var orderId: String? = null

)