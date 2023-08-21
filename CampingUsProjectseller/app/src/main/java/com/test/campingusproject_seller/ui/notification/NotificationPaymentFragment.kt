package com.test.campingusproject_seller.ui.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.FragmentNotificationPaymentBinding

class NotificationPaymentFragment : Fragment() {
    lateinit var fragmentNotificationPaymentBinding: FragmentNotificationPaymentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentNotificationPaymentBinding = FragmentNotificationPaymentBinding.inflate(layoutInflater)

        return fragmentNotificationPaymentBinding.root
    }
}