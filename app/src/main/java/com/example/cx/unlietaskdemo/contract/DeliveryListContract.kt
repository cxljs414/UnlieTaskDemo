package com.xstore.tms.android.contract

import com.xstore.tms.android.base.IBaseView
import com.xstore.tms.android.entity.delivery.DeliveryDetailEntity
import com.xstore.tms.android.entity.delivery.DeliveryListEntity
import com.xstore.tms.android.entity.delivery.RequestOutbound

/**
 * Created by hly on 2018/3/16.
 * email hly910206@gmail.com
 */

interface DeliveryListContract {

    interface IPresenter {
        fun getDeliveryList()

        fun getOrderListByCollectionId(collectionId:String)

        fun callOutbound(outbound: RequestOutbound)

        fun removeItemByDoNo(needRemoveItemDoNo: String,needCheckUnlineTask:Boolean)

        fun removeItem(neddRemoveItemPosition: Int,needCheckUnlineTask:Boolean)
        fun updateLeaveTime()
    }

    interface IView : IBaseView{
        fun getDeliveryList(list: List<DeliveryListEntity>?)

        fun outboundBack(result: Map<String, Int>)

        fun onLoadComplete()

        fun showNoNet()
        fun removeItem(position: Int)

    }
}
