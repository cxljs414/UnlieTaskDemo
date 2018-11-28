package com.xstore.tms.android.entity.home


import java.io.Serializable
import java.math.BigDecimal
import java.util.*

/**
 * Created by liqianlong
 * 2018 2018/3/23 16:38
 *
 * 废弃，已经替换为 com.xstore.tms.android.entity.home.HomeDeliveryOrder
 */
class ResponseHomeDeliveryDetail : Serializable {
    //编号
    var id: Long? = null

    //宅配运单号
    var homeNo: String? = null

    //原始订单号
    var orderId: Long? = null

    //宅配订单
    var homeId: String? = null

    //门店id
    var storeId: Long? = null

    //门店名称
    var storeName: String? = null

    //下单人
    var customerName: String? = null

    //下单人手机(带*)
    var customerMobile: String? = null

    //下单人手机密文
    var customerMobileSecret: String? = null

    //收货人
    var consigneeName: String? = null

    //收货人手机(带*)
    var consigneeMobile: String? = null

    //收货人手机号密文
    var consigneeMobileSecret: String? = null

    //配送日期
    var shipDate: Date? = null

    //期望送达时间
    var expectedArrivePeriodTime: String? = null

    //路区编号
    var areaId: Long? = null

    //路区名称
    var areaName: String? = null

    //地址详细信息
    var address: String? = null

    //纬度
    var lat: BigDecimal? = null

    //经度
    var lng: BigDecimal? = null

    //取件数
    var totalQuantity: Long? = null

    //取件单分配状态（1:未分配 2:已分配）
    var homeOrderDistributionStatus: Int? = null

    //取件处理状态（1:配送员未确认接单 2:配送员已取消接单 3:配送员已接单,待取件 4:妥投 5:客户拒收
    var homeOrderStatus: Int? = null

    //配送员姓名
    var carrierName: String? = null

    //配送员电话
    var carrierPhone: String? = null

    //配送员pin(自营)
    var carrierPin: String? = null

    //承运商编号
    var expressId: Int? = null

    //承运商类型（1:7fresh自营 2：第三方）
    var expressType: Int? = null

    //下单时间
    var underOrderTime: Date? = null

    //分配任务时间
    var distributionTime: Date? = null

    //配送员接单时间
    var acceptOrderTime: Date? = null

    //配送员取消接单时间
    var cancelOrderTime: Date? = null

    //宅配单妥投完成时间
    var completeTime: Date? = null

    //拒收时间
    var rejectTime: Date? = null

    //拒收原因
    var rejectReason: String? = null

    //提示配送员取件是否校验商品完整性
    var isShouldCheck: Int? = null

    //业务类别(1：宅配业务，2：待定)
    var businessType: Int? = null

    //状态（1：有效-1：无效）
    var status: Int? = null

    //创建人
    var creator: String? = null

    //创建时间
    var createTime: Date? = null

    //修改人
    var modifier: String? = null

    //修改时间
    var modifyTime: Date? = null
}
