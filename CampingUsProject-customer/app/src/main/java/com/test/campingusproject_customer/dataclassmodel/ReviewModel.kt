package com.test.campingusproject_customer.dataclassmodel

import android.net.Uri

data class ReviewModel(
    var reviewId: String,               // 리뷰 ID
    var reviewProductId: String,        // 상품 ID
    var reviewWriterId: String,         // 리뷰 작성자 ID
    var reviewRating: String,           // 리뷰 별점
    var reviewImage: Uri,               // 리뷰 사진
    var reviewRecommendation: String    // 상품 추천
)