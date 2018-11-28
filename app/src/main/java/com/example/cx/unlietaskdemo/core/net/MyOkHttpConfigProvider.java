package com.example.cx.unlietaskdemo.core.net;


import android.content.Context;
import android.text.TextUtils;

import com.leinyo.httpclient.okhttp.provider.impl.DefaultOkHttpProvider;

import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * Created by hly on 2017/11/2.
 * email hly910206@gmail.com
 */
public class MyOkHttpConfigProvider extends DefaultOkHttpProvider {

    @Override
    public List<Interceptor> createInterceptor(Context context) {
        List<Interceptor> list = super.createInterceptor(context);
        list.add(chain -> {
            Request originalRequest = chain.request();
            Request.Builder newBuilder = originalRequest.newBuilder();
            HttpUrl url = originalRequest.url();
            String originalUrl = url.encodedQuery();
            if (TextUtils.isEmpty(originalUrl)) {
                newBuilder.url(url.newBuilder().encodedPath(url.encodedPath()).toString());
            } else {
                newBuilder.url(url.newBuilder().encodedPath(url.encodedPath()).toString());
            }
            return chain.proceed(newBuilder.build());
        });
        list.add(new E_DInterceptor());
        return list;
    }

    @Override
    public Long createConnectTimeout() {
        return 3000L;
    }

    @Override
    public Long createWriteTimeout() {
        return 3000L;
    }

    @Override
    public Long createReadTimeout() {
        return 3000L;
    }
}

