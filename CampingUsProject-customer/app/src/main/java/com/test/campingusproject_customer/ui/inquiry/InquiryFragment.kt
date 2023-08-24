package com.test.campingusproject_customer.ui.inquiry

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentInquiryBinding
import com.test.campingusproject_customer.ui.main.MainActivity

class InquiryFragment : Fragment() {
    lateinit var fragmentInquiryBinding: FragmentInquiryBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentInquiryBinding = FragmentInquiryBinding.inflate(layoutInflater)

        fragmentInquiryBinding.run {

        }

        return fragmentInquiryBinding.root
    }
}