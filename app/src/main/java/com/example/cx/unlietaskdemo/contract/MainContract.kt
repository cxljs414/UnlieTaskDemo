package com.xstore.tms.android.contract

import com.xstore.tms.android.base.IBaseView
import com.xstore.tms.android.entity.main.ResponseDeliveryCount

/**
 * Created by wangwenming1 on 2018/3/27.
 */
interface MainContract {

    interface IPresenter {
        /**
         * 加载今日配送数量
         */
        fun loadDeliveryCount()
    }

    interface IView :IBaseView{
        /**
         * 今日配送数量加载完成
         */
        fun onDeliveryReady(deliveryCount: ResponseDeliveryCount)

        fun showNoNet()
    }
}