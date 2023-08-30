package com.test.campingusproject_seller.ui.inquiry

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.FragmentInquiryDetailBinding
import com.test.campingusproject_seller.dataclassmodel.InquiryModel
import com.test.campingusproject_seller.repository.InquiryRepository
import com.test.campingusproject_seller.ui.main.MainActivity
import com.test.campingusproject_seller.viewmodel.InquiryViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InquiryDetailFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentInquiryDetailBinding: FragmentInquiryDetailBinding

    lateinit var inquiryViewModel: InquiryViewModel

    // 문의 목록 인덱스 번호를 받는다.
    var inquiryIdx = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mainActivity = activity as MainActivity
        fragmentInquiryDetailBinding = FragmentInquiryDetailBinding.inflate(layoutInflater)

        // 문의 인덱스 번호를 받는다.
        inquiryIdx = arguments?.getLong("inquiryIdx")!!

        inquiryViewModel = ViewModelProvider(mainActivity)[InquiryViewModel::class.java]
        inquiryViewModel.run {
            inquiryProductName.observe(mainActivity) {
                fragmentInquiryDetailBinding.textInputEditTextInquiryDetailTitle.setText(it)
            }
            inquiryUserId.observe(mainActivity) {
                fragmentInquiryDetailBinding.textInputEditTextInquiryDetailWriter.setText(it)
            }
            inquiryContent.observe(mainActivity) {
                fragmentInquiryDetailBinding.textInputEditTextInquiryDetailContent.setText(it)
            }
            inquiryAnswer.observe(mainActivity) {
                fragmentInquiryDetailBinding.textInputEditTextInquiryDetailAnswer.setText(it)
            }
        }

        fragmentInquiryDetailBinding.run {

            // 뒤로가기 아이콘
            topAppBarInquiryDetail.setNavigationOnClickListener {
                mainActivity.removeFragment(MainActivity.INQUIRY_DETAIL_FRAGMENT)
            }

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

//                    mainActivity.removeFragment(MainActivity.INQUIRY_DETAIL_FRAGMENT)

                    InquiryRepository.inquiryAnswer(inquiryIdx, answer, true) {
                        mainActivity.removeFragment(MainActivity.INQUIRY_DETAIL_FRAGMENT)
                    }

                }
            }
        }


        // 문의 정보를 가져온다.
        inquiryViewModel.setInquiryDetailData(inquiryIdx.toDouble())

        return fragmentInquiryDetailBinding.root
    }

}