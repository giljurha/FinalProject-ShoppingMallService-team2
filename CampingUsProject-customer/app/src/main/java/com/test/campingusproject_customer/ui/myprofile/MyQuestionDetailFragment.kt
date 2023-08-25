package com.test.campingusproject_customer.ui.myprofile

import android.os.Bundle
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

        fragmentMyQuestionDetailBinding.run {
            textInputEditTextMyQuestionDetailProductName.setText("강현구의 바람숭숭 텐트")
            textInputEditTextMyQuestionDetailUserName.setText("차은우")
            textInputEditTextMyQuestionDetailAnswerContent.setText("텐트 문을 닫으세요....")
            textInputEditTextMyQuestionDetailQuestionContent.setText("바람이 너무 잘통해요 ㅜㅜㅜㅜㅠㅠㅠ")

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