package com.test.campingusproject_seller.ui.updatemyinfo

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.FragmentUpdateMyInfoBinding
import com.test.campingusproject_seller.dataclassmodel.UserModel
import com.test.campingusproject_seller.repository.UserInfoRepository
import com.test.campingusproject_seller.ui.main.MainActivity


class UpdateMyInfoFragment : Fragment() {

    lateinit var fragmentUpdateMyInfoBinding: FragmentUpdateMyInfoBinding
    lateinit var mainActivity: MainActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentUpdateMyInfoBinding = FragmentUpdateMyInfoBinding.inflate(inflater,container,false)
        mainActivity = activity as MainActivity

        fragmentUpdateMyInfoBinding.run{

            materialToolbarMyInfoUpdate.run {

                setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
                setNavigationOnClickListener {
                    //백버튼 클릭 시 화면 삭제
                    mainActivity.removeFragment(MainActivity.UPDATE_MY_INFO_FRAGMENT)

                }
            }


            // 회원정보 수정완료 버튼
            buttonMyInfoUpdateFix.setOnClickListener {
                val inputName = textInputEditTextMyInfoUpdateUserName.text.toString()
                val inputPw = textInputEditTextMyInfoUpdatePw.text.toString()
                val inputPwCheck = textInputEditTextMyInfoUpdatePwCheck.text.toString()
                val pref = mainActivity.getSharedPreferences("user_info", Context.MODE_PRIVATE)
                val userId = pref.getString("userId",null)
                val builder = MaterialAlertDialogBuilder(mainActivity, R.style.ThemeOverlay_App_MaterialAlertDialog)

                if(inputName.isEmpty() || inputPw.isEmpty() || inputPwCheck.isEmpty()){
                    builder.setMessage("정보가 비어었습니다")
                    builder.setNeutralButton("닫기",null)
                    builder.show()

                } else if (inputPw != inputPwCheck){
                    builder.setMessage("비밀번호가 잘못되었습니다")
                    builder.setNeutralButton("닫기",null)
                    builder.show()

                } else {

                    val database = FirebaseDatabase.getInstance()
                    val db = database.getReference("SellerUsers")

                    db.orderByChild("sellerUserId").equalTo(userId).get().addOnCompleteListener{
                        db.setValue(mapOf(
                            "sellerUserName" to inputName,
                            "sellerUserPassword" to inputPw
                        )
                        )
                    }

                    val userName = db.child("sellerUserName").toString()
                    val userId = db.child("sellerUserId").toString()
                    val userPassword = db.child("sellerUserPassword").toString()
                    val userPhoneNumber = db.child("sellerUserPhoneNumber").toString()

                    UserInfoRepository.savePre(mainActivity, UserModel(userName,userId,userPassword,userPhoneNumber))
                    mainActivity.replaceFragment(MainActivity.LOGIN_FRAGMENT,false,false,null)

                }
                // 1번 널값 확인
                // 2번 비밀번호 일치
                // 3번 데이터베이스 자료값 확인
                // 4번 로컬 개인정보 변경
                // 5번 내정보 화면으로 프래그먼트 전환

            }

        }

        // Inflate the layout for this fragment
        return fragmentUpdateMyInfoBinding.root
    }



}