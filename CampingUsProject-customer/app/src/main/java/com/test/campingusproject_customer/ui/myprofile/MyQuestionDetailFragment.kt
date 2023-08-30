package com.test.campingusproject_customer.ui.myprofile

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentMyQuestionDetailBinding
import com.test.campingusproject_customer.databinding.RowMyQuestionsBinding
import com.test.campingusproject_customer.ui.main.MainActivity

class MyQuestionDetailFragment : Fragment() {

    lateinit var fragmentMyQuestionDetailBinding: FragmentMyQuestionDetailBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentMyQuestionDetailBinding = FragmentMyQuestionDetailBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        val inquiryProduct = arguments?.getString("inquiryProduct")
        val inquiryContent = arguments?.getString("inquiryContent")
        val inquiryAnswer = arguments?.getString("inquiryAnswer")
        val inquiryDate = arguments?.getString("inquiryDate")

        Log.d("test", "${inquiryAnswer}")

        fragmentMyQuestionDetailBinding.run {
            textInputEditTextMyQuestionDetailProductName.setText(inquiryProduct)
            textInputEditTextMyQuestionDetailDate.setText(inquiryDate)
            textInputEditTextMyQuestionDetailAnswerContent.setText(inquiryAnswer)
            textInputEditTextMyQuestionDetailQuestionContent.setText(inquiryContent)

            materialToolbarMyQuestionDetail.run {
                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.MY_QUESTION_DETAIL_FRAGMENT)
                }
            }
        }

        return fragmentMyQuestionDetailBinding.root
    }

}