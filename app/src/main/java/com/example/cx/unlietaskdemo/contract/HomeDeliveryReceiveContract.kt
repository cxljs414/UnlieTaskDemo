package com.xstore.tms.android.contract

import com.xstore.tms.android.base.IBaseView
import com.xstore.tms.android.entity.home.HomeDeliveryOrder
import com.xstore.tms.android.entity.home.ResponseHomeDeliveryDetail

/**
 * Created by hly on 2018/3/30.
 * email hly910206@gmail.com
 */
interface HomeDeliveryReceiveContract {


    interface IPresenter {
        fun getHomeReceiveList(isPull: Boolean)

        fun getHomeDeliveryList(isPull: Boolean)

        fun sendBack(isCancel: Boolean, list: List<HomeDeliveryOrder>)

        fun getHomeDeliveryDetail(homeNo: String)
    }

    interface IView {
        fun getHomeDeliveryList(list: List<HomeDeliveryOrder>?, hasMore: Boolean)

        fun getDeliveryDetail(entity: HomeDeliveryOrder)

        fun sendBack(isSuccess: Boolean)
    }
}