package com.xstore.tms.android.contract

import com.xstore.tms.android.base.IBaseView
import com.xstore.tms.android.entity.net.ResponseReturnOrder
import com.xstore.tms.android.entity.net.ro.ResponseReturnOrderListWrap

/**
 * Created by wangwenming1 on 2018/3/20.
 */

interface ReturnOrderContract {

    interface IPresenter {
        /**
         * 载入未接收的取件单
         */
        fun loadUnreceivedReturnOrder()

        /**
         * 载入等待接收的取件单
         */
        fun loadWaitingReceiveReturnOrder()

        /**
         * 拒绝接单
         */
        fun cancelTheOrder(returnOrders: List<ResponseReturnOrder>)

        /**
         * 确认接单
         * @param returnOrders
         */
        fun confirmTheOrder(returnOrders: List<ResponseReturnOrder>)

        fun cancelTag()
    }

    interface IView{

        fun returnOrderListOnReady(returnOrderListWrap: ResponseReturnOrderListWrap)

        /**
         * 拒绝或者确认接单，无论成功失败都回调本方法
         */
        fun operateComplete()

    }
}
