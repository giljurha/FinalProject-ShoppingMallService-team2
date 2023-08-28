package com.test.campingusproject_customer.repository

import com.google.android.gms.tasks.Task
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
    }
}