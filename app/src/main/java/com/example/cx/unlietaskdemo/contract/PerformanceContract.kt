package com.xstore.tms.android.contract

import com.xstore.tms.android.base.IBaseView
import com.xstore.tms.android.ui.performance.PerformanceActivity

/**
 * Created by wangwenming1 on 2018/4/4.
 */
interface PerformanceContract {
    interface IPresenter {
        /**
         * 载入数据
         */
        fun loadData(navigation: PerformanceActivity.Navigation)
        /**
         * 刷新数据
         */
        fun refresh(navigation: PerformanceActivity.Navigation)
        /**
         * 载入更多
         */
        fun loadMore(navigation: PerformanceActivity.Navigation)
    }
    interface IView{
        fun onDataReady(data: List<ItemData>, firstPage: Boolean)
    }

    /**
     * 数据项定义
     */
    data class ItemData(
            val number: String,
            val numberLabel: String,
            val time: String,
            val timeLabel: String
    )
}