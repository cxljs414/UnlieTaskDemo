package com.example.cx.unlietaskdemo.contract;

import com.xstore.tms.android.entity.net.performance.ResponseCompleteDo;
import com.xstore.tms.android.entity.net.performance.ResponseCompleteDoAndRoCount;
import com.xstore.tms.android.entity.net.performance.ResponseCompleteRo;

/**
 * 个人业绩
 * Created by wangwenming1 on 2018/3/16.
 */
@Deprecated
public interface CourierPerformanceContract {

    int todayCount = 4580010;
    int lastMCount = 4580020;
    int thisMCount = 4580030;

    int C_1_1 = 4580011;
    int C_1_2 = 4580012;
    int C_2_1 = 4580021;
    int C_2_2 = 4580022;
    int C_3_1 = 4580031;
    int C_3_2 = 4580032;

    interface IPresenter {
        /**
         *
         */
        void loadToday();
        void loadLastM();
        void loadThisM();
    }

    interface IView {
        void countOnReady(int reqNum, ResponseCompleteDoAndRoCount response);
        void dataOnReady(int reqNum, ResponseCompleteDo doResponse, ResponseCompleteRo roResponse);
    }
}
