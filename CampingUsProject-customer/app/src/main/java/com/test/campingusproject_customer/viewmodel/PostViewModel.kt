package com.test.campingusproject_customer.viewmodel

import android.net.Uri
import android.util.Log
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
    var profileImagePath = MutableLiveData<String>() // 프로필 이미지 경로

    // 이미지 저장 리스트
    var postImageList = MutableLiveData<MutableList<Uri?>>()

    // 게시글 목록
    var postDataList = MutableLiveData<MutableList<PostModel>>()

    init {
        postDataList.value = mutableListOf<PostModel>()
        postImageList.value = mutableListOf<Uri?>()
    }

    //댓글 수 카운트
    fun setCommentsCount(count:Long){
        postCommentCount.value = count
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
                //이미지 경로
                postImagePath.value = c1.child("postImagePath").value as String
                //프로필 이미지 경로
                profileImagePath.value = c1.child("profileImagePath").value as String

                // 이미지가 있다면
                if (postImagePath.value != "null") {
                    postImageList.value?.clear()
                    PostRepository.getPostImages(postImagePath.value.toString()) { storageRef ->
                        storageRef.downloadUrl.addOnCompleteListener {
                            if (it.isSuccessful) {
                                val downloadUrl = it.result
                                val updatedList = postImageList.value ?: mutableListOf()
                                updatedList.add(0,downloadUrl)
                                postImageList.value = updatedList
                            }
                        }
                    }
                }
                else if(postImagePath.value == "null"){
                    postImageList.value?.clear()
                }
            }
        }
    }

    // 게시글 목록
    fun getPostAll(getPostType:Long){
        val tempList = mutableListOf<PostModel>()
        PostRepository.getPostAll {
            for(c1 in it.result.children){
                val postIdx = c1.child("postIdx").value as Long
                val postImagePath = c1.child("postImagePath").value as String
                val postSubject = c1.child("postSubject").value as String
                val postText = c1.child("postText").value as String
                val postType = c1.child("postType").value as Long
                val postWriteDate = c1.child("postWriteDate").value as String
                val postUserId = c1.child("postUserId").value as String
                val postLiked = c1.child("postLiked").value as Long
                val postCommentCount = c1.child("postCommentCount").value as Long
                //프로필 이미지 경로
                val profileImagePath = c1.child("profileImagePath").value as String

                if(getPostType != 0L && getPostType != postType){
                    continue
                }

                val p1 = PostModel(
                    postIdx,
                    postUserId,
                    postType,
                    postSubject,
                    postText,
                    postLiked,
                    postCommentCount,
                    postWriteDate,
                    postImagePath,
                    profileImagePath
                )
                tempList.add(p1)
            }
            // 데이터가 postIdx를 기준으로 오름 차순 정렬되어 있기 때문에
            // 순서를 뒤집는다.
            tempList.reverse()

            postDataList.value = tempList
        }
    }

    fun resetImageList(){
        postImageList.value?.clear()
    }

    // postDataList 초기화
    fun resetPostList(){
        postDataList.value = mutableListOf<PostModel>()
    }

    // 검색 결과를 가져온다.
    fun getSearchPostList(keyword:String){ //getPostType:Long,
        // 검색 결과를 담을 리스트
        val tempList = mutableListOf<PostModel>()

        PostRepository.getPostAll {
            for(c1 in it.result.children){
                val postIdx = c1.child("postIdx").value as Long
                val postImagePath = c1.child("postImagePath").value as String
                val postSubject = c1.child("postSubject").value as String
                val postText = c1.child("postText").value as String
                val postType = c1.child("postType").value as Long
                val postWriteDate = c1.child("postWriteDate").value as String
                val postUserId = c1.child("postUserId").value as String
                val postLiked = c1.child("postLiked").value as Long
                val postCommentCount = c1.child("postCommentCount").value as Long
                val profileImagePath = c1.child("profileImagePath").value as String
//                if(getPostType != 0L && getPostType != postType){
//                    continue
//                }

                if(postSubject.contains(keyword) == false && postText.contains(keyword) == false){
                    continue
                }

                val p1 = PostModel(
                    postIdx,
                    postUserId,
                    postType,
                    postSubject,
                    postText,
                    postLiked,
                    postCommentCount,
                    postWriteDate,
                    postImagePath,
                    profileImagePath
                )
                tempList.add(p1)

            }
            // 데이터가 postIdx를 기준으로 오름 차순 정렬되어 있기 때문에
            // 순서를 뒤집는다.
            tempList.reverse()

            postDataList.value = tempList
        }
    }

    // 검색 결과를 가져온다.
    fun getMyPostList(keyword:String){ //getPostType:Long,
        // 검색 결과를 담을 리스트
        val tempList = mutableListOf<PostModel>()

        PostRepository.getPostAll {
            for(c1 in it.result.children){
                val postIdx = c1.child("postIdx").value as Long
                val postImagePath = c1.child("postImagePath").value as String
                val postSubject = c1.child("postSubject").value as String
                val postText = c1.child("postText").value as String
                val postType = c1.child("postType").value as Long
                val postWriteDate = c1.child("postWriteDate").value as String
                val postUserId = c1.child("postUserId").value as String
                val postLiked = c1.child("postLiked").value as Long
                val postCommentCount = c1.child("postCommentCount").value as Long
                val profileImagePath = c1.child("profileImagePath").value as String
//                if(getPostType != 0L && getPostType != postType){
//                    continue
//                }

                if(postUserId.contains(keyword) == false){
                    continue
                }

                val p1 = PostModel(
                    postIdx,
                    postUserId,
                    postType,
                    postSubject,
                    postText,
                    postLiked,
                    postCommentCount,
                    postWriteDate,
                    postImagePath,
                    profileImagePath
                )
                tempList.add(p1)

            }
            // 데이터가 postIdx를 기준으로 오름 차순 정렬되어 있기 때문에
            // 순서를 뒤집는다.
            tempList.reverse()

            postDataList.value = tempList
        }
    }

    // 인기게시글 목록
    fun getPostPopularAll(){
        val tempList = mutableListOf<PostModel>()
        PostRepository.getPostPopularAll {
            for(c1 in it.result.children){
                val postIdx = c1.child("postIdx").value as Long
                val postImagePath = c1.child("postImagePath").value as String
                val postSubject = c1.child("postSubject").value as String
                val postText = c1.child("postText").value as String
                val postType = c1.child("postType").value as Long
                val postWriteDate = c1.child("postWriteDate").value as String
                val postUserId = c1.child("postUserId").value as String
                val postLiked = c1.child("postLiked").value as Long
                val postCommentCount = c1.child("postCommentCount").value as Long
                val profileImagePath = c1.child("profileImagePath").value as String

                val p1 = PostModel(
                    postIdx,
                    postUserId,
                    postType,
                    postSubject,
                    postText,
                    postLiked,
                    postCommentCount,
                    postWriteDate,
                    postImagePath,
                    profileImagePath
                )
                tempList.add(p1)
            }
            // 데이터가 postIdx를 기준으로 오름 차순 정렬되어 있기 때문에
            // 순서를 뒤집는다.
            tempList.reverse()

            postDataList.value = tempList
        }
    }
}