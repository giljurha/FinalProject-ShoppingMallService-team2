package com.test.campingusproject_customer.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.test.campingusproject_customer.dataclassmodel.ReviewModel

class ReviewRepository {
    companion object {
        // 리뷰 인덱스를 DB에 저장
        fun setReviewIdx(reviewId: Long, callback1: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val reviewIdRef = database.getReference("ReviewIdx")

            reviewIdRef.get().addOnCompleteListener {
                it.result.ref.setValue(reviewId).addOnCompleteListener(callback1)
            }
        }

        // 리뷰 인덱스를 DB에서 가져온다.
        fun getReviewIdx(callback1: (Task<DataSnapshot>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val reviewIdRef = database.getReference("ReviewIdx")

            reviewIdRef.get().addOnCompleteListener(callback1)
        }

        // 리뷰를 DB에 추가하는 함수
        fun setReviewInfo(reviewModel : ReviewModel, callback1 : (Task<Void>) -> Unit){
            val database = FirebaseDatabase.getInstance()

            val productRef = database.getReference("ReviewData")
            productRef.push().setValue(reviewModel).addOnCompleteListener(callback1)
        }

        //상품 이미지들을 업로드하는 함수
        fun uploadImages(uploadUri : MutableList<Uri>, fileDir : String, callback1: (Task<UploadTask.TaskSnapshot>) -> Unit){
            val storage = FirebaseStorage.getInstance()

            val count = uploadUri.size
            for(idx in 0 until count){
                val fileName = fileDir + "${idx+1}.png"
                val imageRef = storage.reference.child(fileName)
                imageRef.putFile(uploadUri[idx]).addOnCompleteListener(callback1)
            }
        }

        //상품의 대표이미지만 가져오는 함수
        fun getProductFirstImage(fileDir:String, callback1: (Task<Uri>) -> Unit){
            val storage = FirebaseStorage.getInstance()
            val fileName = fileDir + "1.png"

            val imageRef = storage.reference.child(fileName)
            imageRef.downloadUrl.addOnCompleteListener(callback1)
        }
    }
}