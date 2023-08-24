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
        mainActivity = activity as MainActivity

        fragmentInquiryBinding.run {
            toolbarInquiry.run {
                title = "상품 문의 등록"

                // 백버튼
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.INQUIRY_FRAGMENT)
                }
            }
        }

        return fragmentInquiryBinding.root
    }
}