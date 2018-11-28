package com.example.cx.unlietaskdemo.entity.delivery;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ExceptionDeliveryRoot {

    private String doNo;
    private String md5Str;
    private String tempStr;
    private ArrayList<AbnormalCompleteSkus> abnormalCompleteSkus;

    private BigDecimal customerDistance;


    public BigDecimal getCustomerDistance() {
        return customerDistance;
    }

    public void setCustomerDistance(BigDecimal customerDistance) {
        this.customerDistance = customerDistance;
    }

    public String getDoNo() {
        return doNo;
    }

    public void setDoNo(String doNo) {
        this.doNo = doNo;
    }

    public String getMd5Str() {
        return md5Str;
    }

    public void setMd5Str(String md5Str) {
        this.md5Str = md5Str;
    }

    public String getTempStr() {
        return tempStr;
    }

    public void setTempStr(String tempStr) {
        this.tempStr = tempStr;
    }

    public ArrayList<AbnormalCompleteSkus> getAbnormalCompleteSkus() {
        return abnormalCompleteSkus;
    }

    public void setAbnormalCompleteSkus(ArrayList<AbnormalCompleteSkus> abnormalCompleteSkus) {
        this.abnormalCompleteSkus = abnormalCompleteSkus;
    }
}
