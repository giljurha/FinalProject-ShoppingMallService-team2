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

    // 상품 하나 가져오기
    fun getOneProductData(productId:Long){
        ProductRepository.getOneProductData(productId){
            for(c1 in it.result.children){
                productName.value = c1.child("productName").value as String
                productPrice.value = c1.child("productPrice").value as Long
                productImage.value = c1.child("productImage").value as String
                productInfo.value = c1.child("productInfo").value as String
                productCount.value = c1.child("productCount").value as Long
                productDiscountRate.value = c1.child("productDiscountRate").value as Long
                productBrand.value = c1.child("productBrand").value as String
                productKeywordList.value = c1.child("productKeywordList").value as HashMap<String, Boolean>
                productCategory.value = c1.child("productCategory").value as String

                productImageList.value?.clear()
                ProductRepository.getProductImages(productImage.value.toString()){storageRef->
                    storageRef.downloadUrl.addOnCompleteListener{
                        if(it.isSuccessful){
                            val downloadUrl = it.result
                            val updatedList = productImageList.value?: mutableListOf()
                            updatedList.add(downloadUrl)
                            productImageList.value = updatedList
                        }
                    }
                }
            }
        }
    }
}