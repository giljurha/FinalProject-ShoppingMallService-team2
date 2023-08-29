package com.test.campingusproject_customer.dataclassmodel

data class OrderModel (
    val orderUserId : String,               //유저ID
    val orderId : String,                   //주문 번호
    val orderDate : String,                 //주문 날짜
    val orderPayment : String,              //결제 수단
    val orderStatus : String,               //주문 처리 상태
    val orderDeliveryRecipent : String,     //배송 받는 사람
    val orderDeliveryContact : String,      //배송 연락처
    val orderDeliveryAddress : String,      //배송 주소
    val orderCustomerUserName : String,     //주문자 이름
    val orderCustomerUserPhone : String     //주문자 전화번호
)