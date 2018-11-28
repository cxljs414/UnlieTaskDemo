package com.xstore.tms.android.entity.delivery

import android.os.Parcel
import android.os.Parcelable
import java.math.BigDecimal

/**
 * Created by hly on 2018/7/20.
 * email hly910206@gmail.com
 */
data class DeliveryUnusualListEntity(

        var imageUrl: String? = null,


        var thumbUrl: String? = null,

        var skuId: Long? = null,
        var name: String? = null,

        var shippedQty: BigDecimal? = null,   //实际发货
        var expectedQty: BigDecimal? = null,  //期望发货

        var saleMode: Int? = null,
        var unit: String? = null,

        var reason: String? = null,
        var misTakeNum: String? = null,
        var leakeNum: String? = null,
        var damageNum: String? = null,
        var checkIndex :Int? = null,
        var isMisCheck :Boolean = false,
        var isLeakCheck :Boolean = false,
        var isDamageCheck :Boolean = false





) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
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
        writeString(imageUrl)
        writeString(thumbUrl)
        writeValue(skuId)
        writeString(name)
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
        val CREATOR: Parcelable.Creator<DeliveryUnusualListEntity> = object : Parcelable.Creator<DeliveryUnusualListEntity> {
            override fun createFromParcel(source: Parcel): DeliveryUnusualListEntity = DeliveryUnusualListEntity(source)
            override fun newArray(size: Int): Array<DeliveryUnusualListEntity?> = arrayOfNulls(size)
        }
    }
}

