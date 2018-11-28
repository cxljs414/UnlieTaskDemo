package com.example.cx.unlietaskdemo.presenter;

import com.alibaba.fastjson.JSON;
import com.leinyo.httpclient.retrofit.NetworkStringResponse;
import com.xstore.tms.android.base.BasePresenter;
import com.xstore.tms.android.contract.CourierPerformanceContract;
import com.xstore.tms.android.entity.net.performance.RequestCompleteDoAndRo;
import com.xstore.tms.android.entity.net.performance.RequestCompleteDoAndRoCount;
import com.xstore.tms.android.entity.net.performance.ResponseCompleteDo;
import com.xstore.tms.android.entity.net.performance.ResponseCompleteDoAndRoCount;
import com.xstore.tms.android.entity.net.performance.ResponseCompleteRo;
import com.xstore.tms.android.repository.CourierPerformanceRepository;
import com.xstore.tms.android.utils.SysUtil;
import com.xstore.tms.android.utils.ToastUtils;
import com.xstore.tms.android.entity.net.Msg;

import org.apache.commons.lang3.StringUtils;

import static com.xstore.tms.android.contract.CourierPerformanceContract.C_1_1;
import static com.xstore.tms.android.contract.CourierPerformanceContract.C_1_2;
import static com.xstore.tms.android.contract.CourierPerformanceContract.C_2_1;
import static com.xstore.tms.android.contract.CourierPerformanceContract.C_2_2;
import static com.xstore.tms.android.contract.CourierPerformanceContract.C_3_1;
import static com.xstore.tms.android.contract.CourierPerformanceContract.C_3_2;
import static com.xstore.tms.android.contract.CourierPerformanceContract.lastMCount;
import static com.xstore.tms.android.contract.CourierPerformanceContract.thisMCount;
import static com.xstore.tms.android.contract.CourierPerformanceContract.todayCount;

/**
 * Created by wangwenming1 on 2018/3/16.
 */
@Deprecated
public class CourierPerformancePresenter extends BasePresenter implements CourierPerformanceContract.IPresenter, NetworkStringResponse {

    private CourierPerformanceRepository repository = new CourierPerformanceRepository();

    private CourierPerformanceContract.IView iView = null;

    public CourierPerformancePresenter(CourierPerformanceContract.IView iView) {
        this.iView = iView;
    }

    @Override
    public void loadToday() {
        repository.getCompleteDoAndRoCount(todayCount, mTag, new RequestCompleteDoAndRoCount("1"), this);

        RequestCompleteDoAndRo doQuery = new RequestCompleteDoAndRo();
        doQuery.setPage(1);
        if (SysUtil.INSTANCE.isDebug()) {
            doQuery.setDateType("0");
        } else {
            doQuery.setDateType("today");
        }
        repository.getCompleteDoList(C_1_1, mTag, doQuery, this);
        repository.getCompleteRoList(C_1_2, mTag, doQuery, this);
    }

    @Override
    public void loadLastM() {
        repository.getCompleteDoAndRoCount(lastMCount, mTag, new RequestCompleteDoAndRoCount("lastMonth"), this);

        RequestCompleteDoAndRo doQuery = new RequestCompleteDoAndRo();
        doQuery.setPage(1);
        doQuery.setDateType("lastMonth");
        repository.getCompleteDoList(C_2_1, mTag, doQuery, this);
        repository.getCompleteRoList(C_2_2, mTag, doQuery, this);
    }

    @Override
    public void loadThisM() {
        repository.getCompleteDoAndRoCount(thisMCount, mTag, new RequestCompleteDoAndRoCount("thisMonth"), this);

        RequestCompleteDoAndRo doQuery = new RequestCompleteDoAndRo();
        doQuery.setPage(1);
        doQuery.setDateType("thisMonth");
        repository.getCompleteDoList(C_3_1, mTag, doQuery, this);
        repository.getCompleteRoList(C_3_2, mTag, doQuery, this);
    }

    @Override
    public void onDataError(int requestCode, int responseCode, String message) {
        ToastUtils.showToast(message);
    }

    @Override
    public void onDataReady(String response, int requestCode) {
        if (StringUtils.isBlank(response)) {
            ToastUtils.showToast("请求结果为空， - " + requestCode);
            return;
        }

        Msg msg = JSON.parseObject(response, Msg.class);
        // - 登录请求结果
        if (!Msg.SUCCESS.equals(msg.getFlag())) {
            ToastUtils.showToast("登录请求发生异常：- " + msg.getFlag());
            return;
        }
        if (StringUtils.isBlank(msg.getContent())) {
            ToastUtils.showToast("请求内容为空， - " + requestCode);
            return;
        }

        switch (requestCode) {
            case todayCount:
            case lastMCount:
            case thisMCount:
                ResponseCompleteDoAndRoCount countResponse = msg.getContentEntity(ResponseCompleteDoAndRoCount.class);
                iView.countOnReady(requestCode, countResponse);
                break;
            case C_1_1:
            case C_2_1:
            case C_3_1:
                ResponseCompleteDo doResponse = msg.getContentEntity(ResponseCompleteDo.class);
                iView.dataOnReady(requestCode, doResponse, null);
                break;
            case C_1_2:
            case C_2_2:
            case C_3_2:
                ResponseCompleteRo roResponse = msg.getContentEntity(ResponseCompleteRo.class);
                iView.dataOnReady(requestCode, null, roResponse);
                break;
            default:
                break;
        }
    }
}
