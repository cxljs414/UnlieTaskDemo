package com.xstore.tms.android.contract

import com.amap.api.services.core.LatLonPoint
import com.xstore.tms.android.base.IBaseView
import com.xstore.tms.android.entity.delivery.*

/**
 * Created by hly on 2018/3/26.
 * email hly910206@gmail.com
 */
interface DeliveryDetailContract {

    interface IPresenter {
        fun orderConfirm(entity: RequestDeliveryComplete,storePoint: LatLonPoint)

        fun orderReject(entity: RequestDeliveryReject, storePoint: LatLonPoint)

        fun orderExceptionDelivery(entity: RequestDeliveryException)

        /**
         * 解密客户电话号码
         * @param roNo
         */
        fun decryptMobileNumber(doNo: String)

        /**
         * 获取运单详情
         */
        fun getDeliveryOrderDetail(stringExtra: String?)

        /**
         * 执行离线任务 妥投
         */
        fun orderConfirmUnlineTask()

        /**
         * 有离线失败任务时 执行离线拒收
         */
        fun orderRejectUnlineTask(reason:String)

        fun loadUnlineTask(doNo: String)
    }

    interface IView : IBaseView{
        fun orderConfirm(result: Boolean){}

        fun orderReject(result: Boolean){}

        fun orderExceptionDelivery(entity: ResponseExceptionDelivery){}


        /**
         * 返回电话号码
         * @param phoneNo
         */
        fun backMobileNumber(phoneNo: String){}

        /**
         * 更新运单信息
         */
        fun updateView(buildDetailResult: DeliveryDetailEntity){}

        fun updateError(){}
        fun showUnlineFailureTip(type: String?){}
        fun startUnlineTask(delayEntity: DelayOrderEntity){}
    }


}