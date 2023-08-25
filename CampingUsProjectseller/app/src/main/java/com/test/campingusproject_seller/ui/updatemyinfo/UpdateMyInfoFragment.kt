package com.test.campingusproject_seller.ui.updatemyinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.campingusproject_seller.databinding.FragmentUpdateMyInfoBinding
import com.test.campingusproject_seller.ui.main.MainActivity


class UpdateMyInfoFragment : Fragment() {

    lateinit var fragmentUpdateMyInfoBinding: FragmentUpdateMyInfoBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentUpdateMyInfoBinding = FragmentUpdateMyInfoBinding.inflate(inflater,container,false)
        mainActivity = activity as MainActivity

        fragmentUpdateMyInfoBinding.run{

            materialToolbarMyInfoUpdate.run {

                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    //백버튼 클릭 시 화면 삭제
                    mainActivity.removeFragment(MainActivity.UPDATE_MY_INFO_FRAGMENT)

                }
            }

            buttonMyInfoUpdateFix.setOnClickListener {

            }

        }



        // Inflate the layout for this fragment
        return fragmentUpdateMyInfoBinding.root
    }



}