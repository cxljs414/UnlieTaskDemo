package com.xstore.tms.android.entity.home

import java.io.Serializable
import java.util.*

/**
 * Created by wangwenming1 on 2018/3/30.
 */
class HomeDeliveryOrder : Serializable {

    /**
     * 取件处理状态（1:配送员未确认接单 2:配送员已取消接单 3:配送员已接单,待取件 4:妥投 5:客户拒收
     */
    var homeOrderStatus: Int? = null

    /**
     * 宅配运单号
     */
    var homeNo: String? = null

    /**
     * 宅配订单
     */
    var homeId: String? = null

    /**
     * 门店id
     */
    var storeId: Long? = null

    /**
     * 收货人
     */
    var consigneeName: String? = null

    /**
     * 收货人手机(带*)
     */
    var consigneeMobile: String? = null

    /**
     * 收货人手机号密文
     */
    var consigneeMobileSecret: String? = null

    /**
     * 收货人地址
     */
    var address: String? = null

    /**
     * 拒收原因
     */
    var rejectReason: String? = null

    /**
     * 拒收时间
     */
    var rejectTime: Date? = null

    /**
     * 宅配单妥投完成时间
     */
    var completeTime: Date? = null

    /**
     * 创建人
     */
    var creator: String? = null

    /**
     * 修改人
     */
    var modifier: String? = null

    /**
     * 取件数
     */
    var totalQuantity: Long? = null

    /**
     * 下单时间
     */
    var underOrderTime: Date? = null

    /**
     * 期望送达日期
     */
    var shipDate: Date? = null

    /**
     * 用户收货时间段
     */
    var expectedArrivePeriodTime: String? = null

    /**
     * 用户最晚收货时间
     */
    var expectedArriveTime: Date? = null

    /**
     * 记录点击状态
     */
    var isCheck: Boolean = false

    var leaveTime: Long? = null
}
