package com.xstore.tms.android.entity.net.performance

import java.util.*

/**
 *
 * 配送完成订单，来自服务端的 com.xstore.tms.appserver.deliveryorder.domain.DeliveryOrderResult
 *
 * Created by wangwenming1 on 2018/3/19.
 */

class ResponseCompleteOrderItem : ResponseCompleteItem() {
    /**
     * 发货单号/运单号/DO单号
     */
    var doNo: String? = null
    /**
     * 处理状态（1：待处理 2：已接收 3：拒接收 4：妥投 5：拒收 6：强制妥投）
     */
    var processStatus: Int? = null
    /**
     * 妥投时间
     */
    var completeTime: Date? = null

    /**
     * 拒收时间
     */
    var rejectTime: Date? = null
}
