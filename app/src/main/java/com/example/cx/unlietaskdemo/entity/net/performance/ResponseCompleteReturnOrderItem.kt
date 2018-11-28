package com.xstore.tms.android.entity.net.performance

import java.util.*

/**
 * 取件完成的订单列表（实体来自服务端的 com.xstore.tms.appserver.returnorder.domain.ReturnOrderResult）
 *
 * Created by wangwenming1 on 2018/3/19.
 */
class ResponseCompleteReturnOrderItem : ResponseCompleteItem() {

    /**
     * 取件单单号
     */
    var roNo: String? = null

    /**
     * 取件单完成时间
     */
    var completeTime: Date? = null
}
