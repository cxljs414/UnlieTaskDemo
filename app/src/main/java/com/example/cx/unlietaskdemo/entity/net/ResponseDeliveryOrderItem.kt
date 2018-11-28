package com.xstore.tms.android.entity.net

import java.math.BigDecimal
import java.util.*

/**
 * Created by wangwenming1 on 2018/3/19.
 */

class ResponseDeliveryOrderItem {
    /**
     * 主键
     */
    var id: Long? = null

    /**
     * 承运商类型（1:7fresh自营 2：第三方）
     */
    var expressType: Int? = null

    /**
     * 承运商ID
     */
    var expressId: Int? = null

    /**
     * 门店编号
     */
    var storeId: Long? = null

    /**
     * 门店名称
     */
    var storeName: String? = null

    /**
     * 路区编号
     */
    var areaId: Long? = null

    /**
     * 路区
     */
    var areaName: String? = null

    /**
     * 集合单号
     */
    var collectionId: String? = null

    /**
     * 配送员pin(自营)
     */
    var carrierPin: String? = null

    /**
     * 配送员姓名
     */
    var carrierName: String? = null

    /**
     * 配送员电话
     */
    var carrierPhone: String? = null

    /**
     * 发货单号/运单号/DO单号
     */
    var doNo: String? = null

    /**
     * skuID
     */
    var skuId: Long? = null

    /**
     * 商品名称
     */
    var productName: String? = null

    /**
     * 期望发货数量
     */
    var expectedQty: BigDecimal? = null

    /**
     * 单位
     */
    var uom: String? = null

    /**
     * 价格
     */
    var price: BigDecimal? = null

    /**
     * 促销商品
     */
    var promoteFlag: Short = 0

    /**
     * 批次编码
     */
    var lotNo: String? = null

    /**
     * 供应商ID
     */
    var supplierId: Long? = null

    /**
     * 商家ID
     */
    var merchantId: Long? = null

    /**
     * 特殊商品标识，如“餐饮”
     */
    var skuSpecFlag: Int? = null

    /**
     * 客户特殊要求标识，如"鱼需宰杀"
     */
    var customerSpecFlag: Int? = null

    /**
     * 实发数
     */
    var shippedQty: BigDecimal? = null

    /**
     * 赠品
     */
    var giftFlag: Int? = null

    /**
     * 实际出库重量
     */
    var shippedWt: BigDecimal? = null

    /**
     * 状态（1：有效-1：无效）
     */
    var status: Int? = null

    /**
     * 创建人
     */
    var creator: String? = null

    /**
     * 创建时间
     */
    var created: Date? = null

    /**
     * 修改人
     */
    var modifier: String? = null

    /**
     * 修改时间
     */
    var modified: Date? = null


    /**
     * 图片
     */
    var imagePathSuffix: String? = null

    var saleMode: Int? = null

    /**
     * 是否缺货  0否 1是
     */
    var oosFlag:Int?=null
}
