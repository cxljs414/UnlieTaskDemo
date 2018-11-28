package com.xstore.tms.android.entity.net

import java.util.*

class ResponseReturnOrder {
    /**
     * 取件单单号
     */
    var roNo: String? = null

    /**
     * 取件单分配状态：1.未分配  2.已分配
     */
    var returnOrderDistributionStatus: Int? = null

    /**
     * 取件单状态：1.未确认接单  2.已取消接单  3.待取件  4.取件完成
     */
    var returnOrderStatus: Int? = null

    /**
     * 取件单总取件数
     */
    var totalQuantity: Int? = null

    /**
     * 客户名字
     */
    var customerName: String? = null

    /**
     * 客户手机号
     */
    var customerMobile: String? = null

    /**
     * 客户手机 密文
     */
    var secretCustomerMobile: String? = null

    /**
     * 客户地址
     */
    var address: String? = null

    /**
     * 取件商品明细列表
     */
    var roItemList: List<ResponseReturnOrderItem>? = null

    /**
     * 修改时间
     */
    var modified: Date? = null

    /**
     * 分配任务时间
     */
    var distributionTime: Date? = null

    /**
     * 配送员接单时间
     */
    var acceptOrderTime: Date? = null

    /**
     * 取件单完成时间
     */
    var completeTime: Date? = null

    /**
     * 订单Id
     */
    var orderId: Long? = null
    /**
     * 是否校验商品完整性
     */
    var isShouldCheck: Int? = null
}
