package com.test.campingusproject_customer.ui.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentReviewDetailBinding
import com.test.campingusproject_customer.ui.main.MainActivity

class ReviewDetailFragment : Fragment() {
    lateinit var fragmentReviewDetailBinding: FragmentReviewDetailBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentReviewDetailBinding = FragmentReviewDetailBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        fragmentReviewDetailBinding.run {
            materialToolbarReviewDetail.run {
                title = "상품 리뷰"

                // 백버튼
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.REVIEW_FRAGMENT)
                }
            }
        }

        return fragmentReviewDetailBinding.root
    }
}