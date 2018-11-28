package com.example.cx.unlietaskdemo.entity.delivery;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by hly on 2018/3/26.
 * email hly910206@gmail.com
 */

public class DeliveryDetailEntity implements Parcelable {

    public String orderId;

    public String consignee;

    public String mobilePhone;

    public String address;

    public BigDecimal waybillNumber;

    public Date orderCreateDate;

    public List<DeliveryOrderInfo> infoList;

    public double[] target = new double[2];

    public double[] store = new double[2];

    public Date lastDate;

    public String collectionId;

    public Long doNo;

    public BigDecimal leaveTime;

    public String storeId;

    public BigDecimal customerDistance = new BigDecimal(-1000);

    public Integer transportPriority = 1;

    public Long receivedTime;


    public static class DeliveryOrderInfo implements Parcelable {

        public String name;
        public BigDecimal shippedQty;   //实际发货
        public BigDecimal expectedQty;//期望发货
        public String url;
        public Integer saleMode;
        public String unit;
        public Long skuId;
        public Integer oosFlag;

        public DeliveryOrderInfo() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeSerializable(this.shippedQty);
            dest.writeSerializable(this.expectedQty);
            dest.writeString(this.url);
            dest.writeValue(this.saleMode);
            dest.writeString(this.unit);
            dest.writeValue(this.skuId);
            dest.writeValue(this.oosFlag);
        }

        protected DeliveryOrderInfo(Parcel in) {
            this.name = in.readString();
            this.shippedQty = (BigDecimal) in.readSerializable();
            this.expectedQty = (BigDecimal) in.readSerializable();
            this.url = in.readString();
            this.saleMode = (Integer) in.readValue(Integer.class.getClassLoader());
            this.unit = in.readString();
            this.skuId = (Long) in.readValue(Long.class.getClassLoader());
            this.oosFlag = (Integer)in.readValue(Integer.class.getClassLoader());
        }

        public static final Creator<DeliveryOrderInfo> CREATOR = new Creator<DeliveryOrderInfo>() {
            @Override
            public DeliveryOrderInfo createFromParcel(Parcel source) {
                return new DeliveryOrderInfo(source);
            }

            @Override
            public DeliveryOrderInfo[] newArray(int size) {
                return new DeliveryOrderInfo[size];
            }
        };
    }


    public DeliveryDetailEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderId);
        dest.writeString(this.consignee);
        dest.writeString(this.mobilePhone);
        dest.writeString(this.address);
        dest.writeSerializable(this.waybillNumber);
        dest.writeLong(this.orderCreateDate != null ? this.orderCreateDate.getTime() : -1);
        dest.writeTypedList(this.infoList);
        dest.writeDoubleArray(this.target);
        dest.writeDoubleArray(this.store);
        dest.writeLong(this.lastDate != null ? this.lastDate.getTime() : -1);
        dest.writeString(this.collectionId);
        dest.writeValue(this.doNo);
        dest.writeSerializable(this.leaveTime);
        dest.writeString(this.storeId);
        dest.writeSerializable(this.customerDistance);
        dest.writeInt(this.transportPriority);
        dest.writeLong(this.receivedTime);
    }

    protected DeliveryDetailEntity(Parcel in) {
        this.orderId = in.readString();
        this.consignee = in.readString();
        this.mobilePhone = in.readString();
        this.address = in.readString();
        this.waybillNumber = (BigDecimal) in.readSerializable();
        long tmpOrderCreateDate = in.readLong();
        this.orderCreateDate = tmpOrderCreateDate == -1 ? null : new Date(tmpOrderCreateDate);
        this.infoList = in.createTypedArrayList(DeliveryOrderInfo.CREATOR);
        this.target = in.createDoubleArray();
        this.store = in.createDoubleArray();
        long tmpLastDate = in.readLong();
        this.lastDate = tmpLastDate == -1 ? null : new Date(tmpLastDate);
        this.collectionId = in.readString();
        this.doNo = (Long) in.readValue(Long.class.getClassLoader());
        this.leaveTime = (BigDecimal) in.readSerializable();
        this.storeId = in.readString();
        this.customerDistance = (BigDecimal) in.readSerializable();
        this.transportPriority = in.readInt();
        this.receivedTime = in.readLong();
    }

    public static final Creator<DeliveryDetailEntity> CREATOR = new Creator<DeliveryDetailEntity>() {
        @Override
        public DeliveryDetailEntity createFromParcel(Parcel source) {
            return new DeliveryDetailEntity(source);
        }

        @Override
        public DeliveryDetailEntity[] newArray(int size) {
            return new DeliveryDetailEntity[size];
        }
    };
}
