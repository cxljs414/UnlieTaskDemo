package com.xstore.tms.android.entity.delivery

import java.math.BigDecimal

class DelayOrderEntity {

    /**
     * 运单号
     */
    var doNo:String?=null
    /**
     * 处理状态 4：妥投 5：拒收
     */
    var processStatus:Int?=null

    /**
     * 妥投时间 processStatus=4,必须
     */
    var completeTime:Long?=null

    /**
     *拒收时间 processStatus=5,必须
     */
    var rejectTime:Long?=null

    /**
     *拒收原因  processStatus=5,必须
     */
    var failReason:String?= null

    /**
     * 配送员pin
     */
    var carrierPin:String?=null

    /**
     * 配送员姓名
     */
    var carrierName:String?=null

    /**
     * 配送员电话
     */
    var carrierPhone:String?=null

    /**
     *顾客地址到门店的距离(单位：km，小数点后两位)
     */
    var customerDistance:BigDecimal?=null

    /**
     * 经度  processStatus=4,必须
     */
    var longitude:String?=null

    /**
     *纬度  processStatus=4,必须
     */
    var latitude:String?=null

    /**
     * 地图坐标类型 processStatus=4,必须
     */
    var mapType:Int?=null

    /**
     * 配送员实际走了多少米
     */
    var realDistance:BigDecimal?=null

    /**
     * 配送员定位状态  1：有效，-1：无效, -2:未能定位
    定位失败，前端传参：longitude（经度）为0，latitude（纬度）为0， 状态status为-2，real_distance（配送员实际配送距离）为-1000, customer_distance为-1000
     */
    var locationStatus:Int?=null

    /**
     *随机字符
     */
    var tempStr:String?=null

    /**
     * MD5加密
     */
    var md5Str:String?=null
}