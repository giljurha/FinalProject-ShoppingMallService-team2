package com.test.campingusproject_customer.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.test.campingusproject_customer.dataclassmodel.CommentsModel
import com.test.campingusproject_customer.dataclassmodel.PostModel

class CommentsRepository {
    companion object{
        //댓글 저장
        fun addComments(commentsModel: CommentsModel, callback1: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val commentsDataRef = database.getReference("CommentsData")
            commentsDataRef.push().setValue(commentsModel).addOnCompleteListener(callback1)
        }

        // 댓글 인덱스 번호를 가져온다.
        fun getCommentsIdx(callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            // 게시글 인덱스 번호
            val commentsDataRef = database.getReference("CommentsIdx")
            commentsDataRef.get().addOnCompleteListener(callback1)
        }
        // 댓글 번호를 저장한다.
        fun setCommentsIdx(commentsIdx: Long, callback1: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val commentsDataRef = database.getReference("CommentsIdx")
            // 게시글 인덱스번호 저장
            commentsDataRef.get().addOnCompleteListener {
                it.result.ref.setValue(commentsIdx).addOnCompleteListener(callback1)
            }
        }

        // 댓글 전체를 가져온다.
        fun getCommentsAll(callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val commentsDataRef = database.getReference("CommentsData")
            commentsDataRef.orderByChild("commentsIdx").get().addOnCompleteListener(callback1)
        }


    }
}