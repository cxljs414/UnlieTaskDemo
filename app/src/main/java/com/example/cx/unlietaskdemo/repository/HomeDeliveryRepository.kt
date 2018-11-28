package com.xstore.tms.android.repository

import com.leinyo.httpclient.exception.HttpRequestEnum
import com.leinyo.httpclient.retrofit.HttpClient
import com.leinyo.httpclient.retrofit.HttpRequest
import com.leinyo.httpclient.retrofit.NetworkResponse
import com.xstore.tms.android.core.net.HttpConstants
import com.xstore.tms.android.core.net.InterceptorResponse
import com.xstore.tms.android.core.net.URLHandler
import com.xstore.tms.android.entity.LoginedCarrier
import com.xstore.tms.android.entity.delivery.ExceptionDeliveryRoot
import com.xstore.tms.android.entity.home.RequestHomeDeliveryHandle
import com.xstore.tms.android.entity.home.RequestHomeDeliveryList
import com.xstore.tms.android.utils.CryptoUtil
import java.text.MessageFormat

/**
 * 宅配相关接口
 * Created by wangwenming1 on 2018/3/30.
 */
class HomeDeliveryRepository {

    /**
     * 获取宅配列表
     */
    fun getHomeDeliveryList(tag: String, query: RequestHomeDeliveryList, networkResponse: NetworkResponse) {
        val httpRequest = HttpRequest.HttpRequestBuilder(URLHandler.HOME_DELIVERY_LIST,
                HttpConstants.HOME_DELIVERY_LIST, tag)
                .requestWay(HttpRequestEnum.POST)
                .jsonObject(query)
                .create()
        HttpClient.getInstance().asyncNetWork<Any>(httpRequest, InterceptorResponse().interceptor(networkResponse))
    }

    /**
     * 异常妥投提交
     */
    fun commitExceptionDelivery(tag: String, query: ExceptionDeliveryRoot, networkResponse: NetworkResponse) {
        val time =  System.currentTimeMillis().toString()
        val tempStr = LoginedCarrier.sfTempStr
        val md5Str = CryptoUtil.generate(tempStr, time)
        query.tempStr = tempStr
        query.md5Str = md5Str
        val httpRequest = HttpRequest.HttpRequestBuilder(MessageFormat.format(URLHandler.HOME_EXCEPTION_DELIVERY, time),
                HttpConstants.HOME_EXCEPTION_DELIVERY, tag)
                .requestWay(HttpRequestEnum.POST)
                .jsonObject(query)
                .create()
        HttpClient.getInstance().asyncNetWork<Any>(httpRequest, InterceptorResponse().interceptor(networkResponse))
    }

    /**
     * 获取宅配订单详情
     * @param homeNo 宅配运单号
     */
    fun getHomeDeliveryDetial(tag: String, homeNo: String, networkResponse: NetworkResponse) {
        val httpRequest = HttpRequest.HttpRequestBuilder(
                MessageFormat.format(URLHandler.HOME_DELIVERY_DETAIL, homeNo), HttpConstants.HOME_DELIVERY_DETAIL, tag)
                .requestWay(HttpRequestEnum.GET)
                .create()
        //WebRequestWrap.asyncNetWork(httpRequest, networkResponse)
        HttpClient.getInstance().asyncNetWork<Any>(httpRequest, InterceptorResponse().interceptor(networkResponse))
    }

    /**
     * 确认、取消
     *
     * 注： 成功或失败，直接输出字符串信息。
     *
     * @param homeNo 宅配运单号
     */
    fun sendBackDeliveryList(tag: String, request: RequestHomeDeliveryHandle, networkResponse: NetworkResponse) {
        val httpRequest = HttpRequest.HttpRequestBuilder(URLHandler.HOME_DELIVERY_HANDLE, HttpConstants.HOME_DELIVERY_HANDLE, tag)
                .requestWay(HttpRequestEnum.POST)
                .jsonObject(request)
                .create()
        //WebRequestWrap.asyncNetWork(httpRequest, networkResponse)
        HttpClient.getInstance().asyncNetWork<Any>(httpRequest, InterceptorResponse().interceptor(networkResponse))
    }


    /**
     * 确认、取消
     *
     * 注： 成功或失败，直接输出字符串信息。
     *
     * @param homeNo 宅配运单号
     */
    fun sendBackDeliveryListSafe(tag: String, request: RequestHomeDeliveryHandle, networkResponse: NetworkResponse) {
        val time =  System.currentTimeMillis().toString()
        val tempStr = LoginedCarrier.sfTempStr
        val md5Str = CryptoUtil.generate(tempStr, time)
        request.tempStr = tempStr
        request.md5Str = md5Str
        val uri = MessageFormat.format(URLHandler.HOME_DELIVERY_HANDLE_SAFE, time)
        val httpRequest = HttpRequest.HttpRequestBuilder(uri, HttpConstants.HOME_DELIVERY_HANDLE, tag)
                .requestWay(HttpRequestEnum.POST)
                .jsonObject(request)
                .create()
        //WebRequestWrap.asyncNetWork(httpRequest, networkResponse)
        HttpClient.getInstance().asyncNetWork<Any>(httpRequest, InterceptorResponse().interceptor(networkResponse))
    }
}