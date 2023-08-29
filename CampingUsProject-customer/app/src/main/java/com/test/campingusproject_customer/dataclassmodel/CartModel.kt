package com.test.campingusproject_customer.dataclassmodel

import android.os.Parcel
import android.os.Parcelable

data class CartModel(
    var cartUserId: String,         // 유저 ID
    var cartProductId: Long,        // 상품 ID
    var cartProductCount: Long      // 상품 개수
) {
    constructor() : this("", 0, 0)
}
//    : Parcelable {
//    constructor(parcel: Parcel) : this(
//        parcel.readString().toString(),
//        parcel.readLong(),
//        parcel.readLong()
//    ) {
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(cartUserId)
//        parcel.writeLong(cartProductId)
//        parcel.writeLong(cartProductCount)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<CartModel> {
//        override fun createFromParcel(parcel: Parcel): CartModel {
//            return CartModel(parcel)
//        }
//
//        override fun newArray(size: Int): Array<CartModel?> {
//            return arrayOfNulls(size)
//        }
//    }
//
//}

data class CartProductModel(
    var productName: String,
    var productPrice: Long,
    var productImage: String,
    var productInfo: String,
)

data class BundleData(var productId: Long, var productCount: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(productId)
        parcel.writeInt(productCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BundleData> {
        override fun createFromParcel(parcel: Parcel): BundleData {
            return BundleData(parcel)
        }

        override fun newArray(size: Int): Array<BundleData?> {
            return arrayOfNulls(size)
        }
    }
}