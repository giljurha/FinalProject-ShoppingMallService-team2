package com.test.campingusproject_seller.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.test.campingusproject_seller.dataclassmodel.ProductModel

class ProductRepository {
    companion object{

        fun getProductId(callback1 : (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()

            val productId = database.getReference("ProductId")
            productId.get().addOnCompleteListener(callback1)
        }

        fun setProductId(productId:Long, callback1: (Task<Void>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val productIdRef = database.getReference("ProductId")

            productIdRef.get().addOnCompleteListener {
                it.result.ref.setValue(productId).addOnCompleteListener(callback1)
            }
        }

        fun addProductInfo(productModel: ProductModel, callback1: (Task<Void>) -> Unit){
            val database = FirebaseDatabase.getInstance()

            val productRef = database.getReference("ProductData")
            productRef.push().setValue(productModel).addOnCompleteListener(callback1)
        }
    }
}