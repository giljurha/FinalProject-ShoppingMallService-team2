package com.test.campingusproject_customer.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.campingusproject_customer.dataclassmodel.CartModel
import com.test.campingusproject_customer.dataclassmodel.CartProductModel
import com.test.campingusproject_customer.repository.CartRepository
import com.test.campingusproject_customer.repository.CustomerUserRepository

class CartViewModel() : ViewModel() {

    // 장바구니 목록
    var cartDataList = MutableLiveData<MutableList<CartModel>>()

    // 상품 정보 목록
    var cartProductList = MutableLiveData<MutableList<CartProductModel>>()

    init {
        cartDataList.value = mutableListOf<CartModel>()
    }


    // 장바구니 목록 화면
    fun getCartData(cartUserId: String) {

        val tempList = mutableListOf<CartModel>()
        val tempList2 = mutableListOf<CartProductModel>()

        CartRepository.getAllCartData(cartUserId) {
            if(it.result.exists() == true) {
                for (c1 in it.result.children) {
                    val cartUserId = c1.child("cartUserId").value as String
                    val cartProductId = c1.child("cartProductId").value as Long
                    val cartProductCount = c1.child("cartProductCount").value as Long

                    val cartProduct = CartModel(cartUserId, cartProductId, cartProductCount)
                    tempList.add(cartProduct)

                    CartRepository.getProductData(cartProductId) {
                        if(it.result.exists() == true) {
                            for (c2 in it.result.children) {
                                val productName = c2.child("productName").value as String
                                val productPrice = c2.child("productPrice").value as Long
                                val productImage = c2.child("productImage").value as String
                                val productInfo = c2.child("productInfo").value as String

                                Log.d("aaa", "${productPrice}")
                                Log.d("aaa", "${productInfo}")

                                val cartProductModel = CartProductModel(productName, productPrice, productImage, productInfo)
                                tempList2.add(cartProductModel)
                            }
                        } else {

                        }

                    }
                }

                cartDataList.value = tempList
                cartProductList.value = tempList2
            }
        }
    }

//    fun getProductData(cartProductId: Long) {
//        CartRepository.getProductData(cartProductId) {
//            for(c1 in it.result.children) {
//                productImage.value = c1.child("produceImage").value as String
//                productInfo.value = c1.child("productInfo").value as String
//                productPrice.value = c1.child("productPrice").value as Long
//            }
//        }
//    }
}