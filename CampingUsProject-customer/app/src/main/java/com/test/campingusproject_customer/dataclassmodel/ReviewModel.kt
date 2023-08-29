package com.test.campingusproject_customer.dataclassmodel

data class ReviewModel(
    var reviewId: Long,                 // 리뷰 ID
    var reviewProductId: Long,          // 상품 ID
    var reviewWriterId: String,         // 리뷰 작성자 ID
    var reviewRating: Float,            // 리뷰 별점
    var reviewImage: String,            // 리뷰 사진
    var reviewContent: String           // 리뷰 내용
)