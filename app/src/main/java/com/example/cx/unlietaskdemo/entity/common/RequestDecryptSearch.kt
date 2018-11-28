package com.xstore.tms.android.entity.common

/**
 * Created by wangwenming3 on 2018/04/11.
 * @description 解密查询实体
 */
data class RequestDecryptSearch(
        /**
         * 发货单号/运单号/DO单号
         */
        val doNo: String? = null,
        /**
         * RO单号
         */
        val roNo: String? = null,
        /**
         * 宅配运单号
         */
        val homeNo: String? = null,
        /**
         * 随机字符
         */
        var tempStr: String? = null,
        /**
         * MD5加密
         */
        var md5Str: String? = null
)