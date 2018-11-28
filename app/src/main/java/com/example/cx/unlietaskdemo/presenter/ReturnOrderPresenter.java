package com.example.cx.unlietaskdemo.presenter;

import com.alibaba.fastjson.JSON;
import com.leinyo.httpclient.retrofit.NetworkStringResponse;
import com.xstore.tms.android.base.BasePresenter;
import com.xstore.tms.android.contract.ReturnOrderContract;
import com.xstore.tms.android.core.net.HttpConstants;
import com.xstore.tms.android.entity.net.ResponseReturnOrder;
import com.xstore.tms.android.entity.net.ro.RequestConfirmOrCancelRo;
import com.xstore.tms.android.entity.net.ro.RequestReturnOrderList;
import com.xstore.tms.android.entity.net.ro.ResponseConfirmOrCancelRo;
import com.xstore.tms.android.entity.net.ro.ResponseReturnOrderListWrap;
import com.xstore.tms.android.repository.ReturnOrderRepository;
import com.xstore.tms.android.utils.ToastUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by wangwenming1 on 2018/3/20.
 */

public class ReturnOrderPresenter extends BasePresenter implements ReturnOrderContract.IPresenter, NetworkStringResponse {

    private ReturnOrderRepository returnOrderRepository = new ReturnOrderRepository();

    private ReturnOrderContract.IView iView;

    public ReturnOrderPresenter(ReturnOrderContract.IView iView) {
        this.iView = iView;
    }

    @Override
    public void loadUnreceivedReturnOrder() {
        returnOrderRepository.getReturnOrderList(HttpConstants.RETURN_ORDER_LIST_UNRECEIVED, mTag,
                new RequestReturnOrderList(1), this);
    }

    @Override
    public void loadWaitingReceiveReturnOrder() {
        returnOrderRepository.getReturnOrderList(HttpConstants.RETURN_ORDER_LIST_WAITING_RECEIVE, mTag,
                new RequestReturnOrderList(3), this);
    }

    @Override
    public void cancelTheOrder(List<ResponseReturnOrder> returnOrders) {
        RequestConfirmOrCancelRo request = new RequestConfirmOrCancelRo();
        for (ResponseReturnOrder ro : returnOrders) {
            request.getRoNoList().add(ro.getRoNo());
        }
        returnOrderRepository.carrierCancelReturnOrder(mTag, request, this);
    }

    @Override
    public void confirmTheOrder(List<ResponseReturnOrder> returnOrders) {
        RequestConfirmOrCancelRo request = new RequestConfirmOrCancelRo();
        for (ResponseReturnOrder ro : returnOrders) {
            request.getRoNoList().add(ro.getRoNo());
        }
        returnOrderRepository.carrierConfirmReturnOrder(mTag, request, this);
    }

    @Override
    public void onDataReady(String response, int requestCode) {
        if(isCancelTag)return;
        if (StringUtils.isBlank(response)) {
            ToastUtils.showToast("请求结果为空， - " + requestCode);
            return;
        }
        String message = null;
        switch (requestCode) {
            case HttpConstants.RETURN_ORDER_LIST_UNRECEIVED:
            case HttpConstants.RETURN_ORDER_LIST_WAITING_RECEIVE:
                iView.returnOrderListOnReady(JSON.parseObject(response, ResponseReturnOrderListWrap.class));
                break;
            case HttpConstants.CARRIER_CANCEL_RO:
                message = "配送员取消接单";
            case HttpConstants.CARRIER_CONFIRM_RO:
                if (message == null) {
                    message = "配送员确认接单";
                }
                ResponseConfirmOrCancelRo result = JSON.parseObject(response, ResponseConfirmOrCancelRo.class);
                if (result.getSuccess()) {
                    ToastUtils.showToast(message + "成功！");
                } else {
                    ToastUtils.showToast(message + "失败！- " + result.getErrorMsg());
                }
                iView.operateComplete();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDataError(int requestCode, int responseCode, String message) {
        if(isCancelTag)return;
        ToastUtils.showToast(message);
    }

    @Override
    public void cancelTag() {
        destroy();
    }
}
