package com.test.campingusproject_seller.ui.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.FragmentNotificationInquiryBinding

class NotificationInquiryFragment : Fragment() {
    lateinit var fragmentNotificationInquiryBinding: FragmentNotificationInquiryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentNotificationInquiryBinding = FragmentNotificationInquiryBinding.inflate(layoutInflater)

        return fragmentNotificationInquiryBinding.root
    }
}