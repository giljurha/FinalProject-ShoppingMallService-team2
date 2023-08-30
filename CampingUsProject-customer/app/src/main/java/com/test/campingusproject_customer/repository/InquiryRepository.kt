package com.test.campingusproject_customer.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.test.campingusproject_customer.dataclassmodel.InquiryModel

class InquiryRepository {
    companion object {
        // 작성한 문의를 DB에 저장
        fun setInquiryData(inquiryModel: InquiryModel, callback1 : (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val inquiryRef = database.getReference("InquiryData")
            inquiryRef.push().setValue(inquiryModel).addOnCompleteListener(callback1)
        }

        // 문의 인덱스 번호를 가져온다.
        fun getInquiryIdx(callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            // 게시글 인덱스 번호
            val inquiryIdxRef = database.getReference("InquiryIdx")
            inquiryIdxRef.get().addOnCompleteListener(callback1)
        }

        // 문의 인덱스 번호를 저장한다.
        fun setInquiryIdx(inquiryIdx: Long, callback1: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val inquiryIdxRef = database.getReference("InquiryIdx")
            // 게시글 인덱스번호 저장
            inquiryIdxRef.get().addOnCompleteListener {
                it.result.ref.setValue(inquiryIdx).addOnCompleteListener(callback1)
            }
        }
    }
}