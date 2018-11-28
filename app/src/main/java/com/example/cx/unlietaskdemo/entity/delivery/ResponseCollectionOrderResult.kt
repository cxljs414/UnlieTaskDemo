package com.xstore.tms.android.entity.delivery

import java.math.BigDecimal

/**
 * 集合单信息
 * Created by wangwenming1 on 2018/3/28.
 */
data class ResponseCollectionOrderResult(
        /**
         * 箱子数量
         */
        var containerQuantity: Int? = null,
        /**
         * 运单数量
         */
        var units: BigDecimal? = null
)