package com.test.campingusproject_seller.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.campingusproject_seller.dataclassmodel.ProductModel
import com.test.campingusproject_seller.repository.ProductRepository

class ProductViewModel() : ViewModel() {
    val productList = MutableLiveData<MutableList<ProductModel>>()

    init {
        productList.value = mutableListOf<ProductModel>()
    }

    fun getAllProductData(sellerId : String){
        val tempList = mutableListOf<ProductModel>()

        ProductRepository.getAllProductData {
            for(c1 in it.result.children){
                val productId = c1.child("productId").value as Long
                val productSellerId = c1.child("productSellerId").value as String
                val productName = c1.child("productName").value as String
                val productPrice = c1.child("productPrice").value as Long
                val productImage = c1.child("productImage").value as String
                val productInfo = c1.child("productInfo").value as String
                val productCount = c1.child("productCount").value as Long
                val productSellingStatus = c1.child("productSellingStatus").value as Boolean
                val productDiscountRate = c1.child("productDiscountRate").value as Long
                val productRecommendationCount = c1.child("productRecommendationCount").value as Long

                val product = ProductModel(productId, productSellerId, productName, productPrice, productImage,
                    productInfo, productCount, productSellingStatus, productDiscountRate, productRecommendationCount)

                tempList.add(product)
            }

            productList.value = tempList
        }
    }
}