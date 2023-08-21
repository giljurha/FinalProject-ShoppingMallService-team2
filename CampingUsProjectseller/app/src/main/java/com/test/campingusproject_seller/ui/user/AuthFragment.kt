package com.test.campingusproject_seller.ui.user


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.test.campingusproject_seller.databinding.FragmentAuthBinding
import com.test.campingusproject_seller.ui.main.MainActivity
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class AuthFragment : Fragment() {
    lateinit var fragmentAuthBinding: FragmentAuthBinding
    lateinit var mainActivity: MainActivity
    //파이어베이스의 인증객체
    val auth=Firebase.auth
    //인증번호 확인에 사용되는 코드
    lateinit var verificationIdStr:String
    var token=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mainActivity=activity as MainActivity
        fragmentAuthBinding = FragmentAuthBinding.inflate(layoutInflater)
        auth.setLanguageCode("kr")

        //회원가입 창에서 가져온 데이터
        val joinName = arguments?.getString("joinName")
        val joinId = arguments?.getString("joinId")
        val joinPw = arguments?.getString("joinPw")

        fragmentAuthBinding.ifSendCode.visibility=View.GONE
        fragmentAuthBinding.layoutInputAuthCode.visibility=View.GONE
        fragmentAuthBinding.run {
            //인증번호 받기 버튼 클릭 시
            buttonGetAuthNumber.setOnClickListener {
                //인증번호 콜백부분
                val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        //자동로그인 할 때 사용하는 콜백함수
                        Log.d("testt", "zzonVerificationCompleted:$credential")
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        // This callback is invoked in an invalid request for verification is made,
                        // for instance if the the phone number format is not valid.
                        Log.w("testt", "onVerificationFailed", e)

                        if (e is FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                        } else if (e is FirebaseTooManyRequestsException) {
                            // The SMS quota for the project has been exceeded
                        } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                            // reCAPTCHA verification attempted with null Activity
                        }

                        // Show a message and update the UI
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken,
                    ) {
                        fragmentAuthBinding.ifSendCode.visibility=View.VISIBLE
                        fragmentAuthBinding.layoutInputAuthCode.visibility=View.VISIBLE
                        Log.d("testt", "onCodeSent:$verificationId")
                        Log.d("testt",verificationId)

                        // Save verification ID and resending token so we can use them later
                        verificationIdStr = verificationId
//                token = token
                    }
                }
                //번호 인코딩
                val inputPhoneNum=editTextInputAuthPhoneNumber.text.toString()
                val authPhoneNum=phoneNumber82(inputPhoneNum)

                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(authPhoneNum) // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(mainActivity) // Activity (for callback binding)
                        .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
            }

            //인증완료 클릭 시
            buttonAuthComplete.setOnClickListener {
                val inputAuthCode=editTextInputAuthNumber.text.toString()
                val credential = PhoneAuthProvider.getCredential(verificationIdStr, inputAuthCode)
                signInWithPhoneAuthCredential(credential)
            }
        }



        return fragmentAuthBinding.root
    }

    fun phoneNumber82(msg : String) : String{
        val firstNumber : String = msg.substring(0,3)
        var phoneEdit = msg.substring(3)

        when(firstNumber){
            "010" -> phoneEdit = "+8210$phoneEdit"
            "011" -> phoneEdit = "+8211$phoneEdit"
            "016" -> phoneEdit = "+8216$phoneEdit"
            "017" -> phoneEdit = "+8217$phoneEdit"
            "018" -> phoneEdit = "+8218$phoneEdit"
            "019" -> phoneEdit = "+8219$phoneEdit"
            "106" -> phoneEdit = "+82106$phoneEdit"
        }
        Log.d("국가코드로 변경된 번호 ",phoneEdit)
        return phoneEdit
    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(mainActivity) { task ->
                if (task.isSuccessful) {

                    Log.d("testt", "signInWithCredential:success")
                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d("testt", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }
}