package com.example.cx.unlietaskdemo.repository;

import com.leinyo.httpclient.exception.HttpRequestEnum;
import com.leinyo.httpclient.retrofit.HttpClient;
import com.leinyo.httpclient.retrofit.HttpRequest;
import com.leinyo.httpclient.retrofit.NetworkResponse;
import com.xstore.tms.android.core.WebRequestWrap;
import com.xstore.tms.android.core.net.InterceptorResponse;
import com.xstore.tms.android.core.net.URLHandler;
import com.xstore.tms.android.entity.net.performance.RequestCompleteDoAndRo;
import com.xstore.tms.android.entity.net.performance.RequestCompleteDoAndRoCount;
import com.xstore.tms.android.entity.net.performance.RequestDeliveryOrderDetail;
import com.xstore.tms.android.entity.net.performance.RequestReturnOrderDetail;

/**
 * Created by wangwenming1 on 2018/3/18.
 */
public class CourierPerformanceRepository {

    /**
     * 获取已完成运单/取件单数量
     *
     * 返回结果： com.xstore.tms.android.entity.net.performance.ResponseCompleteDoAndRoCount
     */
    public void getCompleteDoAndRoCount(int requestNumber, String tag, RequestCompleteDoAndRoCount query, NetworkResponse networkResponse) {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder(URLHandler.COMPLETE_DO_AND_RO_COUNT, requestNumber, tag)
                .requestWay(HttpRequestEnum.POST)
                .jsonObject(query).create();
        //WebRequestWrap.INSTANCE.asyncNetWork(httpRequest, networkResponse);
        HttpClient.getInstance().asyncNetWork(httpRequest, new InterceptorResponse().interceptor(networkResponse));
    }

    /**
     * 已经完成的订单
     */
    public void getCompleteDoList(int requestNumber, String tag, RequestCompleteDoAndRo query, NetworkResponse networkResponse) {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder(URLHandler.COMPLATE_DO_LIST, requestNumber, tag)
                .requestWay(HttpRequestEnum.POST)
                .jsonObject(query)
                .create();
        //WebRequestWrap.INSTANCE.asyncNetWork(httpRequest, networkResponse);
        HttpClient.getInstance().asyncNetWork(httpRequest, new InterceptorResponse().interceptor(networkResponse));
    }

    /**
     * 获取已经完成取件列表
     * @param requestNumber
     * @param tag
     * @param query
     * @param networkResponse
     */
    public void getCompleteRoList(int requestNumber, String tag, RequestCompleteDoAndRo query, NetworkResponse networkResponse) {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder(URLHandler.COMPLATE_RO_LIST, requestNumber, tag)
                .requestWay(HttpRequestEnum.POST)
                .jsonObject(query)
                .create();
        //WebRequestWrap.INSTANCE.asyncNetWork(httpRequest, networkResponse);
        HttpClient.getInstance().asyncNetWork(httpRequest, new InterceptorResponse().interceptor(networkResponse));
    }

    /**
     * 获取拒收单列表
     * @param requestNumber
     * @param tag
     * @param query
     * @param networkResponse
     */
    public void getCompleteRejectList(int requestNumber, String tag, RequestCompleteDoAndRo query, NetworkResponse networkResponse) {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder(URLHandler.COMPLATE_REJECT_LIST, requestNumber, tag)
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
    public void getDeliveryOrderDetail(int requestNumber, String tag, RequestDeliveryOrderDetail query, NetworkResponse networkResponse) {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder(URLHandler.DELIVERY_ORDER_DETAIL, requestNumber, tag)
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
}