package com.example.cx.unlietaskdemo.presenter;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.leinyo.httpclient.retrofit.NetworkStringResponse;
import com.xstore.tms.android.base.BasePresenter;
import com.xstore.tms.android.contract.LoginContract;
import com.xstore.tms.android.core.net.HttpConstants;
import com.xstore.tms.android.entity.LoginedCarrier;
import com.xstore.tms.android.entity.personal.RequestLoginEntity;
import com.xstore.tms.android.entity.personal.ResponseLoginEntity;
import com.xstore.tms.android.entity.personal.ResponseUserInfoEntity;
import com.xstore.tms.android.repository.UserInfoRepository;
import com.xstore.tms.android.utils.CryptoUtil;
import com.xstore.tms.android.utils.DateUtil;
import com.xstore.tms.android.utils.DeviceUtil;
import com.xstore.tms.android.utils.MD5Util;
import com.xstore.tms.android.utils.PreferencesUtils;
import com.xstore.tms.android.utils.TimeOutUtil;
import com.xstore.tms.android.utils.ToastUtils;
import com.xstore.tms.android.utils.ViewUtils;

/**
 * @author zhangchangzhi on 2018/3/6.
 */

public class LoginPresenter extends BasePresenter implements LoginContract.IPresenter, NetworkStringResponse {
    private UserInfoRepository mUserInfoRepository;
    private LoginContract.IView mIView;
    private String mUserPin, mPassword;
    private int failCount = 5;

    public LoginPresenter(LoginContract.IView iView) {
        mIView = iView;
    }

    @Override
    public void onDataReady(String response, int requestCode) {
        switch (requestCode) {
            case HttpConstants.REQUEST_LOGIN:
                ResponseLoginEntity loginEntity = JSON.parseObject(response, ResponseLoginEntity.class);
                if (!loginEntity.isSuccess()) {
                    mIView.hideLoading();
                    ToastUtils.showToast(loginEntity.getErrorMsg());
                    mIView.showFailCountTip(--failCount);
                } else if (loginEntity.isSuccess()) {
                    mIView.hideFailCountTip();
                    PreferencesUtils.remove(mUserPin);
                    TimeOutUtil.Companion.getInstance().clear();
                    LoginedCarrier.INSTANCE.initLoginInfo(null, mUserPin, loginEntity.getToken(), loginEntity.getTempStr());
                    getUserInfo();
                } else {
                    mIView.hideLoading();
                    ToastUtils.showToast("调用接口失败");
                }
                break;
            case HttpConstants.REQUEST_GET_USR_INFO:
                mIView.hideLoading();
                ResponseUserInfoEntity responseUserInfoEntity = JSON.parseObject(response, ResponseUserInfoEntity.class);
                if (responseUserInfoEntity != null) {
                    LoginedCarrier.INSTANCE.initLoginInfo(response, null, null, null);
                    PreferencesUtils.putStoreId(responseUserInfoEntity.getStoreId());
                    mIView.login(true,response);
                }
                break;
        }
    }

    @Override
    public void onDataError(int requestCode, int responseCode, String message) {
        mIView.hideLoading();
        ViewUtils.closeLoadingDialog();
        ToastUtils.showToast(message);
    }

    @Override
    public void login(String userPin, String pwd) {
        Log.i("---login---", MD5Util.md5(pwd));

        if(!loginFailureCheck(userPin)){
            return;
        }
        if (mUserInfoRepository == null) {
            mUserInfoRepository = new UserInfoRepository();
        }
        Long time = System.currentTimeMillis();
        mUserPin = userPin;
        mPassword = pwd;
        String tempStr = LoginedCarrier.INSTANCE.getSfTempStr();
        String md5Str = CryptoUtil.generate(tempStr, String.valueOf(time));
        mIView.showLoading();
        mUserInfoRepository.newLogin(mTag, new RequestLoginEntity(mUserPin, MD5Util.md5(mPassword), tempStr, md5Str, DeviceUtil.getDeviceBrand(), DeviceUtil.getSystemModel(), DeviceUtil.getAppVersionName((Context)mIView)), time, this);
    }

    /**
     * 检查此时登录账号是否已经超过限制登陆次数
     * @param userPin
     */
    private boolean loginFailureCheck(String userPin){
        failCount = 5;
        if(PreferencesUtils.contains(userPin)){
            String value = PreferencesUtils.getStr(userPin,"");
            if(!value.isEmpty()){
                String[] strs= value.split(":");
                String date= strs[0];
                int count= Integer.parseInt(strs[1]);
                if(date.equals(DateUtil.dateToString(DateUtil.getNowDate()))){
                    failCount = count;
                    if(count == 0){
                        mIView.showFailCountTip(count);
                        return false;
                    }

                }
            }
        }
        return true;
    }

    private void getUserInfo() {
        mUserInfoRepository.newQueryUserInfo(mTag, this);
    }
}
