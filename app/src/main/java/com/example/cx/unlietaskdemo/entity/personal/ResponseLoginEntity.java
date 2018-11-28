package com.example.cx.unlietaskdemo.entity.personal;

/**
 * @author zhangchangzhi on 2018/3/6.
 */
public class ResponseLoginEntity {
    /**
     * 登录结果（成功:true  失败:false）
     */
    private boolean success;

    /**
     * 登录失败原因
     */
    private String errorMsg;

    /**
     * 登录证书
     */
    private String token;

    /**
     * 对称加密秘钥
     */
    private String tempStr;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getTempStr() {
        return tempStr;
    }

    public void setTempStr(String tempStr) {
        this.tempStr = tempStr;
    }
}
