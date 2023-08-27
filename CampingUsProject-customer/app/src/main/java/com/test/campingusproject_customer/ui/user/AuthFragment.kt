package com.test.campingusproject_customer.ui.user

import android.content.DialogInterface
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentAuthBinding
import com.test.campingusproject_customer.dataclassmodel.CustomerUserModel
import com.test.campingusproject_customer.repository.CustomerUserRepository
import com.test.campingusproject_customer.ui.main.MainActivity
import com.test.campingusproject_customer.viewmodel.CustomerUserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AuthFragment : Fragment() {

    lateinit var fragmentAuthBinding: FragmentAuthBinding
    lateinit var mainActivity: MainActivity

    lateinit var customerUserViewModel : CustomerUserViewModel

    private var job : Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentAuthBinding = FragmentAuthBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        //파이어베이스 인증 객체 생성
        val auth = Firebase.auth

        customerUserViewModel = ViewModelProvider(mainActivity)[CustomerUserViewModel::class.java]
        customerUserViewModel.run {
            verificationCompleted.observe(mainActivity){
                if(it){     //자동 인증 성공
                    Log.d("authLog", "자동 인증 성공")
                }
            }
            verificationFailed.observe(mainActivity){
                if(it){     //인증 실패
                    Log.d("authLog", "인증 실패")
                    Snackbar.make(mainActivity.activityMainBinding.root,"인증에 실패했습니다. 다시 시도해주세요.", Snackbar.LENGTH_SHORT).show()
                }
            }
            codeSent.observe(mainActivity){
                if(it){     //인증 성공
                    Log.d("authLog", "인증 성공 - 코드 전송됨")
                    Snackbar.make(mainActivity.activityMainBinding.root,"인증에 성공했습니다. 인증 번호를 입력해주세요.", Snackbar.LENGTH_SHORT).show()
                    fragmentAuthBinding.linearLayoutAuthCode.visibility = View.VISIBLE
                }
            }
        }

        //하단 nav bar 안보이게
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.GONE

        //회원가입 화면에서 넘겨준 bundle 객체 값을 받아옴
        val userName = arguments?.getString("userName")!!
        val userId = arguments?.getString("userId")!!
        val userPw = arguments?.getString("userPw")!!
        val userShipRecipient = arguments?.getString("userShipRecipient")!!
        val userShipContact = arguments?.getString("userShipContact")!!
        val userShipAddress = arguments?.getString("userShipAddress")!!

        fragmentAuthBinding.run {

            //인증번호 요청 성공 전까지 보이지 않게
            linearLayoutAuthCode.visibility = View.GONE

            materialToolbarAuth.run {
                title = "본인 인증"

                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.AUTH_FRAGMENT)
                }
            }

            buttonAuthRequestAuthNumber.setOnClickListener {
                val phoneNumber = editTextAuthPhoneNumber.text.toString()

                //휴대폰 번호 입력 검사
                if(editTextisEmptyCheck("입력 오류", "전화번호를 입력하세요", editTextAuthPhoneNumber)||
                        editTextLengthCheck("입력 오류", "유효하지 않은 전화번호입니다.", editTextAuthPhoneNumber)){
                    return@setOnClickListener
                }

                //휴대폰 번호 중복 검사
                CustomerUserRepository.getRegisteredPhoneNumber(phoneNumber, object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        //서버에 저장된 전화번호
                        if(snapshot.exists()){
                            Log.d("existNum", "중복된 번호")
                            createDialog("회원 정보 중복", "이미 가입된 번호입니다."){
                                editTextAuthPhoneNumber.setText("")
                                mainActivity.focusOnView(editTextAuthPhoneNumber)
                            }
                        }
                        //서버에 저장되지 않은 전화번호
                        else{
                            Log.d("existNum", "중복되지 않은 번호")
                            Snackbar.make(fragmentAuthBinding.root, "사용 가능한 전화번호입니다.", Snackbar.LENGTH_SHORT).show()

                            job = lifecycleScope.launch {
                            //인증 상태에 맞게 코드 처리하는 콜백 함수 생성
                                SystemClock.sleep(500)
                                val callbacks = CustomerUserRepository.phoneAuthCallback(
                                    onVerificationCompleted = {customerUserViewModel.verificationCompleted.value = true},
                                    onVerificationFailed = {customerUserViewModel.verificationFailed.value = true},
                                    onCodeSent = { verificationId ->
                                        customerUserViewModel.verificationId.value = verificationId
                                        customerUserViewModel.codeSent.value = true
                                    }
                                )

                                val phoneNumber = fragmentAuthBinding.editTextAuthPhoneNumber.text.toString()
                                //입력된 휴대폰 번호 포맷팅
                                val validPhoneNumber = phoneNumber.replaceFirst("0", "+82")

                                //해당 번호로 인증코드 전송하는 함수 호출
                                CustomerUserRepository.sendAuthCode(auth, validPhoneNumber, mainActivity, callbacks)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}

                })

            }

            buttonAuthSubmit.setOnClickListener {
                //휴대폰번호 및 인증번호 입력 검사
                if(editTextisEmptyCheck("입력 오류", "인증 번호를 입력하세요", editTextAuthNumber)||
                    editTextisEmptyCheck("입력 오류", "휴대폰 번호를 입력하세요", editTextAuthPhoneNumber)){
                    return@setOnClickListener
                }

                //가입처리
                val userInputCode = editTextAuthNumber.text.toString()
                val credential = PhoneAuthProvider.getCredential(customerUserViewModel.verificationId.value.toString(), userInputCode)
                CustomerUserRepository.signInWithPhoneAuthCredential(credential, auth){ task ->
                    if(task.isSuccessful){
                        Log.d("credentialTest", "signInWithCredential:success")

                        //사용자 객체 생성
                        val customerUser = CustomerUserModel(userName, userId, userPw, userShipRecipient,
                            userShipContact, userShipAddress, editTextAuthPhoneNumber.text.toString())

                        //사용자 데이터 서버에 저장
                        CustomerUserRepository.addCustomerUserInfo(customerUser){
                            createDialog("회원가입 완료", "캠핑어스의 회원이 되신 것을 환영합니다."){
                                mainActivity.removeFragment(MainActivity.AUTH_FRAGMENT)
                                mainActivity.removeFragment(MainActivity.JOIN_FRAGMENT)
                            }
                        }

                    }else{
                        Log.d("credentialTest", "signInWithCredential:failure")

                        //인증 실패 시 다이얼로그
                        createDialog("인증번호 오류", "유효하지 않은 인증번호 입니다. 다시 입력해주세요"){
                            mainActivity.focusOnView(editTextAuthNumber)
                        }
                    }
                }
            }
        }

        return fragmentAuthBinding.root

    }

    //editText 입력된 값 검사하는 함수
    fun editTextisEmptyCheck(title : String, message : String, editText: EditText) : Boolean {
        if(editText.text.toString().isEmpty()){
            //입력되지 않았으면 다이얼로그 표시
            createDialog(title, message){
                mainActivity.focusOnView(editText)
            }
            return true
        }
        else{
            return false
        }
    }

    //edittext 입력된 값 길이 검사하는 함수
    fun editTextLengthCheck(title:String, message: String, editText: EditText): Boolean{
        if(editText.text.length!=11){
            createDialog(title, message){
                mainActivity.focusOnView(editText)
            }
            return true
        }else{
            return false
        }
    }

    //다이얼로그 생성하는 함수
    fun createDialog(title : String, message : String, callback: () -> Unit){
        MaterialAlertDialogBuilder(mainActivity,R.style.ThemeOverlay_App_MaterialAlertDialog).run {
            setTitle(title)
            setMessage(message)
            setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                callback()
            }
            show()
        }
    }

    //화면 onstop 상태에서 코루틴 종료
    override fun onStop() {
        super.onStop()
        job?.cancel()
    }
}