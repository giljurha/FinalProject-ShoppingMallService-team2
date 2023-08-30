package com.test.campingusproject_customer.repository

import android.provider.ContactsContract.Data
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.test.campingusproject_customer.dataclassmodel.CustomerUserModel
import com.test.campingusproject_customer.dataclassmodel.InquiryModel

class InquiryRepository {
    companion object {
        // 작성한 문의를 DB에 저장
        fun setInquiryData(inquiryModel: InquiryModel, callback1 : (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val inquiryRef = database.getReference("InquiryData")
            inquiryRef.push().setValue(inquiryModel).addOnCompleteListener(callback1)
        }

        fun getInquiryData(userId : String, callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()

            val inquiryRef = database.getReference("InquiryData")
            inquiryRef.orderByChild("inquiryUserId").equalTo(userId).get().addOnCompleteListener(callback1)
        }
    }
}