package com.example.cx.unlietaskdemo.base;


import com.leinyo.httpclient.retrofit.HttpClient;
import com.leinyo.httpclient.retrofit.NetworkResponse;

/**
 * Created by hly on 2017/2/13.
 * email hly910206@gmail.com
 */

public abstract class BasePresenter implements IBasePresenter {
    protected String mTag;
    protected Boolean isCancelTag = false;

    @Override
    public void setTag(String tag) {
        mTag = tag;
    }

    @Override
    public void destroy() {
        isCancelTag = true;
        HttpClient.getInstance().cancelTagCall(mTag);
    }
}
