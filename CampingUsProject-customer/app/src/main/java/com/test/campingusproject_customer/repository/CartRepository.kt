package com.test.campingusproject_customer.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.test.campingusproject_customer.dataclassmodel.CartModel

class CartRepository {
    companion object {

        // 상품 정보를 장바구니에 추가하는 함수
        fun addCartData(cartModel: CartModel, callback1: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()

            val cartRef = database.getReference("CartData")
            cartRef.push().setValue(cartModel).addOnCompleteListener(callback1)
        }

        // 상품 정보를 장바구니에서 제거하는 함수
        fun removeCartData(cartProudctId: String) {
            val database = FirebaseDatabase.getInstance()
            val cartRef = database.getReference("CartData")

            cartRef.orderByChild("cartProductId").equalTo(cartProudctId.toString()).get()
                .addOnCompleteListener {
                    for (a1 in it.result.children) {
                        a1.ref.removeValue()
                    }
                }
        }

        // 장바구니 상품 개수 설정하는 함수
        fun setCartCount(cartModel: CartModel) {
            val database = FirebaseDatabase.getInstance()
            val cartRef = database.getReference("CartData")

            cartRef.orderByChild("cartUserId").equalTo(cartModel.cartUserId).get().addOnCompleteListener {
                cartRef.orderByChild("cartProductId").equalTo(cartModel.cartProductId.toDouble()).get().addOnCompleteListener {
                    for (a1 in it.result.children) {
                        a1.ref.child("cartProductCount").setValue(cartModel.cartProductCount)
                    }
                }
            }
        }

        // 모든 장바구니 상품 가져오는 함수
        fun getAllCartData(cartUserId: String, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val cartRef = database.getReference("CartData")

            cartRef.orderByChild("cartUserId").equalTo(cartUserId).get()
                .addOnCompleteListener(callback1)
        }

        // 상품 정보 가져오는 함수
        fun getProductData(cartProductId: Long, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val productRef = database.getReference("ProductData")

            productRef.orderByChild("productId").equalTo(cartProductId.toDouble()).get()
                .addOnCompleteListener(callback1)
        }

        //상품의 대표이미지만 가져오는 함수
        fun getProductFirstImage(fileDir: String, callback1: (Task<Uri>) -> Unit) {
            val storage = FirebaseStorage.getInstance()
            val fileName = fileDir + "1.png"

            val imageRef = storage.reference.child(fileName)
            imageRef.downloadUrl.addOnCompleteListener(callback1)
        }


    }
}