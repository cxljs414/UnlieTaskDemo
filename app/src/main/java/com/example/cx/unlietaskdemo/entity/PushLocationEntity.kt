package com.xstore.tms.android.entity

import java.math.BigDecimal

/**
 * gis定位上传实体类
 */
data class PushLocationEntity(
        /**
         * 地图坐标类型（1：GPS坐标值 2:高德地图坐标值 3:百度地图坐标值 4:mapbar）
         */
        var mapType:Int = 2,

        /**
         * 所属门店编号
         */
        var storeId:BigDecimal ?= null,

        /**
         * 所属门店名称
         */
        var storeName:String ?= null,

        /**
         * 查询批次号
         */
        var version:Long ?= null,

        /**
         * 纬度
         */
        var lat:BigDecimal ?= null,

        /**
         * 经度
         */
        var lng:BigDecimal ?= null,

        /**
         * 骑手到门店的距离(单位：m)
         */
        var carrierDistance:BigDecimal ?= null,

        /**
         * 随机字符，默认值为“success”
         */
        var tempStr:String = "success",

        /**
         * MD5加密
         */
        var md5Str:String? = null,

        var storeLatitude:Float? = null,

        var storeLongitude:Float? = null,

        var carrierName:String?=null,

        var carrierPhone:String?=null,

        var carrierPin:String?=null

) {
}