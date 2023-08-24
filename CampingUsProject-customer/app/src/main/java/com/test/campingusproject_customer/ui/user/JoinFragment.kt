package com.test.campingusproject_customer.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        fragmentJoinBinding.run {
            materialToolbarJoin.run {
                title = "회원가입"

                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.JOIN_FRAGMENT)
                }
            }

            buttonJoinSubmit.setOnClickListener {
                mainActivity.replaceFragment(MainActivity.AUTH_FRAGMENT, true, true, null)
            }
        }

        return fragmentJoinBinding.root
    }

}