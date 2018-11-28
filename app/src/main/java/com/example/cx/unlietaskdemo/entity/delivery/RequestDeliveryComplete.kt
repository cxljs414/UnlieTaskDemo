package com.xstore.tms.android.entity.delivery

import java.math.BigDecimal

/**
 * 确认妥投完成请求参数
 */
data class RequestDeliveryComplete(
        /**
         * 集合单号
         */
        var collectionId: String? = null,

        /**
         * 发货单号/运单号/DO单号
         */
        var doNo: String? = null,
        /**
         * 顾客地址到门店的距离(单位：km，小数点后两位)
         */
        var customerDistance: BigDecimal? = null,
        /**
         * 随机字符
         */
        var tempStr: String? = null,

        /**
         * MD5加密
         */
        var md5Str: String? = null
)