package com.example.cx.unlietaskdemo.entity.net.request;


import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by liuwei35 on 2017/6/20.
 * @description 运单查询实体
 */
public class DeliveryOrderVO {

    /**
     * 承运商类型（1:7fresh自营 2：第三方）
     */
    private Integer expressType;

    /**
     * 承运商ID
     */
    private Integer expressId;

    /**
     * 门店编号
     */
    private Long storeId;

    /**
     * 门店名称
     */
    private  String storeName;

    /**
     * 路区编号
     */
    private Long areaId;

    /**
     * 路区
     */
    private String areaName;

    /**
     * 集合单号
     */
    private String collectionId;

    /**
     * 配送员pin(自营)
     */
    private String carrierPin;

    /**
     * 配送员姓名
     */
    private String carrierName;

    /**
     * 配送员电话
     * */
    private String carrierPhone;

    /**
     * 主键
     */
    private Long id;

    /**
     * 订单号，一个订单可拆分为多个DO运单
     */
    private Long orderId;

    /**
     * 发货单ID(OPC)
     */
    private Long doId;

    /**
     * 发货单号/运单号/DO单号
     */
    private String doNo;

    /**
     * 处理状态（1：待处理 2：已接收 3：拒接收 4：妥投 5：拒收 6：强制妥投）
     */
    private Integer processStatus;

    /**
     * 配送方式 1 配送到家 2. 自提
     */
    private Integer deliveryType;

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
     * 配送日期
     */
    private Date shipDate;

    /**
     * 运单来源主体 1. 7fresh
     */
    private Integer deliveryOrderSource;

    /**
     * 订单来源主体 1. 7fresh 2. jd
     */
    private Integer sellerOrderSource;

    /**
     * 用户下单时间
     */
    private Date orderCreateDate;

    /**
     * 缺货策略： 0- 电话处理、1-先发有货商品（取货商品退款）、2-缺货取消订单
     */
    private Integer outOfStockStrategy;

    /**
     * 商家id  默认0,表示京东自营
     */
    private Long merchantId;

    /**
     * 付款类型
     */
    private Integer paymentType;

    /**
     * 期望送达日期
     */
    private Date expectedArriveDate;

    /**
     * 用户收货时间段
     */
    private String expectedArrivePeriodTime;

    /**
     * 扫描完成接收时间
     */
    private Date receivedTime;

    /**
     * 妥投时间
     */
    private Date completeTime;

    /**
     * 妥投失败原因
     */
    private String failReason;

    /**
     * 拒收时间
     */
    private Date rejectTime;

    /**
     * 状态（1：有效-1：无效）
     */
    private Integer status;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 修改时间
     */
    private Date modified;

    /**
     * 纬度
     */
    private BigDecimal lat;

    /**
     * 经度
     */
    private BigDecimal lng;

    /**
     * 状态集合
     * */
    private Integer[] processStatusArr;

    /**
     * 妥投true，拒收false
     * */
    private Boolean completed;

    /**
     * 开始时间
     * */
    private Date startDate;

    /**
     * 结束时间
     * */
    private Date endDate;

    /**
     * 页码
     * */
    private Integer page;

    /**
     * 查询时间维度（thisMonth:查询本月  lastMonth:查询上月  today:查询当天）
     * */
    private String dateType;


    public Integer getExpressType() {
        return expressType;
    }

    public void setExpressType(Integer expressType) {
        this.expressType = expressType;
    }

    public Integer getExpressId() {
        return expressId;
    }

    public void setExpressId(Integer expressId) {
        this.expressId = expressId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getCarrierPin() {
        return carrierPin;
    }

    public void setCarrierPin(String carrierPin) {
        this.carrierPin = carrierPin;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getCarrierPhone() {
        return carrierPhone;
    }

    public void setCarrierPhone(String carrierPhone) {
        this.carrierPhone = carrierPhone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getDoId() {
        return doId;
    }

    public void setDoId(Long doId) {
        this.doId = doId;
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

    public Integer getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
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

    public Date getShipDate() {
        return shipDate;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }

    public Integer getDeliveryOrderSource() {
        return deliveryOrderSource;
    }

    public void setDeliveryOrderSource(Integer deliveryOrderSource) {
        this.deliveryOrderSource = deliveryOrderSource;
    }

    public Integer getSellerOrderSource() {
        return sellerOrderSource;
    }

    public void setSellerOrderSource(Integer sellerOrderSource) {
        this.sellerOrderSource = sellerOrderSource;
    }

    public Date getOrderCreateDate() {
        return orderCreateDate;
    }

    public void setOrderCreateDate(Date orderCreateDate) {
        this.orderCreateDate = orderCreateDate;
    }

    public Integer getOutOfStockStrategy() {
        return outOfStockStrategy;
    }

    public void setOutOfStockStrategy(Integer outOfStockStrategy) {
        this.outOfStockStrategy = outOfStockStrategy;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Date getExpectedArriveDate() {
        return expectedArriveDate;
    }

    public void setExpectedArriveDate(Date expectedArriveDate) {
        this.expectedArriveDate = expectedArriveDate;
    }

    public String getExpectedArrivePeriodTime() {
        return expectedArrivePeriodTime;
    }

    public void setExpectedArrivePeriodTime(String expectedArrivePeriodTime) {
        this.expectedArrivePeriodTime = expectedArrivePeriodTime;
    }

    public Date getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Date receivedTime) {
        this.receivedTime = receivedTime;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public Date getRejectTime() {
        return rejectTime;
    }

    public void setRejectTime(Date rejectTime) {
        this.rejectTime = rejectTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
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

    public Integer[] getProcessStatusArr() {
        return processStatusArr;
    }

    public void setProcessStatusArr(Integer[] processStatusArr) {
        this.processStatusArr = processStatusArr;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }
}
