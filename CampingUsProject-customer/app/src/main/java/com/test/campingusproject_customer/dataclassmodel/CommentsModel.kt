package com.test.campingusproject_customer.dataclassmodel

data class CommentsModel(
    val postIdx: Long, //게시글 Idx
    val commentsIdx: Long, //댓글 Idx
    val userId: String, //댓글을 적은 User 이름
    val contents: String, //댓글 내용
    val writeDate : String//작성 날짜
)