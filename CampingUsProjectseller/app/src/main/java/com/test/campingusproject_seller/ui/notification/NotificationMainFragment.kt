package com.test.campingusproject_seller.ui.notification

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.FragmentNotificationMainBinding
import com.test.campingusproject_seller.ui.main.MainActivity

class NotificationMainFragment : Fragment() {
    lateinit var fragmentNotificationMainBinding: FragmentNotificationMainBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentNotificationMainBinding = FragmentNotificationMainBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        fragmentNotificationMainBinding.run {
            toolbarNotification.run {
                title = "알림"

                // 백버튼
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.NOTIFICATION_MAIN_FRAGMENT)
                }
            }

            // 뷰페이저
            viewPager2Notification.run {
                val adapter = ViewPagerAdapter(mainActivity)
                viewPager2Notification.adapter = adapter

                TabLayoutMediator(tabLayoutNotification, viewPager2Notification) { tab, position ->
                    when(position) {
                        0 -> tab.text = "결제"
                        1 -> tab.text = "문의"
                        2 -> tab.text = "리뷰"
                    }
                }.attach()
            }
        }

        return fragmentNotificationMainBinding.root
    }

    // 뷰페이저 어댑터
    inner class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
        val fragments: List<Fragment>
        init {
            fragments= listOf(NotificationPaymentFragment(), NotificationReviewFragment(), NotificationInquiryFragment())
        }

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            val resultFragment = when(position){
                0 -> NotificationPaymentFragment()
                1 -> NotificationInquiryFragment()
                2 -> NotificationReviewFragment()
                else -> NotificationPaymentFragment()
            }
            return resultFragment
        }
    }
}