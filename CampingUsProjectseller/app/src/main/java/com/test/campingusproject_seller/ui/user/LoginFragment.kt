package com.test.campingusproject_seller.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.FragmentJoinBinding
import com.test.campingusproject_seller.databinding.FragmentLoginBinding
import com.test.campingusproject_seller.dataclassmodel.UserModel
import com.test.campingusproject_seller.repository.UserInfoRepository
import com.test.campingusproject_seller.ui.main.MainActivity

class LoginFragment : Fragment() {
    lateinit var fragmentJoinBinding: FragmentLoginBinding
    lateinit var mainActivity: MainActivity
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,

        savedInstanceState: Bundle?,
    ): View? {

        mainActivity = activity as MainActivity
        fragmentJoinBinding = FragmentLoginBinding.inflate(layoutInflater)

        fragmentJoinBinding.run {
            //회원가입 버튼 클릭시
            buttonMoveToJoin.setOnClickListener {
                mainActivity.replaceFragment(MainActivity.JOIN_FRAGMENT, true, true, null)
            }
            //로그인 클릭시
            buttonBasicLogin.setOnClickListener {
                if (editTextInputLoginId.text.toString().isEmpty()) {
                    MaterialAlertDialogBuilder(
                        mainActivity,
                        R.style.ThemeOverlay_App_MaterialAlertDialog
                    ).run {
                        setTitle("로그인 오류")
                        setMessage("아이디를 입력해주세요!")
                        setPositiveButton("확인", null)
                        show()
                    }
                    return@setOnClickListener
                } else if (editTextInputLoginPassword.text.toString().isEmpty()) {
                    MaterialAlertDialogBuilder(
                        mainActivity,
                        R.style.ThemeOverlay_App_MaterialAlertDialog
                    ).run {
                        setTitle("로그인 오류")
                        setMessage("아이디를 입력해주세요!")
                        setPositiveButton("확인", null)
                        show()
                    }
                    return@setOnClickListener
                }
                val userId = editTextInputLoginId.text.toString()
                val userPw = editTextInputLoginPassword.text.toString()
                UserInfoRepository.getUserInfoByUserId(userId) {
                    if (it.result.exists() == false) {
                        MaterialAlertDialogBuilder(
                            mainActivity,
                            R.style.ThemeOverlay_App_MaterialAlertDialog
                        ).run {
                            setTitle("로그인 오류")
                            setMessage("존재하지 않는 아이디입니다!")
                            setPositiveButton("확인", null)
                            show()
                        }
                        return@getUserInfoByUserId
                    } else {
                        for (c1 in it.result.children) {
                            val databaseUserPw = c1.child("sellerUserPassword").value as String

                            //입력한 비번과 현재 계정의 비번이 다르다면
                            if (userPw != databaseUserPw) {
                                MaterialAlertDialogBuilder(
                                    mainActivity,
                                    R.style.ThemeOverlay_App_MaterialAlertDialog
                                ).run {
                                    setTitle("로그인 오류")
                                    setMessage("비밀번호를 확인하세요!")
                                    setPositiveButton("확인", null)
                                    show()
                                }
                            } //입력한 비번과 현재 계정의 비번이 같다면
                            else {
                                val userName = c1.child("sellerUserName").value as String
                                val userId = c1.child("sellerUserId").value as String
                                val userPassword = c1.child("sellerUserPassword").value as String
                                val userPhoneNumber = c1.child("sellerUserPhoneNumber").value as String

                                UserInfoRepository.savePre(mainActivity, UserModel(userName,userId,userPassword,userPhoneNumber))
                                Snackbar.make(fragmentJoinBinding.root, "로그인 되었습니다", Snackbar.LENGTH_SHORT).show()
                                mainActivity.replaceFragment(MainActivity.SELL_STATE_FRAGMENT,false,true,null)
                                //메인 화면의 네비게이션 visible=true
                                mainActivity.activityMainBinding.bottomNavigationViewMain.visibility=View.VISIBLE
                            }
                        }
                    }
                }
            }
        }
        return fragmentJoinBinding.root
    }
}