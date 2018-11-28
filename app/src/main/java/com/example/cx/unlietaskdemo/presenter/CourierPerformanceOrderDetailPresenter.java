package com.example.cx.unlietaskdemo.presenter;

import com.alibaba.fastjson.JSON;
import com.leinyo.httpclient.retrofit.NetworkStringResponse;
import com.xstore.tms.android.base.BasePresenter;
import com.xstore.tms.android.contract.CourierPerformanceOrderDetailContract;
import com.xstore.tms.android.core.net.HttpConstants;
import com.xstore.tms.android.entity.home.ResponseHomeDeliveryDetail;
import com.xstore.tms.android.entity.net.Msg;
import com.xstore.tms.android.entity.delivery.ResponseDeliveryOrder;
import com.xstore.tms.android.entity.net.ResponseReturnOrder;
import com.xstore.tms.android.entity.net.performance.RequestDeliveryOrderDetail;
import com.xstore.tms.android.entity.net.performance.RequestReturnOrderDetail;
import com.xstore.tms.android.repository.CourierPerformanceRepository;
import com.xstore.tms.android.repository.HomeDeliveryRepository;
import com.xstore.tms.android.utils.ToastUtils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Created by wangwenming1 on 2018/3/19.
 */

public class CourierPerformanceOrderDetailPresenter extends BasePresenter
        implements CourierPerformanceOrderDetailContract.IPresenter, NetworkStringResponse {

    private CourierPerformanceRepository performanceRepository = new CourierPerformanceRepository();

    private HomeDeliveryRepository homeDeliveryRepository = new HomeDeliveryRepository();

    private CourierPerformanceOrderDetailContract.IView iView;

    public CourierPerformanceOrderDetailPresenter(CourierPerformanceOrderDetailContract.IView iView) {
        this.iView = iView;
    }

    @Override
    public void loadDeliveryOrderDetail(@NotNull String doNo) {
        iView.showLoading();
        performanceRepository.getDeliveryOrderDetail(
                HttpConstants.REQUEST_DELIVERY_ORDER_DETAIL, mTag, new RequestDeliveryOrderDetail(doNo), this);
    }

    @Override
    public void loadReturnOrderDetail(@NotNull String roNo) {
        iView.showLoading();
        performanceRepository.getReturnOrderDetail(
                HttpConstants.RETURN_ORDER_DETAIL, mTag, new RequestReturnOrderDetail(roNo), this);
    }

    public void loadHomeDeliveryOrderDetail(String homeNo) {
        iView.showLoading();
        homeDeliveryRepository.getHomeDeliveryDetial(mTag, homeNo, this);
    }

    @Override
    public void onDataReady(String response, int requestCode) {
        if(isCancelTag)return;
        iView.hideLoading();
        if (StringUtils.isBlank(response)) {
            ToastUtils.showToast("请求结果为空， - " + requestCode);
            return;
        }

        switch (requestCode) {
            case HttpConstants.REQUEST_DELIVERY_ORDER_DETAIL:
                ResponseDeliveryOrder deliveryOrder = JSON.parseObject(response, ResponseDeliveryOrder.class);
                iView.deliveryOrderDetailReady(deliveryOrder);
                break;
            case HttpConstants.RETURN_ORDER_DETAIL:
                ResponseReturnOrder returnOrder = JSON.parseObject(response, ResponseReturnOrder.class);
                iView.returnOrderDetailReady(returnOrder);
                break;
            case HttpConstants.HOME_DELIVERY_DETAIL:
                ResponseHomeDeliveryDetail responseHomeDeliveryDetail = JSON.parseObject(response, ResponseHomeDeliveryDetail.class);
                iView.homeDeliveryOrderDetailReady(responseHomeDeliveryDetail);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDataError(int requestCode, int responseCode, String message) {
        if(isCancelTag)return;
        iView.hideLoading();
        ToastUtils.showToast(message);
    }
}
