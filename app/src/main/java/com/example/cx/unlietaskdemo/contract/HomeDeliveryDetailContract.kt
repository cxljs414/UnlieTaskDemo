package com.xstore.tms.android.contract

import com.xstore.tms.android.base.IBaseView
import com.xstore.tms.android.entity.delivery.AbnormalCompleteSkus

/**
 * Created by hly on 2018/4/2.
 * email hly910206@gmail.com
 */
interface HomeDeliveryDetailContract {

    interface IView : IBaseView{
        fun sendBack(isSuccess: Boolean)

        fun backMobileNumber(mobileNumber: String)
    }

    interface IPresenter {
        fun confirm(homeNo: String, storeId: Long)

        fun reject(homeNo: String, reason: String, storeId: Long)

        fun decryptMobileNumber(homeNo: String)

    }
}