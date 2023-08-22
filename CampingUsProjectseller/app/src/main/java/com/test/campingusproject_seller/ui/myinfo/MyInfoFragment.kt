package com.test.campingusproject_seller.ui.myinfo

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.FragmentMyInfoBinding

class MyInfoFragment : Fragment() {
    lateinit var fragmentMyInfoBinding:FragmentMyInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentMyInfoBinding= FragmentMyInfoBinding.inflate(layoutInflater)

        fragmentMyInfoBinding.run {
            buttonLogout.paintFlags= Paint.UNDERLINE_TEXT_FLAG
            buttonUpdateMyInfo.paintFlags=Paint.UNDERLINE_TEXT_FLAG
        }
        return fragmentMyInfoBinding.root
    }
}