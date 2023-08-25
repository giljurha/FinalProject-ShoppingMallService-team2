package com.test.campingusproject_customer.ui.review

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.AttributeSet
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentReviewBinding
import com.test.campingusproject_customer.ui.main.MainActivity

class ReviewFragment : Fragment() {
    lateinit var fragmentReviewBinding: FragmentReviewBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentReviewBinding = FragmentReviewBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        fragmentReviewBinding.run {
            toolbarReview.run {
                title = "상품 리뷰"

                // 백버튼
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.REVIEW_FRAGMENT)
                }
            }

            linearLayoutReview.run {
                setOnClickListener {
                    mainActivity.replaceFragment(MainActivity.REVIEW_DETAIL_FRAGMENT, true, true, null)
                }
            }

            linearLayoutReviewSeekBarGroup.run {
                seekBar1Review.run {
                }
                seekBar2Review.run {
                }
            }


        }

        return fragmentReviewBinding.root
    }
}