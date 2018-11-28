package com.xstore.tms.android.contract

import com.xstore.tms.android.base.IBaseView
import com.xstore.tms.android.entity.delivery.ResponseDeliveryOrder
import com.xstore.tms.android.entity.home.ResponseHomeDeliveryDetail
import com.xstore.tms.android.entity.net.ResponseReturnOrder

/**
 * Created by wangwenming1 on 2018/3/19.
 */

interface CourierPerformanceOrderDetailContract {

    interface IPresenter {
        /**
         * 载入订单详情
         * @param doNo
         */
        fun loadDeliveryOrderDetail(doNo: String)

        /**
         * 载入取件详情
         * @param roNo
         */
        fun loadReturnOrderDetail(roNo: String)

        /**
         * 载入宅配单
         */
        fun loadHomeDeliveryOrderDetail(homeNo: String)
    }

    interface IView{
        /**
         * 订单详情返回
         */
        fun deliveryOrderDetailReady(deliveryOrder: ResponseDeliveryOrder)

        /**
         * 取件详情返回
         */
        fun returnOrderDetailReady(returnOrder: ResponseReturnOrder)

        /**
         * 宅配详情返回
         */
        fun homeDeliveryOrderDetailReady(homeDeliveryDetail: ResponseHomeDeliveryDetail)

        fun showLoading()

        fun hideLoading()
    }
}
