package com.test.campingusproject_seller.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.campingusproject_seller.dataclassmodel.InquiryModel
import com.test.campingusproject_seller.dataclassmodel.InquiryProductModel
import com.test.campingusproject_seller.repository.InquiryRepository
import kotlinx.coroutines.runBlocking

class InquiryViewModel() : ViewModel() {
    var inquiryUserId = MutableLiveData<String>()        // 문의 작성자 ID
    var inquiryProductName = MutableLiveData<String>()   // 문의 아이템(제목)
    var inquiryContent = MutableLiveData<String>()       // 문의 내용
    var inquiryAnswer = MutableLiveData<String>()        // 답변 내용

    // 상품 목록
    var inquiryProductList = MutableLiveData<MutableList<InquiryProductModel>>()

    // 문의 목록
    var inquiryDataList = MutableLiveData<MutableList<InquiryModel>>()

    init {
        inquiryProductList.value = mutableListOf<InquiryProductModel>()
        inquiryDataList.value = mutableListOf<InquiryModel>()
    }

    // 문의 상세 화면
    fun setInquiryDetailData(inquiryIdx: Double) {
        // 문의 데이터를 가져온다.
        InquiryRepository.getInquiryDetail(inquiryIdx) {
            for (c1 in it.result.children) {
                // 문의 아이템(제목)
                inquiryProductName.value = c1.child("inquiryProductName").value as String
                // 문의 작성자 이름
                inquiryUserId.value = c1.child("inquiryUserId").value as String
                // 문의 내용
                inquiryContent.value = c1.child("inquiryContent").value as String
                // 답변 내용
                inquiryAnswer.value = c1.child("inquiryAnswer").value as? String ?: ""
            }
        }
    }

    // 문의 목록 화면
    fun setInquiryData(sellerId: String) {

        val tempList = mutableListOf<InquiryProductModel>()
        val tempList2 = mutableListOf<InquiryModel>()

        // 판매자의 판매 상품 데이터를 가져온다.
        InquiryRepository.getSellerProduct(sellerId) {
            for (c1 in it.result.children) {
                val productId = c1.child("productId").value as Long
                val productImage = c1.child("productImage").value as String

                val productModel = InquiryProductModel(productId, productImage)

                tempList.add(productModel)

                runBlocking {
                    getInquiryData(productModel, tempList2)
                }


            }
            inquiryProductList.value = tempList
            Log.d("test", "${tempList}")
            Log.d("test2", "${tempList2}")
        }
    }

    fun getInquiryData(inquiryItemModel: InquiryProductModel, tempList2: MutableList<InquiryModel>) {

        InquiryRepository.getSellerInquiry(inquiryItemModel.productId) {
            for (c2 in it.result.children) {
                val inquiryIdx = c2.child("inquiryIdx").value as? Long ?: 0L
                val inquiryItemId = c2.child("inquiryItemId").value as Long
                val inquiryUserId = c2.child("inquiryUserId").value as String
                val inquiryProductName = c2.child("inquiryProductName").value as String
                val inquiryContent = c2.child("inquiryContent").value as String
                val inquiryUserName = c2.child("inquiryUserName").value as String
                val inquiryWriteDate = c2.child("inquiryWriteDate").value as String
                val inquiryAnswer = c2.child("inquiryAnswer").value as String
                val inquiryResult = c2.child("inquiryResult").value as? Boolean ?: false
                val inquiryImage = c2.child("inquiryImage").value as String

                val p1 = InquiryModel(
                    inquiryIdx,
                    inquiryItemId,
                    inquiryUserId,
                    inquiryProductName,
                    inquiryContent,
                    inquiryUserName,
                    inquiryWriteDate,
                    inquiryAnswer,
                    inquiryResult,
                    inquiryImage
                )

                tempList2.add(p1)
            }

            // 순서 뒤집기
            // tempList.reverse()

            inquiryDataList.value = tempList2
        }
    }

//    // 문의 목록
//    fun getInquiryList(itemId: Long) { //getPostType:Long,
//        // 검색 결과를 담을 리스트
//        val tempList = mutableListOf<InquiryModel>()
//
//        InquiryRepository.getInquiry {
//            for (c1 in it.result.children) {
//                val inquiryIdx = c1.child("inquiryIdx").value as Long
//                val inquiryItemId = c1.child("inquiryItemId").value as Long
//                val inquiryUserId = c1.child("inquiryUserId").value as String
//                val inquiryItemTitle = c1.child("inquiryItemTitle").value as String
//                val inquiryContent = c1.child("inquiryContent").value as String
//                val inquiryUserName = c1.child("inquiryUserName").value as String
//                val inquiryWriteDate = c1.child("inquiryWriteDate").value as String
//                val inquiryAnswer = c1.child("inquiryAnswer").value as String
//                val inquiryResult = c1.child("inquiryResult").value as Boolean
//
//                if (inquiryItemId != itemId) {
//                    continue
//                }
//
//                val p1 = InquiryModel(
//                    inquiryIdx,
//                    inquiryItemId,
//                    inquiryUserId,
//                    inquiryItemTitle,
//                    inquiryContent,
//                    inquiryUserName,
//                    inquiryWriteDate,
//                    inquiryAnswer,
//                    inquiryResult
//                )
//
//                tempList.add(p1)
//
//            }
//            // 데이터가 postIdx를 기준으로 오름 차순 정렬되어 있기 때문에
//            // 순서를 뒤집는다.
//            tempList.reverse()
//
//            inquiryDataList.value = tempList
//        }
//    }
}