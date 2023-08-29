package com.test.campingusproject_customer.dataclassmodel

data class OrderProductModel (
    val orderId : String,               //주문 ID
    val orderProductName : String,      //상품 이름
    val orderProductCount : String,     //상품 개수
    val orderProductPrice : String,     //상품 가격
    val orderProductImage : String      //상품 이미지
)