package com.example.cx.unlietaskdemo.presenter;

import com.alibaba.fastjson.JSON;
import com.leinyo.httpclient.retrofit.NetworkStringResponse;
import com.xstore.tms.android.base.BasePresenter;
import com.xstore.tms.android.contract.ReturnOrderDetailContract;
import com.xstore.tms.android.core.Const;
import com.xstore.tms.android.core.net.HttpConstants;
import com.xstore.tms.android.entity.common.RequestDecryptSearch;
import com.xstore.tms.android.entity.common.ResponseDecryptMobile;
import com.xstore.tms.android.entity.net.ResponseReturnOrder;
import com.xstore.tms.android.entity.net.performance.RequestReturnOrderDetail;
import com.xstore.tms.android.entity.net.ro.RequestTerminateOrConfirmRo;
import com.xstore.tms.android.entity.net.ro.ResponseTerminateOrConfirmRo;
import com.xstore.tms.android.repository.DecryptMobileRepository;
import com.xstore.tms.android.repository.ReturnOrderRepository;
import com.xstore.tms.android.utils.LogUtils;
import com.xstore.tms.android.utils.MonitorUtil;
import com.xstore.tms.android.utils.ToastUtils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by wangwenming1 on 2018/3/22.
 */

public class ReturnOrderDetailPresenter extends BasePresenter implements ReturnOrderDetailContract.IPresenter, NetworkStringResponse {

    private ReturnOrderRepository returnOrderRepository = new ReturnOrderRepository();

    private DecryptMobileRepository decryptMobileRepository = new DecryptMobileRepository();

    private ReturnOrderDetailContract.IView iView;

    private String roNo="";

    public ReturnOrderDetailPresenter(ReturnOrderDetailContract.IView iView) {
        this.iView = iView;
    }

    @Override
    public void loadReturnOrderDetail(String roNo) {
        this.roNo = roNo;
        iView.showLoading();
        returnOrderRepository.getReturnOrderDetail(
                HttpConstants.RETURN_ORDER_DETAIL, mTag, new RequestReturnOrderDetail(roNo), this);
    }

    @Override
    public void confirmReturnOrder(RequestTerminateOrConfirmRo confirmRo) {
        iView.showLoading();
        returnOrderRepository.confirmReturnOrderSafe(mTag, confirmRo, this);
    }

    @Override
    public void terminateReturnOrder(RequestTerminateOrConfirmRo terminateRo) {
        iView.showLoading();
        returnOrderRepository.terminateReturnOrderSafe(mTag, terminateRo, this);
    }

    /**
     * 解密客户电话号码
     *
     * @param roNo
     */
    @Override
    public void decryptMobileNumber(String roNo) {
        iView.showLoading();
        decryptMobileRepository.decryptMobileNumberSafe(mTag, new RequestDecryptSearch(
                null, roNo, null ,null, null
        ), this);
    }

    @Override
    public void onDataReady(String response, int requestCode) {
        if(isCancelTag)return;
        iView.hideLoading();
        if (StringUtils.isBlank(response)) {
            ToastUtils.showToast("请求结果为空， - " + requestCode);
            return;
        }
        String msgTag = null;
        switch (requestCode) {
            case HttpConstants.RETURN_ORDER_DETAIL:
                ResponseReturnOrder returnOrder = JSON.parseObject(response, ResponseReturnOrder.class);
                iView.onDataReady(returnOrder);
                break;
            case HttpConstants.CONFIRM_RETURN_ORDER:
                msgTag = "确认取件：";
            case HttpConstants.TERMINATE_RETURN_ORDER:
                if (msgTag == null) {
                    msgTag = "终止取件：";
                }
                try {
                    ResponseTerminateOrConfirmRo terminateOrConfirmRo = JSON.parseObject(response, ResponseTerminateOrConfirmRo.class);
                    if (terminateOrConfirmRo.getSuccess()) {
                        ToastUtils.showToast(msgTag + "操作成功！");
                    } else {
                        ToastUtils.showToast(msgTag + "操作失败，" + terminateOrConfirmRo.getErrorMsg());
                    }
                } catch (Exception e){
                    LogUtils.e(msgTag + e.getMessage(), e);
                    ToastUtils.showToast(msgTag + " " + response);
                }
                iView.operateComeback();
                break;
            case HttpConstants.DECRYPT_MOBILE_NUMBER:
                ResponseDecryptMobile decryptMobile = JSON.parseObject(response, ResponseDecryptMobile.class);
                if (decryptMobile.getSuccess()) {
                    iView.backMobileNumber(decryptMobile.getMobile());
                } else {
                    ToastUtils.showToast(decryptMobile.getErrorMsg());
                }
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

        if(requestCode == HttpConstants.CONFIRM_RETURN_ORDER){
            try {
                MonitorUtil.INSTANCE.addMonitor(Const.FLAG_MONITOR_RECEIVE_COMPLETE,roNo,"");
            }catch (Exception e){}
        }
        if(requestCode == HttpConstants.TERMINATE_RETURN_ORDER){
            try {
                MonitorUtil.INSTANCE.addMonitor(Const.FLAG_MONITOR_RECEIVE_TERMINATED,roNo,"");
            }catch (Exception e){}
        }
    }
}
