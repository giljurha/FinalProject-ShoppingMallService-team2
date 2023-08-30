package com.test.campingusproject_seller.dataclassmodel

data class InquiryModel(
    var inquiryIdx: Long,             // 문의 IDX
    var inquiryItemId: Long,          // 제품 아이템 ID
    var inquiryUserId: String,        // 문의 작성자 ID
    var inquiryProductName: String,     // 문의 아이템(제목)
    var inquiryContent: String,       // 문의 내용
    var inquiryUserName: String,      // 문의 작성자 이름
    var inquiryWriteDate: String?,    // 문의 작성 날짜
    var inquiryAnswer: String?,       // 문의 답변
    var inquiryResult: Boolean        // 답변 여부
)