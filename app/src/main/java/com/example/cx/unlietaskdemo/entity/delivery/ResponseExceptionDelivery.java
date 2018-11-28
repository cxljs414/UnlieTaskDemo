package com.example.cx.unlietaskdemo.entity.delivery;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.services.core.LatLonPoint;

import java.math.BigDecimal;
import java.util.List;

public class ResponseExceptionDelivery implements Parcelable {

    private String success;
    private String errorMsg;
    public Long doNo;
    public String orderId;
    public String storeId;
    private List<DeliveryUnusualDataListEntity> deliveryOrderItems;
    public BigDecimal customerDistance;
    private LatLonPoint storePoint;

    public LatLonPoint getStorePoint() {
        return storePoint;
    }

    public void setStorePoint(LatLonPoint storePoint) {
        this.storePoint = storePoint;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<DeliveryUnusualDataListEntity> getDeliveryOrderItems() {
        return deliveryOrderItems;
    }

    public void setDeliveryOrderItems(List<DeliveryUnusualDataListEntity> deliveryOrderItems) {
        this.deliveryOrderItems = deliveryOrderItems;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public ResponseExceptionDelivery() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.success);
        dest.writeString(this.errorMsg);
        dest.writeValue(this.doNo);
        dest.writeString(this.orderId);
        dest.writeString(this.storeId);
        dest.writeTypedList(this.deliveryOrderItems);
        dest.writeSerializable(this.customerDistance);
        dest.writeParcelable(this.storePoint, flags);
    }

    protected ResponseExceptionDelivery(Parcel in) {
        this.success = in.readString();
        this.errorMsg = in.readString();
        this.doNo = (Long) in.readValue(Long.class.getClassLoader());
        this.orderId = in.readString();
        this.storeId = in.readString();
        this.deliveryOrderItems = in.createTypedArrayList(DeliveryUnusualDataListEntity.CREATOR);
        this.customerDistance = (BigDecimal) in.readSerializable();
        this.storePoint = in.readParcelable(LatLonPoint.class.getClassLoader());
    }

    public static final Creator<ResponseExceptionDelivery> CREATOR = new Creator<ResponseExceptionDelivery>() {
        @Override
        public ResponseExceptionDelivery createFromParcel(Parcel source) {
            return new ResponseExceptionDelivery(source);
        }

        @Override
        public ResponseExceptionDelivery[] newArray(int size) {
            return new ResponseExceptionDelivery[size];
        }
    };
}
