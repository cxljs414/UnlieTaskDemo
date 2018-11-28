package com.xstore.tms.android.repository

import com.leinyo.httpclient.exception.HttpRequestEnum
import com.leinyo.httpclient.retrofit.HttpClient
import com.leinyo.httpclient.retrofit.HttpRequest
import com.leinyo.httpclient.retrofit.NetworkResponse
import com.xstore.tms.android.core.net.HttpConstants
import com.xstore.tms.android.core.net.InterceptorResponse
import com.xstore.tms.android.core.net.URLHandler
import com.xstore.tms.android.entity.LoginedCarrier
import com.xstore.tms.android.entity.PushLocationEntity
import com.xstore.tms.android.utils.CryptoUtil
import java.text.MessageFormat


class PushLocationRepository {

    /**
     * 提交push定位
     */
    fun postPushLocation(tag: String, entity: PushLocationEntity, networkResponse: NetworkResponse) {
        val time = System.currentTimeMillis().toString()
        val tempStr = LoginedCarrier.sfTempStr
        val md5Str = CryptoUtil.generate(tempStr, time)
        entity.tempStr = tempStr
        entity.md5Str = md5Str
        val uri = MessageFormat.format(URLHandler.UPLOAD_LOCATION_PUSH, time)
        val httpRequest = HttpRequest.HttpRequestBuilder(uri, HttpConstants.UPLOAD_LOCATION_PUSH, tag)
                .requestWay(HttpRequestEnum.POST)
                .jsonObject(entity)
                .create()
        HttpClient.getInstance().asyncNetWork<Any>(httpRequest, InterceptorResponse().interceptor(networkResponse))
    }
}