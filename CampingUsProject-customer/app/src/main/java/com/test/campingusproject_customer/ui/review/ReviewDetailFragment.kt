package com.test.campingusproject_customer.ui.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentReviewBinding
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

        }

        return fragmentReviewDetailBinding.root
    }
}