package com.xstore.tms.android.contract

import com.xstore.tms.android.base.IBaseView
import com.xstore.tms.android.entity.delivery.AbnormalCompleteSkus
import com.xstore.tms.android.entity.delivery.DeliveryUnusualEntity
import com.xstore.tms.android.entity.delivery.ResponseExceptionDelivery
import java.math.BigDecimal

/**
 * Created by hly on 2018/7/20.
 * email hly910206@gmail.com
 */
interface DeliveryUnusualContract {

    interface IPresenter {
//        fun buildUnusualEntity(entity: DeliveryDetailEntity): DeliveryUnusualEntity


        fun buildUnusualEntity(entity: ResponseExceptionDelivery): DeliveryUnusualEntity


        fun commitExceptionDelivery(homeNo: String, skuList: ArrayList<AbnormalCompleteSkus>, customerDistance: BigDecimal)
    }

    interface IView : IBaseView{

        fun handSuccess()
    }
}