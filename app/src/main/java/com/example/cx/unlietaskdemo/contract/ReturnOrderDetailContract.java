package com.example.cx.unlietaskdemo.contract;

import com.xstore.tms.android.base.IBaseView;
import com.xstore.tms.android.entity.net.ResponseReturnOrder;
import com.xstore.tms.android.entity.net.ro.RequestTerminateOrConfirmRo;

/**
 * Created by wangwenming1 on 2018/3/22.
 */

public interface ReturnOrderDetailContract {

    interface IPresenter {
        /**
         * 载入取件单详情
         * @param roNo
         */
        void loadReturnOrderDetail(String roNo);

        /**
         *
         * @param confirmRo
         */
        void confirmReturnOrder(RequestTerminateOrConfirmRo confirmRo);

        /**
         *
         * @param terminateRo
         */
        void terminateReturnOrder(RequestTerminateOrConfirmRo terminateRo);

        /**
         * 解密客户电话号码
         * @param roNo
         */
        void decryptMobileNumber(String roNo);
    }

    interface IView extends IBaseView{
        /**
         * 返回取件单详情
         * @param returnOrder
         */
        void onDataReady(ResponseReturnOrder returnOrder);

        /**
         * 确认和取消返回
         */
        void operateComeback();

        /**
         * 返回电话号码
         * @param phoneNo
         */
        void backMobileNumber(String phoneNo);
    }
}
