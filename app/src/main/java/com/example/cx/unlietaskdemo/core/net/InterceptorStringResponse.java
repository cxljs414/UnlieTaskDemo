package com.example.cx.unlietaskdemo.core.net;

import com.alibaba.fastjson.JSON;
import com.leinyo.httpclient.retrofit.NetworkResponse;
import com.leinyo.httpclient.retrofit.NetworkStringResponse;
import com.xstore.tms.android.entity.net.Msg;
import com.xstore.tms.android.ui.LoginActivityKt;
import com.xstore.tms.android.utils.LogUtils;

/**
 * Created by hly on 2018/3/14.
 * email hly910206@gmail.com
 */

public class InterceptorStringResponse implements NetworkStringResponse {

    private NetworkResponse mNetworkResponse;

    public InterceptorStringResponse(NetworkResponse networkResponse) {
        mNetworkResponse = networkResponse;
    }

    @Override
    public void onDataReady(String response, int requestCode) {
        if (response != null) {
            try {
                Msg msg = JSON.parseObject(response, Msg.class);
                switch (msg.getFlag()) {
                    case Msg.SUCCESS:
                        String content = msg.getContent();
                        ((NetworkStringResponse) mNetworkResponse).onDataReady(content, requestCode);
                        break;
                    case Msg.BADPARAMS:
                        mNetworkResponse.onDataError(requestCode, 500, msg.getContent());
                        break;
                    case Msg.NEEDLOGIN:
                        LogUtils.i("Intercept","StringResponse needrelogin msg="+msg);
                        LoginActivityKt.Companion.startActivity();
                        onDataError(requestCode,333,"");
                        break;
                    default:
                        mNetworkResponse.onDataError(requestCode, 444, msg.getContent());
                        break;
                }
            }catch (Exception e){
                mNetworkResponse.onDataError(requestCode, 444, "数据解析异常");
            }
        }else{
            mNetworkResponse.onDataError(requestCode, 444, "数据解析异常");
        }
    }

    @Override
    public void onDataError(int requestCode, int responseCode, String message) {
        mNetworkResponse.onDataError(requestCode, responseCode, message);
    }
}
