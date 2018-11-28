package com.xstore.tms.android.contract

import com.xstore.tms.android.base.IBaseView
import com.xstore.tms.android.entity.net.performance.ResponseCompleteDoAndRoCount

interface PerformanceTvContract {
    interface IProcenter {
        /**
         * 载入已完成的数量统计
         */
        fun loadCompleteCount(dateType: String?)
    }
    interface IView{
        /**
         * 统计完成
         */
        fun completeReady(data: ResponseCompleteDoAndRoCount)
    }
}