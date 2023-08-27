package com.test.campingusproject_customer.ui.user

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentLoginBinding
import com.test.campingusproject_customer.dataclassmodel.CustomerUserModel
import com.test.campingusproject_customer.repository.CustomerUserRepository
import com.test.campingusproject_customer.ui.main.MainActivity

class LoginFragment : Fragment() {

    lateinit var fragmentLoginBinding: FragmentLoginBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentLoginBinding = FragmentLoginBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        fragmentLoginBinding.run {
            materialToolbarLogin.run {
                title = "로그인"

                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                //백버튼 클릭 시 화면 삭제
                    mainActivity.removeFragment(MainActivity.LOGIN_FRAGMENT)
                }
            }

            textViewLoginGoToJoin.setOnClickListener {
                //회원가입 화면으로 전환
                mainActivity.replaceFragment(MainActivity.JOIN_FRAGMENT, true, true, null)
            }

            buttonLoginSubmit.setOnClickListener {

                //입력 요소 검사
                if(textInputLayoutisEmptyCheck(textInputLayoutLoginId, textInputEditTextLoginId, "아이디를 입력해주세요")||
                    textInputLayoutisEmptyCheck(textInputLayoutLoginPw, textInputEditTextLoginPw, "비밀번호를 입력해주세요")){

                    return@setOnClickListener
                }

                //서버에 저장된 유저 데이터로 로그인 가능 여부 검사
                CustomerUserRepository.getRegisteredID(textInputEditTextLoginId.text.toString()){
                    val inputId = textInputEditTextLoginId.text.toString()
                    val inputPw = textInputEditTextLoginPw.text.toString()

                    //아이디 존재 여부에 따른 분기
                    if(it.result.exists()){
                        Log.d("existId", "존재하는 ID")

                        for(c1 in it.result.children){
                            val customerUserName = c1.child("customerUserName").value as String
                            val cusomerUserId = c1.child("customerUserId").value as String
                            val customerUserPw = c1.child("customerUserPw").value as String
                            val customerUserShipRecipient = c1.child("customerUserShipRecipient").value as String
                            val customerUserShipContact = c1.child("customerUserShipContact").value as String
                            val customerUserShipAddress = c1.child("customerUserShipAddress").value as String
                            val customerUserPhoneNumber = c1.child("customerUserPhoneNumber").value as String
                            val customerUserProfileImage = c1.child("customerUserProfileImage").value as String

                            //비밀번호 검사
                            if(customerUserPw != inputPw){
                                createDialog("비밀번호 오류", "비밀번호가 일치하지 않습니다."){
                                    textInputEditTextLoginPw.setText("")
                                }
                            }
                            else{
                                //현재 로그인 된 사용자 정보를 sharedPreferences에 저장
                                val customerUser = CustomerUserModel(customerUserName, cusomerUserId, customerUserPw, customerUserShipRecipient,
                                    customerUserShipContact, customerUserShipAddress, customerUserPhoneNumber, customerUserProfileImage)

                                val sharedPreferences = mainActivity.getSharedPreferences("customer_user_info", Context.MODE_PRIVATE)
                                CustomerUserRepository.saveUserInfo(sharedPreferences, customerUser)

                                Snackbar.make(fragmentLoginBinding.root, "로그인 되었습니다.", Snackbar.LENGTH_SHORT).show()
                                mainActivity.removeFragment(MainActivity.LOGIN_FRAGMENT)
                            }

                        }
                    }else{
                        Log.d("existId", "존재하지 않는 ID")

                        createDialog("아이디 오류", "존재하지 않는 아이디입니다."){
                            textInputEditTextLoginId.setText("")
                        }
                    }
                }
            }
        }

        return fragmentLoginBinding.root
    }

    //textInputLayout 오류 표시 함수
    fun textInputLayoutEmptyError(textInputLayout: TextInputLayout, errorMessage : String){
        textInputLayout.run {
            error = errorMessage
            setErrorIconDrawable(R.drawable.error_24px)
            requestFocus()
        }
    }

    //textInputLayout 입력 검사 함수
    fun textInputLayoutisEmptyCheck(
        textInputLayout: TextInputLayout,
        textInputEditText: TextInputEditText,
        errorMessage: String) : Boolean
    {
        if(textInputEditText.text.toString().isEmpty()){
            //입력되지 않았으면 오류 표시
            textInputLayoutEmptyError(textInputLayout, errorMessage)
            mainActivity.focusOnView(textInputEditText)
            return true
        }
        else{
            textInputLayout.error = null
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

}