package com.example.food.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable


class OrderDetail():Serializable{
    var userUid: String? = null
    var userName: String? = null
    var foodImages: MutableList<String>? = null
    var foodPrices: MutableList<String>? = null
    var foodQuantity: MutableList<String>? =null
    var address: String? = null
    var totalPrice: String? = null
    var phoneNumber: String? = null
    var orderAccepted: Boolean? = false
    var itemPushKey: String? = null
    var currentItem: Long = 0
    var paymentReceived: Boolean = false
    var foodNames: MutableList<String>? = null

    constructor(parcel: Parcel) : this() {
        userUid = parcel.readString()
        userName = parcel.readString()
        address = parcel.readString()
        totalPrice = parcel.readString()
        phoneNumber = parcel.readString()
        orderAccepted = parcel.readByte() != 0.toByte()
        itemPushKey = parcel.readString()
        currentItem = parcel.readLong()
        paymentReceived = parcel.readByte() != 0.toByte()
    }

    fun describeContents() {

    }

    fun writeToParcel(dest: Parcel, flags: Int) {

    }

    companion object CREATOR : Parcelable.Creator<OrderDetail> {
        override fun createFromParcel(parcel: Parcel): OrderDetail {
            return OrderDetail(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetail?> {
            return arrayOfNulls(size)
        }
    }
}
