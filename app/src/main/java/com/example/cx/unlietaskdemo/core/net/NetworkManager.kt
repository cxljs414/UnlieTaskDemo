package com.xstore.tms.android.core.net

import android.support.v4.app.FragmentActivity
import com.leinyo.httpclient.retrofit.HttpClient
import com.leinyo.httpclient.retrofit.HttpRequest
import com.leinyo.httpclient.retrofit.NetworkResponse
import com.xstore.tms.android.utils.ViewUtils

/**
 * Created by hly on 2018/3/26.
 * email hly910206@gmail.com
 */
object NetworkManager {
    fun open(activity: FragmentActivity, request: HttpRequest, response: NetworkResponse, message: String) {
        ViewUtils.showLoadingDialog(activity, message)
        HttpClient.getInstance().asyncNetWork<Any>(request, InterceptorResponse().interceptor(response))
    }


    fun open(request: HttpRequest, response: NetworkResponse) {
        HttpClient.getInstance().asyncNetWork<Any>(request, InterceptorResponse().interceptor(response))
    }

}