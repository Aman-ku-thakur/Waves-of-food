package com.example.myapplication.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import kotlin.collections.ArrayList

class OrderDetails(): Serializable{


    var userUid: String? = null
    var userName: String? = null
    var foodImages: MutableList<String>? = null
    var foodPrices: MutableList<String>? = null
    var foodQuantity: MutableList<String>? = null
    var address: String? = null
    var totalPrice: String? = null
    var phoneNumber: String? = null
    var orderAccepted:Boolean = false
    var itemPushKey: String? = null
    var currentItem: Long =0
    var paymentRecieved:Boolean ?= null
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
        paymentRecieved = parcel.readByte() != 0.toByte()
    }

    constructor(
        userId: String,
        name: String,
        foodItemName: ArrayList<String>,
        foodItemPrice: ArrayList<String>,
        foodItemImage: ArrayList<String>,
        foodItemquantities: ArrayList<Int>,
        address: String,
        phone: String,
        time: Long,
        itempushkey: String?,
        b: Boolean,
        b1: Boolean,
        totalamount: String?
    ):this() {
        this.userUid = userId
        this.userName = name
this.foodNames = foodItemName
        this.foodPrices = foodItemPrice
        this.foodImages = foodItemImage
        this.foodQuantity = foodItemquantities.map { it.toString() }.toMutableList()
        this.address = address
        this.totalPrice =totalamount
        this.phoneNumber = phone
        this.currentItem = time
        this.itemPushKey = itempushkey
        this.orderAccepted = b
        this.paymentRecieved = b1
    }

     fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userUid)
        parcel.writeString(userName)
        parcel.writeString(address)
        parcel.writeString(totalPrice)
        parcel.writeString(phoneNumber)
        parcel.writeValue(orderAccepted)
        parcel.writeString(itemPushKey)
        parcel.writeLong(currentItem)
        parcel.writeValue(paymentRecieved)
    }

    fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderDetails> {
        override fun createFromParcel(parcel: Parcel): OrderDetails {
            return OrderDetails(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetails?> {
            return arrayOfNulls(size)
        }
    }

}