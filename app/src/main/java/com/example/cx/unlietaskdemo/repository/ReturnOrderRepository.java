package com.example.cx.unlietaskdemo.repository;

import com.leinyo.httpclient.exception.HttpRequestEnum;
import com.leinyo.httpclient.retrofit.HttpClient;
import com.leinyo.httpclient.retrofit.HttpRequest;
import com.leinyo.httpclient.retrofit.NetworkResponse;
import com.xstore.tms.android.core.WebRequestWrap;
import com.xstore.tms.android.core.net.HttpConstants;
import com.xstore.tms.android.core.net.InterceptorResponse;
import com.xstore.tms.android.core.net.URLHandler;
import com.xstore.tms.android.entity.LoginedCarrier;
import com.xstore.tms.android.entity.net.performance.RequestReturnOrderDetail;
import com.xstore.tms.android.entity.net.ro.RequestConfirmOrCancelRo;
import com.xstore.tms.android.entity.net.ro.RequestReturnOrderList;
import com.xstore.tms.android.entity.net.ro.RequestTerminateOrConfirmRo;
import com.xstore.tms.android.utils.CryptoUtil;

import java.text.MessageFormat;

/**
 * Created by wangwenming1 on 2018/3/20.
 */

public class ReturnOrderRepository {

    /**
     * 获取待配送列表接口
     */
    public void getReturnOrderList(Integer requestCode, String tag, RequestReturnOrderList query, NetworkResponse networkResponse) {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder(URLHandler.RETURN_ORDER_LIST, requestCode, tag)
                .requestWay(HttpRequestEnum.POST).jsonObject(query)
                .create();
        //WebRequestWrap.INSTANCE.asyncNetWork(httpRequest, networkResponse);
        HttpClient.getInstance().asyncNetWork(httpRequest, new InterceptorResponse().interceptor(networkResponse));
    }

    /**
     *  配送员取消接单
     * @param tag
     * @param query
     * @param networkResponse
     */
    public void carrierCancelReturnOrder(String tag, RequestConfirmOrCancelRo query, NetworkResponse networkResponse) {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder(URLHandler.CARRIER_CANCEL_RO, HttpConstants.CARRIER_CANCEL_RO, tag)
                .requestWay(HttpRequestEnum.POST)
                .jsonObject(query)
                .create();
        //WebRequestWrap.INSTANCE.asyncNetWork(httpRequest, networkResponse);
        HttpClient.getInstance().asyncNetWork(httpRequest, new InterceptorResponse().interceptor(networkResponse));
    }

    /**
     *  配送员确认接单
     * @param tag
     * @param query
     * @param networkResponse
     */
    public void carrierConfirmReturnOrder(String tag, RequestConfirmOrCancelRo query, NetworkResponse networkResponse) {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder(URLHandler.CARRIER_CONFIRM_RO, HttpConstants.CARRIER_CONFIRM_RO, tag)
                .requestWay(HttpRequestEnum.POST)
                .jsonObject(query)
                .create();
        //WebRequestWrap.INSTANCE.asyncNetWork(httpRequest, networkResponse);
        HttpClient.getInstance().asyncNetWork(httpRequest, new InterceptorResponse().interceptor(networkResponse));
    }

    /**
     * 获取运单明细
     * @param requestNumber
     * @param tag
     * @param query
     * @param networkResponse
     */
    public void getReturnOrderDetail(int requestNumber, String tag, RequestReturnOrderDetail query, NetworkResponse networkResponse) {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder(URLHandler.RETURN_ORDER_DETAIL, requestNumber, tag)
                .requestWay(HttpRequestEnum.POST)
                .jsonObject(query)
                .create();
        //WebRequestWrap.INSTANCE.asyncNetWork(httpRequest, networkResponse);
        HttpClient.getInstance().asyncNetWork(httpRequest, new InterceptorResponse().interceptor(networkResponse));
    }

    /**
     * 确认取件?
     * @param tag
     * @param query
     * @param networkResponse
     */
    public void confirmReturnOrder(String tag, RequestTerminateOrConfirmRo query, NetworkResponse networkResponse) {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder(URLHandler.CONFIRM_RETURN_ORDER, HttpConstants.CONFIRM_RETURN_ORDER, tag)
                .requestWay(HttpRequestEnum.POST)
                .jsonObject(query)
                .create();
        //WebRequestWrap.INSTANCE.asyncNetWork(httpRequest, networkResponse);
        HttpClient.getInstance().asyncNetWork(httpRequest, new InterceptorResponse().interceptor(networkResponse));
    }

    /**
     * 确认取件?
     * @param tag
     * @param query
     * @param networkResponse
     */
    public void confirmReturnOrderSafe(String tag, RequestTerminateOrConfirmRo query, NetworkResponse networkResponse) {
        String time =  String.valueOf(System.currentTimeMillis());
        String tempStr = LoginedCarrier.INSTANCE.getSfTempStr();
        String md5Str = CryptoUtil.generate(tempStr, time);
        query.setTempStr(tempStr);
        query.setMd5Str(md5Str);
        String uri = MessageFormat.format(URLHandler.CONFIRM_RETURN_ORDER_SAFE, time);
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder(uri, HttpConstants.CONFIRM_RETURN_ORDER, tag)
                .requestWay(HttpRequestEnum.POST)
                .jsonObject(query)
                .create();
        //WebRequestWrap.INSTANCE.asyncNetWork(httpRequest, networkResponse);
        HttpClient.getInstance().asyncNetWork(httpRequest, new InterceptorResponse().interceptor(networkResponse));

    }

    /**
     * 终止取件
     * @param tag
     * @param query
     * @param networkResponse
     */
    public void terminateReturnOrder(String tag, RequestTerminateOrConfirmRo query, NetworkResponse networkResponse) {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder(URLHandler.TERMINATE_RETURN_ORDER, HttpConstants.TERMINATE_RETURN_ORDER, tag)
                .requestWay(HttpRequestEnum.POST)
                .jsonObject(query)
                .create();
        //WebRequestWrap.INSTANCE.asyncNetWork(httpRequest, networkResponse);
        HttpClient.getInstance().asyncNetWork(httpRequest, new InterceptorResponse().interceptor(networkResponse));
    }

    /**
     * 终止取件
     * @param tag
     * @param query
     * @param networkResponse
     */
    public void terminateReturnOrderSafe(String tag, RequestTerminateOrConfirmRo query, NetworkResponse networkResponse) {
        String time =  String.valueOf(System.currentTimeMillis());
        String tempStr = LoginedCarrier.INSTANCE.getSfTempStr();
        String md5Str = CryptoUtil.generate(tempStr, time);
        query.setTempStr(tempStr);
        query.setMd5Str(md5Str);
        String uri = MessageFormat.format(URLHandler.TERMINATE_RETURN_ORDER_SAFE, time);
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder(uri, HttpConstants.TERMINATE_RETURN_ORDER, tag)
                .requestWay(HttpRequestEnum.POST)
                .jsonObject(query)
                .create();
        //WebRequestWrap.INSTANCE.asyncNetWork(httpRequest, networkResponse);
        HttpClient.getInstance().asyncNetWork(httpRequest, new InterceptorResponse().interceptor(networkResponse));
    }
}
