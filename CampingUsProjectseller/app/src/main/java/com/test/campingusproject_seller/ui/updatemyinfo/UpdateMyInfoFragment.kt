package com.test.campingusproject_seller.ui.updatemyinfo

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
            // 뒤로가기 아이콘
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
                    builder.setTitle("오류")
                    builder.setMessage("정보가 비어었습니다")
                    builder.setNeutralButton("닫기",null)
                    builder.show()

                } else if (inputPw != inputPwCheck){
                    builder.setTitle("오류")
                    builder.setMessage("비밀번호가 잘못되었습니다")
                    builder.setNeutralButton("닫기",null)
                    builder.show()

                } else {

                    val database = FirebaseDatabase.getInstance()
                    val db = database.getReference("SellerUsers")
                    // 입력된 값을 db에 저장
                    db.orderByChild("sellerUserId").equalTo(userId).addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (snapshot in dataSnapshot.children) {
                                snapshot.ref.child("sellerUserName").setValue(inputName)
                                snapshot.ref.child("sellerUserPassword").setValue(inputPw)

                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            // Failed to read value
                        }
                    })
                    // db 값 프리퍼런스 값에 초기화
                    db.orderByChild("sellerUserId").equalTo(userId).addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            lateinit var userName: String
                            lateinit var userIdPref: String
                            lateinit var userPassword: String
                            lateinit var userPhoneNumber: String

                            for (snapshot in dataSnapshot.children) {
                                userName = snapshot.child("sellerUserName").value as String
                                userIdPref = snapshot.child("sellerUserId").value as String
                                userPassword = snapshot.child("sellerUserPassword").value as String
                                userPhoneNumber = snapshot.child("sellerUserPhoneNumber").value as String
                            }
                            UserInfoRepository.savePre(mainActivity, UserModel(userName,userIdPref,userPassword,userPhoneNumber))

                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })

                }
                // 내정보 화면으로 이동
                mainActivity.replaceFragment(MainActivity.MY_INFO_FRAGMENT,false,false,null)
                }


            }

        return fragmentUpdateMyInfoBinding.root
    }
}