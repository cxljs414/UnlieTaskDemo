package com.xstore.tms.android.entity.delivery

import android.os.Parcel
import android.os.Parcelable
import java.math.BigDecimal

/**
 * Created by hly on 2018/7/20.
 * email hly910206@gmail.com
 */
data class DeliveryUnusualDataListEntity(

        var imagePathSuffix :String? = null,
        var imageUrl: String? = null,
        var skuUuid: String? = null,
        var thumbUrl: String? = null,
        var abnormalCompleteFlag: Int? = null,
        var skuId: Long? = null,

        var productName: String? = null,

        var shippedQty: BigDecimal? = null,   //实际发货
        var expectedQty: BigDecimal? = null,  //期望发货

        var saleMode: Int? = null,
        var unit: String? = null,

        var reason: String? = null,
        var misTakeNum: String? = null,
        var leakeNum: String? = null,
        var damageNum: String? = null,
        var checkIndex: Int? = null,
        var isMisCheck: Boolean = false,
        var isLeakCheck: Boolean = false,
        var isDamageCheck: Boolean = false


) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readSerializable() as BigDecimal?,
            source.readSerializable() as BigDecimal?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Boolean::class.java.classLoader) as Boolean,
            source.readValue(Boolean::class.java.classLoader) as Boolean,
            source.readValue(Boolean::class.java.classLoader) as Boolean
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(imagePathSuffix)
        writeString(imageUrl)
        writeString(skuUuid)
        writeString(thumbUrl)
        writeValue(abnormalCompleteFlag)
        writeValue(skuId)
        writeString(productName)
        writeSerializable(shippedQty)
        writeSerializable(expectedQty)
        writeValue(saleMode)
        writeString(unit)
        writeString(reason)
        writeString(misTakeNum)
        writeString(leakeNum)
        writeString(damageNum)
        writeValue(checkIndex)
        writeValue(isMisCheck)
        writeValue(isLeakCheck)
        writeValue(isDamageCheck)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DeliveryUnusualDataListEntity> = object : Parcelable.Creator<DeliveryUnusualDataListEntity> {
            override fun createFromParcel(source: Parcel): DeliveryUnusualDataListEntity = DeliveryUnusualDataListEntity(source)
            override fun newArray(size: Int): Array<DeliveryUnusualDataListEntity?> = arrayOfNulls(size)
        }
    }
}

