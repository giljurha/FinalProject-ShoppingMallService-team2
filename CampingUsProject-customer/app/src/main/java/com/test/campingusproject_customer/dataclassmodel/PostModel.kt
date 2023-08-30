package com.test.campingusproject_customer.dataclassmodel

// 게시글 정보를 담을 클래스
data class PostModel(
    var postIdx: Long,              // 게시글 인덱스 번호
    var postUserId: String,         // 게시글 작성자 ID
    var postType: Long,             // 게시판 종류
    var postSubject: String,        // 제목
    var postText: String,           // 내용
    var postLiked: Long = 0L,            // 좋아요 수
    var postCommentCount : Long = 0L,     // 댓글 수
    var postWriteDate: String,      // 작성일
    var postImagePath: String,          // 첨부이미지 파일 이름
    var profileImagePath :String // 프로필 이미지 경로
)