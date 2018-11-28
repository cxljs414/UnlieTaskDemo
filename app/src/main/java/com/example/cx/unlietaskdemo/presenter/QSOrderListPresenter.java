package com.example.cx.unlietaskdemo.presenter;

import com.leinyo.httpclient.retrofit.NetworkBeanResponse;
import com.xstore.tms.android.base.BasePresenter;
import com.xstore.tms.android.contract.QSOrderListContract;

/**
 * Created by wangwenming1 on 2018/3/8.
 */
public class QSOrderListPresenter extends BasePresenter implements QSOrderListContract.IPresenter, NetworkBeanResponse<Object> {

    private QSOrderListContract.IView view;

    public QSOrderListPresenter(QSOrderListContract.IView view){
        this.view = view;
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onDataReady(Object response, int requestCode) {

    }

    @Override
    public void onDataError(int requestCode, int responseCode, String message) {

    }
}
