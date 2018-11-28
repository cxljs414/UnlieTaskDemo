package com.example.cx.unlietaskdemo.entity.delivery;

public class ExceptionReason {

    private String abnormalNum;
    private String abnormalWeight;
    private int abnormalType;
    private boolean isChecked;



    public String getAbnormalNum() {
        return abnormalNum;
    }

    public void setAbnormalNum(String abnormalNum) {
        this.abnormalNum = abnormalNum;
    }

    public String getAbnormalWeight() {
        return abnormalWeight;
    }

    public void setAbnormalWeight(String abnormalWeight) {
        this.abnormalWeight = abnormalWeight;
    }

    public int getAbnormalType() {
        return abnormalType;
    }

    public void setAbnormalType(int abnormalType) {
        this.abnormalType = abnormalType;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}
