package com.test.campingusproject_seller.ui.inquiry

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.FragmentInquiryDetailBinding
import com.test.campingusproject_seller.ui.main.MainActivity

class InquiryDetailFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentInquiryDetailBinding: FragmentInquiryDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mainActivity = activity as MainActivity
        fragmentInquiryDetailBinding = FragmentInquiryDetailBinding.inflate(layoutInflater)

        fragmentInquiryDetailBinding.run {

            // 문의 답변 등록 버튼
            buttonInquiryDetailAdd.run {
                setOnClickListener {
                    // 답변 내용을 가져온다.
                    val answer = textInputEditTextInquiryDetailAnswer.text.toString()

                    if (answer.isEmpty()) {
                        val builder = MaterialAlertDialogBuilder(mainActivity)
                        builder.setTitle("답변 입력 오류")
                        builder.setMessage("답변을 입력해주세요")
                        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                            // mainActivity.showSoftInput(textInputEditTextPostWriteSubject)
                        }
                        builder.show()
                        return@setOnClickListener
                    }


                }
            }
        }

        return fragmentInquiryDetailBinding.root
    }

}