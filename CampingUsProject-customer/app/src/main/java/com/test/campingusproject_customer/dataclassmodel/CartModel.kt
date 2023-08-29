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

data class CartProductModel(
    var productName: String?,
    var productPrice: Long,
    var productImage: String?,
    var productInfo: String?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productName)
        parcel.writeLong(productPrice)
        parcel.writeString(productImage)
        parcel.writeString(productInfo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartProductModel> {
        override fun createFromParcel(parcel: Parcel): CartProductModel {
            return CartProductModel(parcel)
        }

        override fun newArray(size: Int): Array<CartProductModel?> {
            return arrayOfNulls(size)
        }
    }
}

data class BundleData(var productId: Long, var productCount: Long) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(productId)
        parcel.writeLong(productCount)
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