package com.example.foodorderapplication.model


import android.os.Parcel
import android.os.Parcelable

class OrderDetails():Parcelable {
var userUid:String?=null
    var userName:String?=null
    var foodNames:MutableList<String>?=null
    var foodImages:MutableList<String>?=null
    var foodPrices:MutableList<String>?=null
    var foodQuantities:MutableList<String>?=null
    var address:String?=null
    var totalPrices:String?=null
    var phoneNumber:String?=null
    var orderAccepted:Boolean?=null
    var paymentReceived:Boolean?=null
    var itemPushKey:String?=null
    var currentTime:Long=0

    constructor(parcel: Parcel) : this() {
        userUid = parcel.readString()
        userName = parcel.readString()
        address = parcel.readString()
        totalPrices = parcel.readString()
        phoneNumber = parcel.readString()
        orderAccepted = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        paymentReceived = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        itemPushKey = parcel.readString()
        currentTime = parcel.readLong()
    }


    constructor(
        userId: String,
        name: String,
        foodItemName: ArrayList<String>,
        foodItemPrice: ArrayList<String>,
        foodItemImage: ArrayList<String>,
        foodItemQuantities: ArrayList<Int>,
        address: String,
        totalAmount:String,
        phone: String,
        time: Long,
        itemPushKey: String?,
        b: Boolean,
        b1: Boolean
    ) : this(){
        this.userUid=userUid
        this.userName=userName
        this.foodNames=foodNames
        this.foodPrices=foodPrices
        this.foodImages=foodImages
        this.foodQuantities=foodQuantities
        this.address=address
        this.totalPrices= totalAmount
        this.phoneNumber=phone
        this.currentTime=time
        this.itemPushKey=itemPushKey
        this.orderAccepted=orderAccepted
        this.paymentReceived=paymentReceived


    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userUid)
        parcel.writeString(userName)
        parcel.writeString(address)
        parcel.writeString(totalPrices)
        parcel.writeString(phoneNumber)
        parcel.writeValue(orderAccepted)
        parcel.writeValue(paymentReceived)
        parcel.writeString(itemPushKey)
        parcel.writeLong(currentTime)
    }

    override fun describeContents(): Int {
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