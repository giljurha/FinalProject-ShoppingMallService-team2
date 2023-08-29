package com.test.campingusproject_customer.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.test.campingusproject_customer.dataclassmodel.OrderModel
import com.test.campingusproject_customer.dataclassmodel.OrderProductModel

class OrderRepository {
    companion object{
        //Order Model 저장
        fun addOrderInfo(orderModel: OrderModel, callback : (Task<Void>) -> Unit){
            val database  = FirebaseDatabase.getInstance()

            val orderRef = database.getReference("OrderData")
            orderRef.push().setValue(orderModel).addOnCompleteListener(callback)
        }

        //Order Product 저장
        fun addOrderProductInfo(orderProductModel: OrderProductModel, callback : (Task<Void>) -> Unit){
            val database = FirebaseDatabase.getInstance()

            val orderProductRef = database.getReference("OrderProductData")
            orderProductRef.push().setValue(orderProductModel).addOnCompleteListener(callback)
        }

        //해당하는 ID의 상품 정보 가져오는 함수
        fun getOneProductData(productId:Long, callback1: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()

            val productRef = database.getReference("ProductData")
            productRef.orderByChild("productId").equalTo(productId.toDouble()).get().addOnCompleteListener(callback1)
        }

    }
}