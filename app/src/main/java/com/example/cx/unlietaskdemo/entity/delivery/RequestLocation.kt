package com.xstore.tms.android.entity.delivery

/**
 * 坐标上传参数
 */
data class RequestLocation(
        /**
         * 纬度
         */
        val latitude: Float? = null,
        /**
         * 经度
         */
        var longitude: Float? = null,
        /**
         * 地图坐标类型（1：GPS坐标值 2:高德地图坐标值 3:百度地图坐标值 4:mapbar）
         * @see
         * GPS(1, "gps", "GPS坐标值"),
         * AUTONAVI(2, "autonavi", "高德地图坐标值"),
         * BAIDU(3, "baidu", "百度地图坐标值"),
         * MAPBAR(4, "mapbar", "");
         */
        var mapType: Int? = null,
        /**
         * 运单号/DO单号
         */
        var doNo: String? = null,
        /**
         * 配送员实际走了多少米
         */
        var realDistance: String? = null
) {
    var status: Int = 1
}