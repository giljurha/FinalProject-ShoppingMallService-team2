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

    }
}