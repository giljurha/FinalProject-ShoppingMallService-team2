package com.test.campingusproject_seller.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.test.campingusproject_seller.dataclassmodel.UserModel
import com.test.campingusproject_seller.repository.UserInfoRepository

class AuthViewModel : ViewModel() {
    val authResult = MutableLiveData<Boolean>()
    val verificationIdStr = MutableLiveData<String>()

    fun authCallback(): PhoneAuthProvider.OnVerificationStateChangedCallbacks {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                //자동로그인 할 때 사용하는 콜백함수
                Log.d("testt", "zzonVerificationCompleted:$credential")
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w("testt", "onVerificationFailed", e)
                authResult.value = false

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                    // reCAPTCHA verification attempted with null Activity
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                authResult.value = true
                Log.d("testt", "onCodeSent:$verificationId")
                Log.d("testt", verificationId)

                //인증 코드 아이디 저장
                verificationIdStr.value = verificationId
            }
        }
        return callbacks
    }

    fun join(userName:String,userId:String,userPw:String,userPhoneNum:String) {
        val userClass=UserModel(userName,userId,userPw,userPhoneNum)
        //저장할 사용자 인덱스 가져오기
        UserInfoRepository.addUserInfo(userClass) {

        }
    }
}