package com.test.campingusproject_customer.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

class OrderDetailRepository {
    companion object{
        //주문번호에 맞는 주문 정보를 불러오는 메서드
        fun getOrderInfoByOrdeNum(orderId:String,callback: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val customerOrderRef = database.getReference("OrderData")
            customerOrderRef.orderByChild("orderId").equalTo(orderId).get().addOnCompleteListener(callback)
        }

        //주문 번호에 해당하는 제품을 불러오는 메서드
        fun getOrderedProductByOrderNum(orderId: String,callback: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val customerOrderRef = database.getReference("OrderProductData")
            customerOrderRef.orderByChild("orderId").equalTo(orderId).get().addOnCompleteListener(callback)
        }

    }
}