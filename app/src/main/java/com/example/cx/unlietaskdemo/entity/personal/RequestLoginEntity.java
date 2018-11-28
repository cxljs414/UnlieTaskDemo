package com.example.cx.unlietaskdemo.entity.personal;

/**
 * @author zhangchangzhi on 2018/3/6.
 */

public class RequestLoginEntity {

    /**
     * 登录人pin
     */
    private String loginPin;


    /**
     * 登录人密码MD5
     */
    private String md5Pwd;

    /**
     * 随机字符
     */
    private String tempStr;

    /**
     * MD5加密
     */
    private String md5Str;

    /**
     * 手机厂商
     */
    private String deviceBrand;


    /**
     * 手机机型
     */
    private String model;


    /**
     * appVersion
     */
    private String appVersion;


    public RequestLoginEntity(String loginPin, String md5Pwd, String tempStr, String md5Str, String deviceBrand, String model, String appVersion) {
        this.loginPin = loginPin;
        this.md5Pwd = md5Pwd;
        this.tempStr = tempStr;
        this.md5Str = md5Str;
        this.deviceBrand = deviceBrand;
        this.model = model;
        this.appVersion = appVersion;
    }

    public void setLoginPin(String loginPin) {
        this.loginPin = loginPin;
    }

    public String getLoginPin() {
        return this.loginPin;
    }

    public void setMd5Pwd(String md5Pwd) {
        this.md5Pwd = md5Pwd;
    }

    public String getMd5Pwd() {
        return this.md5Pwd;
    }

    public String getTempStr() {
        return tempStr;
    }

    public void setTempStr(String tempStr) {
        this.tempStr = tempStr;
    }

    public String getMd5Str() {
        return md5Str;
    }

    public void setMd5Str(String md5Str) {
        this.md5Str = md5Str;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
