package com.test.campingusproject_customer.ui.inquiry

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentInquiryBinding
import com.test.campingusproject_customer.dataclassmodel.InquiryModel
import com.test.campingusproject_customer.repository.InquiryRepository
import com.test.campingusproject_customer.ui.main.MainActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class InquiryFragment : Fragment() {
    lateinit var fragmentInquiryBinding: FragmentInquiryBinding
    lateinit var mainActivity: MainActivity

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentInquiryBinding = FragmentInquiryBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        // SharedPreferences 사용
        val sharedPreferences = mainActivity.getSharedPreferences("customer_user_info", Context.MODE_PRIVATE)
        val inquiryUserId = sharedPreferences.getString("customerUserId", null)!!
        val inquiryUserName = sharedPreferences.getString("customerUserName", null)!!

        // 번들 객체 정보 가져오기
        val inquiryItemId = arguments?.getLong("productId")!!
        val inquiryProductName = arguments?.getString("productName")!!
        val inquiryProductImage = arguments?.getString("productImage")!!


        fragmentInquiryBinding.run {
            // 상품 이름
            textViewInquiryProductName.text = inquiryProductName

            toolbarInquiry.run {
                title = "상품 문의 등록"

                // 백버튼
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    mainActivity.removeFragment(MainActivity.INQUIRY_FRAGMENT)
                }
            }

            buttonInquirySubmit.run {
                setOnClickListener {
                    // 현재시간
                    val current = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")
                    val formatted = current.format(formatter)

                    val inquiryData = InquiryModel(
                        inquiryItemId, inquiryUserId, inquiryProductName,
                        editTextTextInquiryContent.text.toString(), inquiryUserName, formatted, "답변이 작성되지 않았습니다.", false, inquiryProductImage)

                    Log.d("민우", inquiryData.toString())

                    InquiryRepository.setInquiryData(inquiryData) {
                        Snackbar.make(fragmentInquiryBinding.root, "문의가 등록되었습니다.", Snackbar.LENGTH_SHORT).show()
                        mainActivity.removeFragment(MainActivity.INQUIRY_FRAGMENT)
                    }
                }
            }
        }

        return fragmentInquiryBinding.root
    }
}