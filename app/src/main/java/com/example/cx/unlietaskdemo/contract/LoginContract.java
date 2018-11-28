package com.example.cx.unlietaskdemo.contract;

import com.xstore.tms.android.base.IBaseView;

/**
 * @author zhangchangzhi on 2018/3/6.
 */

public interface LoginContract {
    interface IPresenter{
        void login(String userPing, String pwd);
    }
    interface  IView extends IBaseView{
        void login(boolean success, String response);

        void showFailCountTip(int i);

        void hideFailCountTip();
    }
}
