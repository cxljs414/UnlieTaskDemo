package com.example.cx.unlietaskdemo.core.net;

import com.leinyo.httpclient.retrofit.NetworkBeanResponse;
import com.leinyo.httpclient.retrofit.NetworkResponse;
import com.leinyo.httpclient.retrofit.NetworkStringResponse;

/**
 * Created by hly on 2018/3/14.
 * email hly910206@gmail.com
 */

public final class InterceptorResponse {

    public NetworkResponse interceptor(NetworkResponse networkResponse) {
        if (networkResponse instanceof NetworkStringResponse) {
            return new InterceptorStringResponse(networkResponse);
        } else if (networkResponse instanceof NetworkBeanResponse) {
            return new InterceptorBeanResponse(networkResponse);
        }
        throw new IllegalStateException("error NetworkResponse type");
    }
}
