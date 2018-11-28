package com.example.cx.unlietaskdemo.entity.net;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by yangxueyuan on 2017/7/4.
 */
public class DeliveryOrderVO {

    /**
     * 集合单号
     */
    private String collectionId;

    /**
     * 发货单号/运单号/DO单号
     */
    private String doNo;

    /**
     * 处理状态（1：待处理 2：已接收 3：拒接收 4：妥投 5：拒收 6：强制妥投）
     */
    private Integer processStatus;

    /**
     * 客户名
     */
    private String customerName;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 手机号
     */
    private String customerMobile;

    /**
     * 客户手机 密文
     */
    private String secretCustomerMobile;

    /**
     * 配送日期
     */
    private Date shipDate;

    /**
     * 用户收货时间段(小时)
     */
    private String expectedArrivePeriodTime;

    /**
     * 用户最晚收货时间
     */
    private Date expectedArriveTime;

    /**
     * 发货数量
     */
    private BigDecimal shippedQty;

    /**
     * 用户下单时间
     */
    private Date orderCreateDate;

    /**
     * 订单备注
     * */
    private String orderRemark;

    /**
     * 商品信息列表
     * */
    private List<DeliveryOrderItemVO> dOrderItemList;

    /**
     * 妥投时间
     */
    private Date completeTime;

    /**
     * 拒收时间
     */
    private Date rejectTime;

    private BigDecimal lat;
    private BigDecimal lng;
    private Long storeId;

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getDoNo() {
        return doNo;
    }

    public void setDoNo(String doNo) {
        this.doNo = doNo;
    }

    public Integer getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getSecretCustomerMobile() {
        return secretCustomerMobile;
    }

    public void setSecretCustomerMobile(String secretCustomerMobile) {
        this.secretCustomerMobile = secretCustomerMobile;
    }

    public Date getShipDate() {
        return shipDate;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }

    public String getExpectedArrivePeriodTime() {
        return expectedArrivePeriodTime;
    }

    public void setExpectedArrivePeriodTime(String expectedArrivePeriodTime) {
        this.expectedArrivePeriodTime = expectedArrivePeriodTime;
    }

    public Date getExpectedArriveTime() {
        return expectedArriveTime;
    }

    public void setExpectedArriveTime(Date expectedArriveTime) {
        this.expectedArriveTime = expectedArriveTime;
    }

    public BigDecimal getShippedQty() {
        return shippedQty;
    }

    public void setShippedQty(BigDecimal shippedQty) {
        this.shippedQty = shippedQty;
    }

    public Date getOrderCreateDate() {
        return orderCreateDate;
    }

    public void setOrderCreateDate(Date orderCreateDate) {
        this.orderCreateDate = orderCreateDate;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public List<DeliveryOrderItemVO> getdOrderItemList() {
        return dOrderItemList;
    }

    public void setdOrderItemList(List<DeliveryOrderItemVO> dOrderItemList) {
        this.dOrderItemList = dOrderItemList;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public Date getRejectTime() {
        return rejectTime;
    }

    public void setRejectTime(Date rejectTime) {
        this.rejectTime = rejectTime;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
}
