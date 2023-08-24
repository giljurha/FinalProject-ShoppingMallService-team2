package com.test.campingusproject_customer.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        fragmentAuthBinding.run {
            materialToolbarAuth.run {
                title = "본인 인증"

                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.AUTH_FRAGMENT)
                }
            }
        }

        return fragmentAuthBinding.root

    }

}