package com.test.campingusproject_customer.ui.user

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentAuthBinding
import com.test.campingusproject_customer.ui.main.MainActivity

class AuthFragment : Fragment() {

    lateinit var fragmentAuthBinding: FragmentAuthBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentAuthBinding = FragmentAuthBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        //하단 nav bar 안보이게
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.GONE

        //회원가입 화면에서 넘겨준 bundle 객체 값을 받아옴
        val userName = arguments?.getString("userName")
        val userId = arguments?.getString("userId")
        val userPw = arguments?.getString("userPw")
        val userShipRecipient = arguments?.getString("userShipRecipient")
        val userShipContact = arguments?.getString("userShipContact")
        val userShipAddress = arguments?.getString("userShipAddress")

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
                //휴대폰 번호 입력 검사
                if(editTextisEmptyCheck("입력 오류", "휴대폰 번호를 입력하세요", editTextAuthPhoneNumber)){
                    return@setOnClickListener
                }

                //휴대폰 번호 중복 검사

                //인증번호 불러오는 로직 작성

            }

            buttonAuthSubmit.setOnClickListener {
                //휴대폰번호 및 인증번호 입력 검사
                if(editTextisEmptyCheck("입력 오류", "인증 번호를 입력하세요", editTextAuthNumber)||
                    editTextisEmptyCheck("입력 오류", "휴대폰 번호를 입력하세요", editTextAuthPhoneNumber)){
                    return@setOnClickListener
                }

                //가입처리

            }
        }

        return fragmentAuthBinding.root

    }

    //editText 입력된 값 검사하는 함수
    fun editTextisEmptyCheck(title : String, message : String, editText: EditText) : Boolean {
        if(editText.text.toString().isEmpty()){
            //입력되지 않았으면 다이얼로그 표시
            createDialog(title, message, editText)
            return true
        }
        else{
            return false
        }
    }

    //다이얼로그 생성하는 함수
    fun createDialog(title : String, message : String, editText: EditText){
        MaterialAlertDialogBuilder(mainActivity,R.style.ThemeOverlay_App_MaterialAlertDialog).run {
            setTitle(title)
            setMessage(message)
            setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                mainActivity.focusOnView(editText)
            }
            show()
        }
    }
}