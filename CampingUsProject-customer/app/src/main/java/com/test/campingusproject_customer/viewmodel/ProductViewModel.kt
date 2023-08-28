package com.test.campingusproject_customer.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.campingusproject_customer.dataclassmodel.ProductModel
import com.test.campingusproject_customer.repository.ProductRepository

class ProductViewModel : ViewModel() {
    val productList = MutableLiveData<MutableList<ProductModel>>()
    val productImageList = MutableLiveData<MutableList<Uri>>()

    val productName = MutableLiveData<String>()
    val productPrice = MutableLiveData<Long>()
    val productImage = MutableLiveData<String>()
    val productInfo = MutableLiveData<String>()
    val productCount = MutableLiveData<Long>()
    val productDiscountRate = MutableLiveData<Long>()
    val productBrand = MutableLiveData<String>()
    val productCategory = MutableLiveData<String>()

    val productKeywordList = MutableLiveData<HashMap<String, Boolean>>()

    init {
        productList.value = mutableListOf<ProductModel>()
    }

    // 전체 상품 가져오기
    fun getAllProductData(){
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
                val productBrand = c1.child("productBrand").value as String
                val productKeyword = c1.child("productKeywordList").value as HashMap<String, Boolean>
                val productCategory = c1.child("productCategory").value as String

                val product = ProductModel(productId, productSellerId, productName, productPrice, productImage,
                    productInfo, productCount, productSellingStatus, productDiscountRate, productRecommendationCount,
                    productBrand, productKeyword, productCategory)

                tempList.add(product)
            }

            productList.value = tempList
        }
    }

    // 실시간 랭킹 상품 정렬해서 가져오기
    fun getAllProductRealTimeRankingData(){
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
                val productBrand = c1.child("productBrand").value as String
                val productKeyword = c1.child("productKeywordList").value as HashMap<String, Boolean>
                val productCategory = c1.child("productCategory").value as String

                val product = ProductModel(productId, productSellerId, productName, productPrice, productImage,
                    productInfo, productCount, productSellingStatus, productDiscountRate, productRecommendationCount,
                    productBrand, productKeyword, productCategory)

                tempList.add(product)

                // 조건부 내림차순 정렬
                tempList.sortedByDescending { it.productRecommendationCount }
            }

            productList.value = tempList
        }
    }

    // 인기특가 상품 가져오기
    fun getAllProductDiscountData(){
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
                val productBrand = c1.child("productBrand").value as String
                val productKeyword = c1.child("productKeywordList").value as HashMap<String, Boolean>
                val productCategory = c1.child("productCategory").value as String

                val product = ProductModel(productId, productSellerId, productName, productPrice, productImage,
                    productInfo, productCount, productSellingStatus, productDiscountRate, productRecommendationCount,
                    productBrand, productKeyword, productCategory)
                if(productDiscountRate != 0L) {
                    tempList.add(product)
                }
            }

            productList.value = tempList
        }
    }

    // 해당 카테고리의 전체 상품 가져오기
    fun getAllProductCategoryData(category: String){
        val tempList = mutableListOf<ProductModel>()

        ProductRepository.getAllProductData {
            for(c1 in it.result.children) {
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
                val productBrand = c1.child("productBrand").value as String
                val productKeyword = c1.child("productKeywordList").value as HashMap<String, Boolean>
                val productCategory = c1.child("productCategory").value as String

                if(productCategory == category) {
                    val product = ProductModel(productId, productSellerId, productName, productPrice, productImage,
                        productInfo, productCount, productSellingStatus, productDiscountRate, productRecommendationCount,
                        productBrand, productKeyword, productCategory)

                    tempList.add(product)
                }
            }

            productList.value = tempList
        }
    }
}