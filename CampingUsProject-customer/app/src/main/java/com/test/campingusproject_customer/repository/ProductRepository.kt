package com.test.campingusproject_customer.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
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

        //하나의 상품 정보 가져오는 함수
        fun getOneProductData(productId:Long, callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()

            val productRef = database.getReference("ProductData")
            productRef.orderByChild("productId").equalTo(productId.toDouble()).get().addOnCompleteListener(callback1)
        }

        //해당하는 상품 이미지 전부 가져오는 함수
        fun getProductImages(fileDir: String, callback1: (StorageReference) -> Unit){
            val storage = FirebaseStorage.getInstance()

            val filePath = fileDir.substring(0, fileDir.length-1)
            val fileDirRef = storage.reference.child(filePath)

            //listAll 메서드로 해당 디렉토리 하위에 있는 모든 항목을 순회
            fileDirRef.listAll()
                .addOnCompleteListener { task->
                    if(task.isSuccessful){
                        task.result.items.forEach {
                            callback1(it)
                        }
                    }
                }
        }
    }
}