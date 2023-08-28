package com.test.campingusproject_customer.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.test.campingusproject_customer.dataclassmodel.ProductModel

class ProductRepository {
    companion object {
        // 모든 상품 정보 가져오는 함수
        fun getAllProductData(callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()

            val productRef = database.getReference("ProductData")
            productRef.orderByChild("productId").get().addOnCompleteListener(callback1)
        }

        // 상품의 대표 이미지만 가져오는 함수
        fun getProductFirstImage(fileDir:String, callback1: (Task<Uri>) -> Unit){
            val storage = FirebaseStorage.getInstance()
            val fileName = fileDir + "1.png"

            val imageRef = storage.reference.child(fileName)
            imageRef.downloadUrl.addOnCompleteListener(callback1)
        }

        // 좋아요 버튼 클릭 시 +1 하는 함수
        fun likeButtonClicked(productId: Long, productRecommendationCount: Long, callback1: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val productRef = database.getReference("ProductData")

            productRef.orderByChild("productId").equalTo(productId.toDouble()).get().addOnCompleteListener {
                for(a1 in it.result.children){
                    a1.ref.child("productRecommendationCount").setValue(productRecommendationCount + 1).addOnCompleteListener(callback1)
                }
            }
        }
    }
}