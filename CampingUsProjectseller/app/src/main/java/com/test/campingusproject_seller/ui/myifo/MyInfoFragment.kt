package com.test.campingusproject_seller.ui.myifo

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.FragmentMyInfoBinding


class MyInfoFragment : Fragment() {

    lateinit var fragmentMyInfo: FragmentMyInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        fragmentMyInfo = FragmentMyInfoBinding.inflate(inflater)
        fragmentMyInfo.run {
            textView7.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        }

        return fragmentMyInfo.root

        // return view 반황형 추가
    }


}