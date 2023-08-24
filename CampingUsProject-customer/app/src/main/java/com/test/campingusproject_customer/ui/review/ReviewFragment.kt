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

        }

        return fragmentReviewBinding.root
    }
}