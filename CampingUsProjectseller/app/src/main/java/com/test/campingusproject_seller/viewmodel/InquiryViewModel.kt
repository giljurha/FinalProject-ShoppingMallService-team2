package com.test.campingusproject_seller.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.campingusproject_seller.dataclassmodel.InquiryModel
import com.test.campingusproject_seller.repository.InquiryRepository

class InquiryViewModel() : ViewModel() {
    var inquiryUserId = MutableLiveData<String>()      // 문의 작성자 ID
    var inquiryItemTitle = MutableLiveData<String>()   // 문의 아이템(제목)
    var inquiryContent = MutableLiveData<String>()     // 문의 내용

    // 문의 목록
    var inquiryDataList = MutableLiveData<MutableList<InquiryModel>>()

    init {
        inquiryDataList.value = mutableListOf<InquiryModel>()
    }

    // 문의 상세 화면
    fun setInquiryDetailData(inquiryIdx:Double) {
        // 문의 데이터를 가져온다.
        InquiryRepository.getInquiryDetail(inquiryIdx) {
            for(c1 in it.result.children) {
                // 문의 아이템(제목)
                inquiryItemTitle.value = c1.child("inquiryItemTitle").value as String
                // 문의 작성자 이름
                inquiryUserId.value = c1.child("inquiryUserId").value as String
                // 문의 내용
                inquiryContent.value = c1.child("inquiryContent").value as String
            }
        }
    }

    // 문의 목록 화면
    fun setInquiryData(sellerId: String) {

        val tempList = mutableListOf<InquiryModel>()

        // 판매자의 판매 상품 데이터를 가져온다.
        InquiryRepository.getSellerProduct(sellerId) {
            for (c1 in it.result.children) {
                InquiryRepository.getSellerInquiry(c1.child("inquiryItemId").value as String) {
                    for (c2 in it.result.children) {
                        val inquiryIdx = c2.child("inquiryIdx").value as Long
                        val inquiryItemId = c2.child("inquiryItemId").value as Long
                        val inquiryUserId = c2.child("inquiryUserId").value as String
                        val inquiryItemTitle = c2.child("inquiryItemTitle").value as String
                        val inquiryContent = c2.child("inquiryContent").value as String
                        val inquiryUserName = c2.child("inquiryUserName").value as String
                        val inquiryWriteDate = c2.child("inquiryWriteDate").value as String
                        val inquiryAnswer = c2.child("inquiryAnswer").value as String
                        val inquiryResult = c2.child("inquiryResult").value as Boolean

                        val p1 = InquiryModel(
                            inquiryIdx,
                            inquiryItemId,
                            inquiryUserId,
                            inquiryItemTitle,
                            inquiryContent,
                            inquiryUserName,
                            inquiryWriteDate,
                            inquiryAnswer,
                            inquiryResult
                        )

                        tempList.add(p1)
                    }

                    // 순서 뒤집기
                    // tempList.reverse()

                    inquiryDataList.value = tempList
                }
            }
        }
    }
}