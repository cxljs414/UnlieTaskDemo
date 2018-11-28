package com.xstore.tms.android.repository

import com.leinyo.httpclient.exception.HttpRequestEnum
import com.leinyo.httpclient.retrofit.HttpClient
import com.leinyo.httpclient.retrofit.HttpRequest
import com.leinyo.httpclient.retrofit.NetworkResponse
import com.xstore.tms.android.core.WebRequestWrap
import com.xstore.tms.android.core.net.HttpConstants
import com.xstore.tms.android.core.net.InterceptorResponse
import com.xstore.tms.android.core.net.URLHandler
import com.xstore.tms.android.entity.LoginedCarrier
import com.xstore.tms.android.entity.common.RequestDecryptSearch
import com.xstore.tms.android.utils.CryptoUtil
import java.text.MessageFormat

/**
 * 手机号解密
 * Created by wangwenming1 on 2018/3/18.
 */
class DecryptMobileRepository {

    /**
     * 手机号解密
     * 返回结果：com.xstore.tms.android.entity.common.ResponseDecryptMobile
     */
    fun decryptMobileNumber(tag: String, query: RequestDecryptSearch, networkResponse: NetworkResponse) {
        val httpRequest = HttpRequest.HttpRequestBuilder(URLHandler.DECRYPT_MOBILE_NUMBER, HttpConstants.DECRYPT_MOBILE_NUMBER, tag)
                .requestWay(HttpRequestEnum.POST)
                .jsonObject(query)
                .create()
        //WebRequestWrap.asyncNetWork(httpRequest, networkResponse)
        HttpClient.getInstance().asyncNetWork<Any>(httpRequest, InterceptorResponse().interceptor(networkResponse))

    }

    /**
     * 手机号解密
     * 返回结果：com.xstore.tms.android.entity.common.ResponseDecryptMobile
     */
    fun decryptMobileNumberSafe(tag: String, query: RequestDecryptSearch, networkResponse: NetworkResponse) {
        val time =  System.currentTimeMillis().toString()
        val tempStr = LoginedCarrier.sfTempStr
        val md5Str = CryptoUtil.generate(tempStr, time)
        query.tempStr = tempStr
        query.md5Str = md5Str
        val httpRequest = HttpRequest.HttpRequestBuilder(
                MessageFormat.format(URLHandler.DECRYPT_MOBILE_NUMBER_SAFE, time), HttpConstants.DECRYPT_MOBILE_NUMBER, tag)
                .requestWay(HttpRequestEnum.POST)
                .jsonObject(query)
                .create()
        //WebRequestWrap.asyncNetWork(httpRequest, networkResponse)
        HttpClient.getInstance().asyncNetWork<Any>(httpRequest, InterceptorResponse().interceptor(networkResponse))
    }
}