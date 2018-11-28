package com.xstore.tms.android.entity.net

/**
 * 待配送列表
 * Created by wangwenming1 on 2018/3/19.
 */

data class RequestDeliveryOrderList(val processStatus: Int) {
    /**
     * 处理状态（1：待处理 2：已接收 3：拒接收 4：妥投 5：拒收 6：强制妥投）
     */
}
