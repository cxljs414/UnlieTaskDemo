package com.xstore.tms.android.entity.home

/**
 * 宅配状态提交参数 - 确认配送，取消配送，妥投，用户拒绝等操作
 * Created by wangwenming1 on 2018/3/30.
 */
data class RequestHomeDeliveryHandle(

        /**
         * 多个宅配运单号号以逗号分隔
         */
        var homeNos: String? = null,
        /**
         * 2,"配送员取消接单";3,"配送员已接单,待取件";4,"妥投" ;5,"客户拒收"
         */
        var handleValue: Int? = null,
        /**
         * 门店id
         */
        var storeId: Long? = null,
        /**
         * 拒绝原因
         */
        var rejectReason: String? = null,
        /**
         * 修改人
         */
        var modifier: String? = null,
        /**
         * 随机字符
         */
        var tempStr: String? = null,
        /**
         * MD5加密
         */
        var md5Str: String? = null
)