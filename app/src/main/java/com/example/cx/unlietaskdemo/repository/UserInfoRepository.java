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
import com.xstore.tms.android.entity.UserInfoDecryptModel;
import com.xstore.tms.android.entity.personal.RequestLoginEntity;
import com.xstore.tms.android.utils.CryptoUtil;

import java.text.MessageFormat;
import java.util.HashMap;

/**
 * Created by wangwenming1 on 2018/3/8.
 */

public class UserInfoRepository {

    public void login(String tag, RequestLoginEntity loginEntity, NetworkResponse networkResponse) {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder(
                URLHandler.URL_LOGIN, HttpConstants.REQUEST_LOGIN, tag).requestWay(HttpRequestEnum.POST).
                jsonObject(loginEntity).create();
        //WebRequestWrap.INSTANCE.asyncNetWork(httpRequest, networkResponse);
        HttpClient.getInstance().asyncNetWork(httpRequest, new InterceptorResponse().interceptor(networkResponse));
    }

    /**
     * 20180615加密登入
     * @param tag
     * @param loginEntity
     * @param networkResponse
     */
    public void newLogin(String tag, RequestLoginEntity loginEntity, Long time, NetworkResponse networkResponse) {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder(
                MessageFormat.format(URLHandler.URL_NEW_LOGIN, String.valueOf(time)), HttpConstants.REQUEST_LOGIN, tag)
                .requestWay(HttpRequestEnum.POST).
                jsonObject(loginEntity).create();
        //WebRequestWrap.INSTANCE.asyncNetWork(httpRequest, networkResponse);
        HttpClient.getInstance().asyncNetWork(httpRequest, new InterceptorResponse().interceptor(networkResponse));
    }

    /**
     * 获取待配送列表接口
     */
    public void queryUserInfo(String tag, NetworkResponse networkResponse) {
        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder(URLHandler.REQUEST_GET_SKU_PRINT, HttpConstants.REQUEST_GET_USR_INFO, tag)
                .requestWay(HttpRequestEnum.POST).jsonObject(new HashMap<String,String>())
                .create();
        //WebRequestWrap.INSTANCE.asyncNetWork(httpRequest, networkResponse);
        HttpClient.getInstance().asyncNetWork(httpRequest, new InterceptorResponse().interceptor(networkResponse));
    }

    /**
     * 获取待配送列表接口
     * @param tag
     * @param networkResponse
     */
    public void newQueryUserInfo(String tag, NetworkResponse networkResponse) {
        String time =  String.valueOf(System.currentTimeMillis());
        String tempStr = LoginedCarrier.INSTANCE.getSfTempStr();
        String md5Str = CryptoUtil.generate(tempStr, time);
        UserInfoDecryptModel params = new UserInfoDecryptModel(tempStr, md5Str);
        String uri = MessageFormat.format(URLHandler.REQUEST_GET_SKU_PRINT_SAFE, time);

        HttpRequest httpRequest = new HttpRequest.HttpRequestBuilder(uri, HttpConstants.REQUEST_GET_USR_INFO, tag)
                .requestWay(HttpRequestEnum.POST).jsonObject(params)
                .create();
        //WebRequestWrap.INSTANCE.asyncNetWork(httpRequest, networkResponse);
        HttpClient.getInstance().asyncNetWork(httpRequest, new InterceptorResponse().interceptor(networkResponse));
    }
}
