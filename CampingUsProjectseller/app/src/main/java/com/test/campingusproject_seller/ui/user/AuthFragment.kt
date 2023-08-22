package com.test.campingusproject_seller.ui.user


import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.FragmentAuthBinding
import com.test.campingusproject_seller.dataclassmodel.UserModel
import com.test.campingusproject_seller.repository.UserInfoRepository
import com.test.campingusproject_seller.ui.main.MainActivity
import com.test.campingusproject_seller.viewmodel.AuthViewModel
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class AuthFragment : Fragment() {
    lateinit var fragmentAuthBinding: FragmentAuthBinding
    lateinit var mainActivity: MainActivity
    lateinit var authViewModel: AuthViewModel
    lateinit var authCodeDecoded: String
    lateinit var joinName: String
    lateinit var joinId: String
    lateinit var joinPw: String
    lateinit var joinPhoneNum: String

    //파이어베이스의 인증객체
    val auth = Firebase.auth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mainActivity = activity as MainActivity
        fragmentAuthBinding = FragmentAuthBinding.inflate(layoutInflater)



        authViewModel = ViewModelProvider(mainActivity)[AuthViewModel::class.java]

        authViewModel.run {
            authResult.observe(mainActivity) {
                if (it) {
                    //인증코드 발급시 인증코드 기입부분 VISIBLE
                    fragmentAuthBinding.ifSendCode.visibility = View.VISIBLE
                    fragmentAuthBinding.layoutInputAuthCode.visibility = View.VISIBLE
                } else {
                    Snackbar.make(
                        fragmentAuthBinding.root,
                        "인증번호 발급 실패 전화 번호를 다시 확인하세요!",
                        Snackbar.LENGTH_LONG
                    ).setAction("확인") {

                    }.show()
                }
            }
            verificationIdStr.observe(mainActivity) {
                authCodeDecoded = it
            }
        }

        //회원가입 창에서 가져온 데이터
        joinName = arguments?.getString("joinName").toString()
        joinId = arguments?.getString("joinId").toString()
        joinPw = arguments?.getString("joinPw").toString()


        //인증 정보를 문자로 받기전까지 인증코드 부분 GONE
        fragmentAuthBinding.ifSendCode.visibility = View.GONE
        fragmentAuthBinding.layoutInputAuthCode.visibility = View.GONE

        fragmentAuthBinding.run {
            //인증번호 받기 버튼 클릭 시
            buttonGetAuthNumber.setOnClickListener {
                val inputPhoneNum = editTextInputAuthPhoneNumber.text.toString()
                val authPhoneNum = phoneNumber82(inputPhoneNum)

                thread {
                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(authPhoneNum) // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(mainActivity) // Activity (for callback binding)
                        .setCallbacks(authViewModel.authCallback()) // OnVerificationStateChangedCallbacks
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                    //인증 언어를 한국으로 설정
                    auth.setLanguageCode("kr")
                }


                MaterialAlertDialogBuilder(
                    mainActivity,
                    R.style.ThemeOverlay_App_MaterialAlertDialog).run {
                    setTitle("인증번호 전송중..")
                    setMessage("인증번호 기입 부분 생성까지 잠시만 기다려주세요")
                    setPositiveButton("확인", null)
                    show()
                }
            }
//
            //인증 완료 클릭 시
            buttonAuthComplete.setOnClickListener {
                val inputAuthCode = editTextInputAuthNumber.text.toString()
                //PhoneAuthCredential 객체로 인증코드 분석
                val credential = PhoneAuthProvider.getCredential(authCodeDecoded, inputAuthCode)
                signInWithPhoneAuthCredential(credential)
            }
        }

        return fragmentAuthBinding.root
    }

    //전화번호 국가 코드에 맞게 인코딩
    fun phoneNumber82(msg: String): String {
        val firstNumber: String = msg.substring(0, 3)
        var phoneEdit = msg.substring(3)

        when (firstNumber) {
            "010" -> phoneEdit = "+8210$phoneEdit"
        }
        Log.d("국가코드로 변경된 번호 ", phoneEdit)
        return phoneEdit
    }

    //인증 코드가 맞는지 판단하는 함수
    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener(mainActivity) { task ->
            if (task.isSuccessful) {
                Log.d("testt", "signInWithCredential:success")
                joinPhoneNum = fragmentAuthBinding.editTextInputAuthPhoneNumber.text.toString()

                val userClass = UserModel(joinName, joinId, joinPw, joinPhoneNum)

                UserInfoRepository.checkDuplicationUser(userClass.userPhoneNumber,
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                MaterialAlertDialogBuilder(mainActivity, R.style.ThemeOverlay_App_MaterialAlertDialog).run {
                                    setTitle("회원가입 오류")
                                    setMessage("이미 가입한 번호입니다")
                                    setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                                        mainActivity.removeFragment(MainActivity.AUTH_FRAGMENT)
                                        mainActivity.removeFragment(MainActivity.JOIN_FRAGMENT)
                                    }
                                    show()
                                }
                            } else {
                                UserInfoRepository.addUserInfo(userClass) {
                                    MaterialAlertDialogBuilder(mainActivity, R.style.ThemeOverlay_App_MaterialAlertDialog).run {
                                        setTitle("회원가입 완료")
                                        setMessage("회원가입이 완료되었습니다 로그인 화면으로 넘어갑니다")
                                        setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                                            mainActivity.removeFragment(MainActivity.AUTH_FRAGMENT)
                                            mainActivity.removeFragment(MainActivity.JOIN_FRAGMENT)
                                        }
                                        show()
                                    }


                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })


            } else {

                Log.d("testt", "signInWithCredential:failure", task.exception)
                MaterialAlertDialogBuilder(mainActivity, R.style.ThemeOverlay_App_MaterialAlertDialog).run {
                    setTitle("인증번호 오류")
                    setMessage("인증번호 확인 후 다 다시 입력하세요!")
                    setPositiveButton("확인",null)
                    show()
                }
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                }
            }
        }
    }
}