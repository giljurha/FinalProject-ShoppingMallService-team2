package com.test.campingusproject_customer.ui.inquiry

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        val sharedPreferences =
            mainActivity.getSharedPreferences("customer_user_info", Context.MODE_PRIVATE)
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
                    var checkContents = 0 // 내용 체크

                    //내용 안썼을 경우
                    if (editTextTextInquiryContent.text!!.length == 0) {
                        checkContents = 0
                        //다이얼로그 띄움
                        val builder = MaterialAlertDialogBuilder(
                            mainActivity,
                            R.style.ThemeOverlay_App_MaterialAlertDialog
                        )
                        builder.run {
                            setTitle("내용 입력")
                            setMessage("게시글의 내용을 입력해주세요.")
                            setNegativeButton("닫기") { dialogInterface: DialogInterface, i: Int ->
                            }
                            show()
                        }
                    } else {
                        checkContents = 1
                    }

                    // 저장
                    if (checkContents == 1) {
                        var inquiryIdx: Long = 0L     // 문의 인덱스 번호
                        // 현재시간
                        val current = LocalDateTime.now()
                        val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")
                        var formatted = ""

                        InquiryRepository.getInquiryIdx {
                            inquiryIdx = it.result.value as? Long ?: 0L
                            // 문의 인덱스 증가
                            inquiryIdx++

                            // 작성일
                            formatted = current.format(formatter)

                            // 객체 생성
                            val inquiryData = InquiryModel(
                                inquiryIdx,
                                inquiryItemId,
                                inquiryUserId,
                                inquiryProductName,
                                editTextTextInquiryContent.text.toString(),
                                inquiryUserName,
                                formatted,
                                "답변이 작성되지 않았습니다.",
                                false,
                                inquiryProductImage
                            )

                            // 문의 저장
                            InquiryRepository.setInquiryData(inquiryData) {
                                InquiryRepository.setInquiryIdx(inquiryIdx) {
                                    Snackbar.make(
                                        fragmentInquiryBinding.root,
                                        "문의가 등록되었습니다.",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                    mainActivity.removeFragment(MainActivity.INQUIRY_FRAGMENT)
                                }
                            }

                        }


//                        Log.d("민우", inquiryData.toString())


                    }


                }
            }
        }

        return fragmentInquiryBinding.root
    }
}