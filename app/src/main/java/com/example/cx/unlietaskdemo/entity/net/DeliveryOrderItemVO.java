package com.example.cx.unlietaskdemo.entity.net;

import java.math.BigDecimal;
import java.util.Date;

public class DeliveryOrderItemVO {

    /**
     * 主键
     */
    private Long id;

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
    private String storeName;

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
     */
    private String carrierPhone;

    /**
     * 发货单号/运单号/DO单号
     */
    private String doNo;

    /**
     * skuID
     */
    private Long skuId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 期望发货数量
     */
    private BigDecimal expectedQty;

    /**
     * 单位
     */
    private String uom;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 促销商品
     */
    private short promoteFlag;

    /**
     * 批次编码
     */
    private String lotNo;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 商家ID
     */
    private Long merchantId;

    /**
     * 特殊商品标识，如“餐饮”
     */
    private Integer skuSpecFlag;

    /**
     * 客户特殊要求标识，如"鱼需宰杀"
     */
    private Integer customerSpecFlag;

    /**
     * 实发数
     */
    private BigDecimal shippedQty;

    /**
     * 赠品
     */
    private Integer giftFlag;

    /**
     * 实际出库重量
     */
    private BigDecimal shippedWt;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getDoNo() {
        return doNo;
    }

    public void setDoNo(String doNo) {
        this.doNo = doNo;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getExpectedQty() {
        return expectedQty;
    }

    public void setExpectedQty(BigDecimal expectedQty) {
        this.expectedQty = expectedQty;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public short getPromoteFlag() {
        return promoteFlag;
    }

    public void setPromoteFlag(short promoteFlag) {
        this.promoteFlag = promoteFlag;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getSkuSpecFlag() {
        return skuSpecFlag;
    }

    public void setSkuSpecFlag(Integer skuSpecFlag) {
        this.skuSpecFlag = skuSpecFlag;
    }

    public Integer getCustomerSpecFlag() {
        return customerSpecFlag;
    }

    public void setCustomerSpecFlag(Integer customerSpecFlag) {
        this.customerSpecFlag = customerSpecFlag;
    }

    public BigDecimal getShippedQty() {
        return shippedQty;
    }

    public void setShippedQty(BigDecimal shippedQty) {
        this.shippedQty = shippedQty;
    }

    public Integer getGiftFlag() {
        return giftFlag;
    }

    public void setGiftFlag(Integer giftFlag) {
        this.giftFlag = giftFlag;
    }

    public BigDecimal getShippedWt() {
        return shippedWt;
    }

    public void setShippedWt(BigDecimal shippedWt) {
        this.shippedWt = shippedWt;
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
}