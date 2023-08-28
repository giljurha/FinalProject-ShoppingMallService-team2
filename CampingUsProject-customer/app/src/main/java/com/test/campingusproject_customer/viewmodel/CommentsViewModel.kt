package com.test.campingusproject_customer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.campingusproject_customer.dataclassmodel.CommentsModel
import com.test.campingusproject_customer.dataclassmodel.PostModel
import com.test.campingusproject_customer.repository.CommentsRepository
import com.test.campingusproject_customer.repository.PostRepository

class CommentsViewModel : ViewModel(){

    var postCommentsPostIdx = MutableLiveData<Long>() //게시글 Idx
    var postCommentsIdCommentsIdx = MutableLiveData<Long>() //댓글 Idx
    var postCommentsUserId = MutableLiveData<String>() //댓글을 적은 User 이름
    var postCommentsContents = MutableLiveData<String>() //댓글 내용
    var postCommentsWriteDate = MutableLiveData<String>()//작성 날짜

    var commentsList = MutableLiveData<MutableList<CommentsModel>>()

    init {
        commentsList.value = mutableListOf<CommentsModel>()
    }

    // 댓글 가져온다.
    fun getCommentsList(getPostIdx : Long){ //getPostType:Long,
        // 검색 결과를 담을 리스트
        val tempList = mutableListOf<CommentsModel>()

        CommentsRepository.getCommentsAll {
            for(c1 in it.result.children){
                val postIdx= c1.child("postIdx").value as Long
                val commentsIdx= c1.child("commentsIdx").value as Long
                val userId = c1.child("userId").value as String
                val contents = c1.child("contents").value as String
                val writeDate =c1.child("writeDate").value as String

                if(postIdx != getPostIdx){
                    continue
                }

                val p1 = CommentsModel(
                    postIdx,
                    commentsIdx,
                    userId,
                    contents,
                    writeDate
                )
                tempList.add(p1)

            }
            // 데이터가 postIdx를 기준으로 오름 차순 정렬되어 있기 때문에
            // 순서를 뒤집는다.
            tempList.reverse()

            commentsList.value = tempList
        }
    }
}