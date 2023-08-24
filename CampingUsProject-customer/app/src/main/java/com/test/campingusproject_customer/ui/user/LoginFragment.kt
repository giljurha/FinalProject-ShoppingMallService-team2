package com.test.campingusproject_customer.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentLoginBinding
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
        }

        return fragmentLoginBinding.root
    }

}