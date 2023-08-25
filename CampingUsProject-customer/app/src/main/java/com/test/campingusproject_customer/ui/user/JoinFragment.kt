package com.test.campingusproject_customer.ui.user

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentJoinBinding
import com.test.campingusproject_customer.ui.main.MainActivity

class JoinFragment : Fragment() {

    lateinit var fragmentJoinBinding: FragmentJoinBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentJoinBinding = FragmentJoinBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        //하단 nav bar 안보이게
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.GONE

        fragmentJoinBinding.run {
            materialToolbarJoin.run {
                title = "회원가입"

                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.JOIN_FRAGMENT)
                }
            }

            buttonJoinSubmit.setOnClickListener {

                val userName = textInputEditTextJoinName.text.toString()
                val userId = textInputEditTextJoinId.text.toString()
                val userPw = textInputEditTextJoinPw.text.toString()
                val userPwCheck = textInputEditTextJoinPwCheck.text.toString()
                val userShipRecipient = textInputEditTextJoinShipRecipient.text.toString()
                val userShipContact = textInputEditTextJoinShipContact.text.toString()
                val userShipAddress = textInputEditTextJoinShipAddress.text.toString()

                //입력 되지 않은 요소가 있다면 리스너 탈출
                if(textInputLayoutisEmptyCheck(textInputLayoutJoinName, textInputEditTextJoinName, "이름을 입력해주세요")||
                    textInputLayoutisEmptyCheck(textInputLayoutJoinId, textInputEditTextJoinId, "아이디를 입력해주세요")||
                    textInputLayoutisEmptyCheck(textInputLayoutJoinPw, textInputEditTextJoinPw, "비밀번호를 입력해주세요")||
                    textInputLayoutisEmptyCheck(textInputLayoutJoinPwCheck, textInputEditTextJoinPwCheck, "비밀번호 확인을 입력해주세요")||
                    textInputLayoutisEmptyCheck(textInputLayoutJoinShipRecipient, textInputEditTextJoinShipRecipient, "받는사람을 입력해주세요")||
                    textInputLayoutisEmptyCheck(textInputLayoutJoinShipContact, textInputEditTextJoinShipContact, "연락처를 입력하세요")||
                    textInputLayoutisEmptyCheck(textInputLayoutJoinShipAddress, textInputEditTextJoinShipAddress, "주소를 입력하세요"))
                {
                     return@setOnClickListener
                }

                //비밀번호 불일치
                if(userPw != userPwCheck){
                    //비밀번호, 비밀번호 확인 값 초기화
                    textInputEditTextJoinPw.setText("")
                    textInputEditTextJoinPwCheck.setText("")

                    //다이얼로그 생성
                    MaterialAlertDialogBuilder(mainActivity,R.style.ThemeOverlay_App_MaterialAlertDialog).run {
                        setTitle("비밀번호 오류")
                        setMessage("비밀번호가 일치하지 않습니다")
                        setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                            //비밀번호 textinput layout에 포커스
                            mainActivity.focusOnView(textInputEditTextJoinPw)
                        }
                        show()
                    }
                    return@setOnClickListener
                }

                //입력된 정보 본인인증 화면에 넘겨줌
                val newBundle = Bundle()
                newBundle.run {
                    putString("userName", userName)
                    putString("userId", userId)
                    putString("userPw", userPw)
                    putString("userPwCheck", userPwCheck)
                    putString("userShipRecipient", userShipRecipient)
                    putString("userShipContact", userShipContact)
                    putString("userShipAddress", userShipAddress)
                }
                mainActivity.replaceFragment(MainActivity.AUTH_FRAGMENT, true, true, newBundle)
            }
        }

        return fragmentJoinBinding.root
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

}