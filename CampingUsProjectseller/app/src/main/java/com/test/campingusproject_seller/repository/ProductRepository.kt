package com.test.campingusproject_seller.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.test.campingusproject_seller.dataclassmodel.ProductModel

class ProductRepository {
    companion object{

        fun getProductId(callback1 : (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()

            val productId = database.getReference("ProductId")
            productId.get().addOnCompleteListener(callback1)
        }

        fun setProductId(productId : Long, callback1 : (Task<Void>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val productIdRef = database.getReference("ProductId")

            productIdRef.get().addOnCompleteListener {
                it.result.ref.setValue(productId).addOnCompleteListener(callback1)
            }
        }

        fun addProductInfo(productModel : ProductModel, callback1 : (Task<Void>) -> Unit){
            val database = FirebaseDatabase.getInstance()

            val productRef = database.getReference("ProductData")
            productRef.push().setValue(productModel).addOnCompleteListener(callback1)
        }

        fun uploadImages(uploadUri : MutableList<Uri>, fileDir : String, callback1: (Task<UploadTask.TaskSnapshot>) -> Unit){
            val storage = FirebaseStorage.getInstance()

            val count = uploadUri.size
            for(idx in 0 until count){
                val fileName = fileDir + "${idx+1}.jpeg"
                val imageRef = storage.reference.child(fileName)
                imageRef.putFile(uploadUri[idx]).addOnCompleteListener(callback1)
            }
        }
    }
}