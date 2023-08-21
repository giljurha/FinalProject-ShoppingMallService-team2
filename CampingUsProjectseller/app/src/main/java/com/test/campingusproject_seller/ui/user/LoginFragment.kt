package com.test.campingusproject_seller.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        mainActivity=activity as MainActivity
        fragmentJoinBinding= FragmentLoginBinding.inflate(layoutInflater)

        fragmentJoinBinding.run {
            buttonMoveToJoin.setOnClickListener {
                mainActivity.replaceFragment(MainActivity.JOIN_FRAGMENT,true,true,null)
            }
        }
        return fragmentJoinBinding.root
    }
}