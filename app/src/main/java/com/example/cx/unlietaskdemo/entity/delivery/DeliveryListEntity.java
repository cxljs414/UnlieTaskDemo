package com.example.cx.unlietaskdemo.entity.delivery;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * Created by hly on 2018/3/19.
 * email hly910206@gmail.com
 */

public class DeliveryListEntity {
    private String doNo;
    private String consignee;
    private String mobilePhone;
    private String address;
    private Integer status;
    private Date lastDate;
    private BigDecimal leaveTime;
    private Integer transportPriority;
    private Integer sellerOrderSource;
    private Integer unlineTaskState = 0;//0 正常 1有正在执行的离线任务 2有失败的离线任务

    /**
     * 外呼时间
     */
    private Date callTime;

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public String getDoNo() {
        return doNo;
    }

    public void setDoNo(String doNo) {
        this.doNo = doNo;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCallTime() {
        return callTime;
    }

    public void setCallTime(Date callTime) {
        this.callTime = callTime;
    }

    public BigDecimal getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(BigDecimal leaveTime) {
        this.leaveTime = leaveTime;
    }

    public Integer getTransportPriority() {
        return transportPriority;
    }

    public void setTransportPriority(Integer transportPriority) {
        this.transportPriority = transportPriority;
    }

    public Integer getSellerOrderSource() {
        return sellerOrderSource;
    }

    public void setSellerOrderSource(Integer sellerOrderSource) {
        this.sellerOrderSource = sellerOrderSource;
    }

    public Integer getUnlineTaskState() {
        return unlineTaskState;
    }

    public void setUnlineTaskState(Integer unlineTaskState) {
        this.unlineTaskState = unlineTaskState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryListEntity that = (DeliveryListEntity) o;
        return Objects.equals(doNo, that.doNo) &&
                Objects.equals(consignee, that.consignee) &&
                Objects.equals(mobilePhone, that.mobilePhone) &&
                Objects.equals(address, that.address) &&
                Objects.equals(status, that.status) &&
                Objects.equals(lastDate, that.lastDate) &&
                Objects.equals(callTime, that.callTime) &&
                Objects.equals(transportPriority,that.transportPriority)&&
                Objects.equals(sellerOrderSource,that.sellerOrderSource);
    }

    @Override
    public int hashCode() {

        return Objects.hash(doNo, consignee, mobilePhone, address, status, lastDate, callTime,transportPriority,sellerOrderSource);
    }
}
