package com.xstore.tms.android.repository

import com.leinyo.httpclient.exception.HttpRequestEnum
import com.leinyo.httpclient.retrofit.HttpClient
import com.leinyo.httpclient.retrofit.HttpRequest
import com.leinyo.httpclient.retrofit.NetworkResponse
import com.xstore.tms.android.core.WebRequestWrap
import com.xstore.tms.android.core.net.HttpConstants
import com.xstore.tms.android.core.net.InterceptorResponse
import com.xstore.tms.android.core.net.URLHandler
import java.util.*

/**
 * Created by wangwenming1 on 2018/3/27.
 */
class MainRepository {

    fun getDeliveryCount(tag: String, networkResponse: NetworkResponse) {
        val httpRequest = HttpRequest.HttpRequestBuilder(URLHandler.DELIVERY_COUNT, HttpConstants.DELIVERY_COUNT, tag)
                .requestWay(HttpRequestEnum.POST)
                .jsonObject(HashMap<String, Any>())
                .create()
        //WebRequestWrap.asyncNetWork(httpRequest, networkResponse)
        HttpClient.getInstance().asyncNetWork<Any>(httpRequest, InterceptorResponse().interceptor(networkResponse))
    }
}