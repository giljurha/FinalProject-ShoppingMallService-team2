package com.test.campingusproject_customer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.campingusproject_customer.dataclassmodel.InquiryModel
import com.test.campingusproject_customer.repository.InquiryRepository

class InquiryViewModel: ViewModel() {
    var inquiryList = MutableLiveData<MutableList<InquiryModel>>()

    init {
        inquiryList.value = mutableListOf()
    }

    fun getQuestionList(userId: String){
        val tempList = mutableListOf<InquiryModel>()
        InquiryRepository.getInquiryData(userId){
            for(c1 in it.result.children){
                var inquiryIdx = c1.child("inquiryIdx").value as Long
                var inquiryItemId = c1.child("inquiryItemId").value as Long
                var inquiryUserId = c1.child("inquiryUserId").value as String
                var inquiryProductName = c1.child("inquiryProductName").value as String
                var inquiryContent = c1.child("inquiryContent").value as String
                var inquiryUserName = c1.child("inquiryUserName").value as String
                var inquiryWriteDate = c1.child("inquiryWriteDate").value as String
                var inquiryAnswer = c1.child("inquiryAnswer").value as String
                var inquiryResult = c1.child("inquiryResult").value as Boolean
                var inquiryImage = c1.child("inquiryImage").value as String

                val inquiryModel = InquiryModel(inquiryIdx, inquiryItemId, inquiryUserId, inquiryProductName,
                    inquiryContent, inquiryUserName, inquiryWriteDate, inquiryAnswer, inquiryResult, inquiryImage)

                tempList.add(inquiryModel)
            }
            inquiryList.value = tempList
        }
    }
}