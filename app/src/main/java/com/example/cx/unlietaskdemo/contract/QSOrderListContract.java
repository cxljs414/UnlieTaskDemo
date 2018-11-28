package com.example.cx.unlietaskdemo.contract;

import com.xstore.tms.android.entity.net.DeliveryOrderVO;

import java.util.List;

/**
 * Created by wangwenming1 on 2018/3/8.
 */
public interface QSOrderListContract {

    interface IView {
        /**
         * 装载视图数据
         * @param data
         */
        void orderListOnLoad(List<DeliveryOrderVO> data);
    }

    interface IPresenter {
        void loadData();
    }
}
