package com.example.cx.unlietaskdemo.entity.delivery;

import java.util.ArrayList;

public class AbnormalCompleteSkus {

    private String skuId;
    private String skuUuid;
    private ArrayList<ExceptionReason> abnormalCompleteDetails;

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public ArrayList<ExceptionReason> getAbnormalCompleteDetails() {
        return abnormalCompleteDetails;
    }

    public void setAbnormalCompleteDetails(ArrayList<ExceptionReason> abnormalCompleteDetails) {
        this.abnormalCompleteDetails = abnormalCompleteDetails;
    }

    public String getSkuUuid() {
        return skuUuid;
    }

    public void setSkuUuid(String skuUuid) {
        this.skuUuid = skuUuid;
    }
}
