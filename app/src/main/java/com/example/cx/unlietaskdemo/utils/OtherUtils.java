package com.example.cx.unlietaskdemo.utils;

import com.leinyo.httpclient.retrofit.HttpClientConfig;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class OtherUtils {

    public static HttpClientConfig setTimeOut(HttpClientConfig clientConfig){
        OkHttpClient okHttpClient = clientConfig.getOkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS).build();
        try {
            Field field = clientConfig.getClass().getDeclaredField("mOkHttpClient");
            field.setAccessible(true);
            field.set(clientConfig, okHttpClient);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return clientConfig;
    }
}
