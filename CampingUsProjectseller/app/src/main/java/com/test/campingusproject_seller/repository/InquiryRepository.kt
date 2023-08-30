package com.test.campingusproject_seller.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.test.campingusproject_seller.dataclassmodel.InquiryModel

class InquiryRepository {

    companion object {

        // 판매자의 Id를 가져온다.
        fun getSellerId(callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val sellerIdRef = database.getReference("SellerId")

            sellerIdRef.get().addOnCompleteListener(callback1)
        }

        // 판매자 Id를 설정한다.
        fun setSellerId(sellerId: String, callback1: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val sellerIdRef = database.getReference("SellerId")

            sellerIdRef.get().addOnCompleteListener {
                it.result.ref.setValue(sellerId).addOnCompleteListener(callback1)
            }
        }

        // 판매자의 판매 상품 전체를 가져온다.
        fun getSellerProduct(sellerId: String, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val productDataRef = database.getReference("ProductData")
            productDataRef.orderByChild("productSellerId").equalTo(sellerId).get()
                .addOnCompleteListener(callback1)
        }

        // 판매자 문의 전체를 가져온다.
        fun getSellerInquiry(inquiryItemId: Long, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val inquiryDataRef = database.getReference("InquiryData")
            inquiryDataRef.orderByChild("inquiryItemId").equalTo(inquiryItemId.toDouble()).get()
                .addOnCompleteListener(callback1)
        }

        // 판매자 문의 전체를 가져온다.
        fun getInquiry(callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val inquiryDataRef = database.getReference("InquiryData")
            inquiryDataRef.orderByChild("inquiryIdx").get().addOnCompleteListener(callback1)
        }

        // 문의 상세정보를 가져온다.
        fun getInquiryDetail(inquiryIdx: Double, callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val inquiryDataRef = database.getReference("InquiryData")
            inquiryDataRef.orderByChild("inquiryIdx").equalTo(inquiryIdx).get()
                .addOnCompleteListener(callback1)
        }

        // 문의 인덱스 번호를 가져온다.
        fun getInquiryIdx(callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            // 문의 인덱스 번호
            val inquiryIdxRef = database.getReference("inquiryIdx")
            inquiryIdxRef.get().addOnCompleteListener(callback1)
        }

        // 문의 답변 작성
        fun inquiryAnswer(inquiryModel: InquiryModel, callback1: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val inquiryDataRef = database.getReference("InquiryData")

            inquiryDataRef.orderByChild("inquiryIdx").equalTo(inquiryModel.inquiryIdx.toDouble())
                .get()
                .addOnCompleteListener {
                    for (a1 in it.result.children) {
                        a1.ref.child("inqiuryAnswer").setValue(inquiryModel.inquiryAnswer)
                            .addOnCompleteListener(callback1)
                    }
                }
        }

        //상품의 대표이미지만 가져오는 함수
        fun getProductFirstImage(fileDir: String, callback1: (Task<Uri>) -> Unit) {
            val storage = FirebaseStorage.getInstance()
            val fileName = fileDir + "1.png"

            val imageRef = storage.reference.child(fileName)
            imageRef.downloadUrl.addOnCompleteListener(callback1)
        }

    }
}