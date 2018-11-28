package com.example.cx.unlietaskdemo.core.net;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.leinyo.httpclient.retrofit.NetworkBeanResponse;
import com.leinyo.httpclient.retrofit.NetworkResponse;
import com.xstore.tms.android.entity.net.Msg;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by hly on 2018/3/14.
 * email hly910206@gmail.com
 */

public class InterceptorBeanResponse implements NetworkBeanResponse<Msg> {

    private NetworkResponse mNetworkResponse;

    public InterceptorBeanResponse(NetworkResponse networkResponse) {
        mNetworkResponse = networkResponse;
    }

    @Override
    public void onDataReady(Msg response, int requestCode) {
        if (response != null) {
            if (response.getFlag().equals(Msg.SUCCESS)) {
                String content = response.getContent();
                if (mNetworkResponse instanceof NetworkBeanResponse) {
                    Type[] types = mNetworkResponse.getClass().getGenericInterfaces();
                    for (Type type : types) {
                        if (type instanceof ParameterizedType) {
                            String packageName = ((ParameterizedType) type).getRawType().toString();
                            if (!TextUtils.isEmpty(packageName)) {
                                int end = packageName.lastIndexOf(".");
                                int start = packageName.indexOf(" ");
                                if (packageName.substring(start + 1, end).equals("com.leinyo.httpclient.retrofit")) {
                                    Class mClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
                                    Object result = JSON.parseObject(content, mClass);
                                    ((NetworkBeanResponse) mNetworkResponse).onDataReady(result, requestCode);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDataError(int requestCode, int responseCode, String message) {
        mNetworkResponse.onDataError(requestCode, responseCode, message);
    }
}
