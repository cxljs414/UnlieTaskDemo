package com.xstore.tms.android.entity

/**
 * @Description
 * @Author ${user}
 * @Date $time$ $date$
 **/
class MonitorInfo {

    /**
     *配送员pin
     */
    var carrierPin:String?=null

    /**
     * 所属门店编号
     */
    var storeId:Long?=null

    /**
     * 埋点类型  (1:妥投;2:异常妥投;3:拒收;4:配送员定位)
     */
    var flag:Int?=null

    /**
     * 运单号 flag=1,2,3必传
     */
    var doNo:String?=null

    /**
     * 发送定位消息版本号  flag=4 必传
     */
    var pushVersion:Long?=null

    /**
     * 手机设备信息
     */
    var deviceInfo:String ?=null

    /**
     * 埋点内容
     */
    var content:String?=null

    /**
     * 骑手APP生成埋点信息时间
     */
    var sendTime:Long?=null

    /**
     * 随机字符
     */
    var tempStr:String?="success"

    /**
     * MD5加密
     */
    var md5Str:String?=null
}