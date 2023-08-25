package com.test.campingusproject_customer.dataclassmodel

data class CustomerUserModel(
    var customerUserName : String,          //유저 이름
    var customerUserId : String,            //유저 아이디
    var customerUserPw : String,            //유저 비밀번호
    var customerUserShipRecipient : String, //유저 배송자명
    var customerUserShipContact : String,   //유저 배송 연락처
    var customerUserShipAddress : String,   //유저 배송 주소
    var customerUserPhoneNumber : String,   //유저 휴대폰번호
    var customerUserProfileImage : String = ""   //유저 프로필 사진
)