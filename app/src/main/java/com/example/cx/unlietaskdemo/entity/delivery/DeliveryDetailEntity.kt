//package com.xstore.tms.android.entity.delivery
//
//import android.os.Parcel
//import android.os.Parcelable
//import java.math.BigDecimal
//import java.util.*
//
///**
// * Created by hly on 2018/3/20.
// * email hly910206@gmail.com
// */
//
//class DeliveryDetailEntity() : Parcelable {
//
//    var orderId: String? = null
//
//    var consignee: String? = null
//
//    var mobilePhone: String? = null
//
//    var address: String? = null
//
//    var waybillNumber: BigDecimal? = null
//
//    var orderCreateDate: Date? = null
//
//    var infoList: List<DeliveryOrderInfo>? = null
//
//    var target = DoubleArray(2)
//
//    var store = DoubleArray(2)
//
//    var lastDate: Date? = null
//
//    constructor(parcel: Parcel) : this() {
//        orderId = parcel.readString()
//        consignee = parcel.readString()
//        mobilePhone = parcel.readString()
//        address = parcel.readString()
//        infoList = parcel.createTypedArrayList(DeliveryOrderInfo.CREATOR)
//        target = parcel.createDoubleArray()
//        store = parcel.createDoubleArray()
//    }
//
//
//    class DeliveryOrderInfo() : Parcelable {
//
//        var name: String? = null
//        var shippedQty: BigDecimal? = null   //实际发货
//        var expectedQty: BigDecimal? = null  //期望发货
//
//        constructor(parcel: Parcel) : this() {
//            name = parcel.readString()
//        }
//
//        override fun writeToParcel(parcel: Parcel, flags: Int) {
//            parcel.writeString(name)
//        }
//
//        override fun describeContents(): Int {
//            return 0
//        }
//
//        companion object CREATOR : Parcelable.Creator<DeliveryOrderInfo> {
//            override fun createFromParcel(parcel: Parcel): DeliveryOrderInfo {
//                return DeliveryOrderInfo(parcel)
//            }
//
//            override fun newArray(size: Int): Array<DeliveryOrderInfo?> {
//                return arrayOfNulls(size)
//            }
//        }
//
//
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(orderId)
//        parcel.writeString(consignee)
//        parcel.writeString(mobilePhone)
//        parcel.writeString(address)
//        parcel.writeTypedList(infoList)
//        parcel.writeDoubleArray(target)
//        parcel.writeDoubleArray(store)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<DeliveryDetailEntity> {
//        override fun createFromParcel(parcel: Parcel): DeliveryDetailEntity {
//            return DeliveryDetailEntity(parcel)
//        }
//
//        override fun newArray(size: Int): Array<DeliveryDetailEntity?> {
//            return arrayOfNulls(size)
//        }
//    }
//
//
//}
