package com.xstore.tms.android.entity.personal

/**
 *
 * 用户信息返回结果 <br></br>
 *
 * 请求实例：{"flag":"success","content":"{\"carrierName\":\"乔峥峥\",\"carrierPhone\":\"13245678965\",\"deptType\":\"SHOP\",\"storeId\":131219,\"storeName\":\"大族\",\"success\":true}"}
 *
 * Created by wangwenming1 on 2018/3/11.
 */

class ResponseUserInfoEntity {
    /**
     * 查询结果（成功:true  失败:false）
     */
    var isSuccess: Boolean = false

    /**
     * 查询失败原因
     */
    var errorMsg: String? = null

    /**
     * 登录人部门类型(HEAD：总部；SHOP：门店)
     */
    var deptType: String? = null

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
}
