package com.xstore.tms.android.entity.delivery

import com.xstore.tms.android.entity.net.ResponseDeliveryOrderItem
import java.math.BigDecimal
import java.util.*

/**
 * Created by wangwenming1 on 2018/3/19.
 */

class ResponseDeliveryOrder {
    /**
     * 集合单号
     */
    var collectionId: String? = null

    /**
     * 发货单号/运单号/DO单号
     */
    var doNo: String? = null

    /**
     * 处理状态（1：待处理 2：已接收 3：拒接收 4：妥投 5：拒收 6：强制妥投）
     */
    var processStatus: Int? = null

    /**
     * 客户名
     */
    var customerName: String? = null

    /**
     * 详细地址
     */
    var address: String? = null

    /**
     * 手机号
     */
    var customerMobile: String? = null

    /**
     * 客户手机 密文
     */
    var secretCustomerMobile: String? = null

    /**
     * 配送日期
     */
    var shipDate: Date? = null

    /**
     * 用户收货时间段(小时)
     */
    var expectedArrivePeriodTime: String? = null

    /**
     * 用户最晚收货时间
     */
    var expectedArriveTime: Date? = null

    /**
     * 发货数量
     */
    var shippedQty: BigDecimal? = null

    /**
     * 用户下单时间
     */
    var orderCreateDate: Date? = null

    /**
     * 订单备注
     */
    var orderRemark: String? = null

    /**
     * 商品信息列表
     */
    var dOrderItemList: List<ResponseDeliveryOrderItem>? = null

    /**
     * 妥投时间
     */
    var completeTime: Date? = null

    /**
     * 拒收时间
     */
    var rejectTime: Date? = null

    /**
     * 外呼时间
     */
    var callTime: Date? = null

    /**
     * 目的地纬度
     */
    var lat: BigDecimal? = null
    /**
     * 目的地精度
     */
    var lng: BigDecimal? = null
    /**
     * 门店id
     */
    var storeId: Long? = null
    /**
     * 出发时间
     */
    var leaveTime: Long? = null
    /**
     * 运单id
     */
    var orderId: Long? = null

    /**
     * 门店经度
     */
    var storeLongitude:BigDecimal? = null

    /**
     * 门店纬度
     */
    var storeLatitude:BigDecimal? = null

    /**
     * 配送优先级
     *  1:预约达, 99:极速达
     */
    var transportPriority:Int? = null

    /**
     *订单来源主体 1. 7fresh 2. JD 3. JdHome 4.HuaRun
     */
    var sellerOrderSource:Int?=null

    /**
     * 接单时间
     */
    var receivedTime:Long?=null
}
