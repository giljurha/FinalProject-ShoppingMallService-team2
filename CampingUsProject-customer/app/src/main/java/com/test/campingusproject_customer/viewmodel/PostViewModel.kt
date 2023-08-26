package com.test.campingusproject_customer.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.campingusproject_customer.dataclassmodel.PostModel
import com.test.campingusproject_customer.repository.PostRepository

class PostViewModel() : ViewModel() {
    var postSubject = MutableLiveData<String>() //게시글 제목
    var postText = MutableLiveData<String>() //게시글 내용
    var postImagePath = MutableLiveData<String>() //게시글 사진 경로
    var postUserId = MutableLiveData<String>() // 유저 ID
    var postWriteDate = MutableLiveData<String>() // 게시글 작성 날짜
    var postType = MutableLiveData<Long>() // 게시글 타입
    var postLiked = MutableLiveData<Long>() // 좋아요 수
    var postCommentCount = MutableLiveData<Long>()// 댓글 수

    // 이미지 저장 리스트
    var postImageList = MutableLiveData<MutableList<Uri>>()

    // 게시글 목록
    var postDataList = MutableLiveData<MutableList<PostModel>>()

    init {
        postDataList.value = mutableListOf<PostModel>()

    }

    // 게시글 읽기 화면
    fun getOnePostReadData(postIdx: Double) {
        // 게시글 데이터를 가져온다
        PostRepository.getPostInfo(postIdx) {
            for (c1 in it.result.children) {
                // 게시글 제목
                postSubject.value = c1.child("postSubject").value as String
                // 게시글 내용
                postText.value = c1.child("postText").value as String
                // 게시글 작성 날짜
                postWriteDate.value = c1.child("postWriteDate").value as String
                //작성자 ID
                postUserId.value = c1.child("postUserId").value as String
                //게시글 타입
                postType.value = c1.child("postType").value as Long
                //좋아요 수
                postLiked.value = c1.child("postLiked").value as Long
                // 댓글 수
                postCommentCount.value = c1.child("postCommentCount").value as Long

                // 이미지가 있다면
                if (postImagePath.value != "null") {
                    PostRepository.getPostImages(postImagePath.value.toString()) { storageRef ->
                        storageRef.downloadUrl.addOnCompleteListener {
                            if (it.isSuccessful) {
                                val downloadUrl = it.result
                                postImageList.value?.add(downloadUrl)
//                                val updatedList = postImageList.value ?: mutableListOf()
//                                updatedList.add(0,downloadUrl)
//                                postImageList.value = updatedList
                            }
                        }
                    }
                }
            }
        }
    }
}