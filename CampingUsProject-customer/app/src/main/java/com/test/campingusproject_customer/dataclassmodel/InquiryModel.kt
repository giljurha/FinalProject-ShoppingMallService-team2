package com.test.campingusproject_customer.dataclassmodel

import java.io.Serializable

data class InquiryModel(
    var inquiryItemId: Long,                // 제품 아이템 ID
    var inquiryUserId: String,              // 문의 작성자 ID
    var inquiryProductName: String,         // 문의 아이템(제목)
    var inquiryContent: String,             // 문의 내용
    var inquiryUserName: String,            // 문의 작성자 이름
    var inquiryWriteDate: String,           // 문의 작성 날짜
    var inquiryAnswer: String,              // 문의 답변
    var inquiryQuestion: Boolean,           // 답변 여부
    var inquiryImage : String               // 문의 상품 이미지
): Serializable