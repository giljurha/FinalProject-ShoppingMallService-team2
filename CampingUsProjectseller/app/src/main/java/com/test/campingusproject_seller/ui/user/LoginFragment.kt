package com.test.campingusproject_seller.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.FragmentJoinBinding
import com.test.campingusproject_seller.databinding.FragmentLoginBinding
import com.test.campingusproject_seller.ui.main.MainActivity

class LoginFragment : Fragment() {
    lateinit var fragmentJoinBinding: FragmentLoginBinding
    lateinit var mainActivity: MainActivity
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,

        savedInstanceState: Bundle?,
    ): View? {

        mainActivity = activity as MainActivity
        fragmentJoinBinding = FragmentLoginBinding.inflate(layoutInflater)

        fragmentJoinBinding.run {
            //회원가입 버튼 클릭시
            buttonMoveToJoin.setOnClickListener {
                mainActivity.replaceFragment(MainActivity.JOIN_FRAGMENT, true, true, null)
            }
            //로그인 클릭시
            buttonBasicLogin.setOnClickListener {
                if (editTextInputLoginId.text.toString().isEmpty()) {
                    MaterialAlertDialogBuilder(
                        mainActivity,
                        R.style.ThemeOverlay_App_MaterialAlertDialog
                    ).run {
                        setTitle("로그인 오류")
                        setMessage("아이디를 입력해주세요!")
                        setPositiveButton("확인", null)
                        show()
                    }
                    return@setOnClickListener
                } else if (editTextInputLoginPassword.text.toString().isEmpty()) {
                    MaterialAlertDialogBuilder(
                        mainActivity,
                        R.style.ThemeOverlay_App_MaterialAlertDialog
                    ).run {
                        setTitle("로그인 오류")
                        setMessage("아이디를 입력해주세요!")
                        setPositiveButton("확인", null)
                        show()
                    }
                    return@setOnClickListener
                }
            }
        }
        return fragmentJoinBinding.root
    }
}