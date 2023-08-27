package com.test.campingusproject_customer.repository

import android.app.Activity
import android.provider.ContactsContract.Data
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.test.campingusproject_customer.dataclassmodel.CustomerUserModel
import kotlinx.coroutines.Job
import java.lang.Exception
import java.util.concurrent.TimeUnit

class CustomerUserRepository() {
    companion object{
        //사용자 인증
        //인증 코드 전송하는 함수
        fun sendAuthCode(auth: FirebaseAuth, phoneNumber : String, activity:Activity, callbacks: OnVerificationStateChangedCallbacks){
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(activity) // Activity (for callback binding)
                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)

            auth.setLanguageCode("kr")
        }

        //인증 콜백 함수 생성해 반환하는 함수
        fun phoneAuthCallback(
            onVerificationCompleted: () -> Unit,
            onVerificationFailed: () -> Unit,
            onCodeSent: (String) -> Unit
        ) : PhoneAuthProvider.OnVerificationStateChangedCallbacks {
            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    //자동 인증이 완료된 경우
                    onVerificationCompleted()
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    //인증 실패
                    onVerificationFailed()
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken,
                ) {
                    //인증 코드가 전송되었을 때
                    onCodeSent(verificationId)
                }
            }
            return callbacks
        }

        //인증 코드 비교해 사용자 인증하는 함수
        fun signInWithPhoneAuthCredential(
            credential : PhoneAuthCredential,
            auth: FirebaseAuth,
            callback : (Task<AuthResult>) -> Unit
        ){
            auth.signInWithCredential(credential).addOnCompleteListener(callback)
        }

        //사용자 데이터 관리
        //서버에 데이터 저장하는 함수
        fun addCustomerUserInfo(customerUserModel: CustomerUserModel, callback: (Task<Void>) -> Unit){
            val database = FirebaseDatabase.getInstance()

            val customerUserRef = database.getReference("CustomerUsers")
            customerUserRef.push().setValue(customerUserModel).addOnCompleteListener(callback)
        }

        //서버에서 전화번호 찾는 함수
        fun getRegisteredPhoneNumber(
            customerUserPhoneNumber: String, listener: ValueEventListener
        ){
            val database = FirebaseDatabase.getInstance()

            val customerUserRef = database.getReference("CustomerUsers")
            customerUserRef.orderByChild("customerUserPhoneNumber").equalTo(customerUserPhoneNumber)
                .addValueEventListener(listener)
        }
    }
}