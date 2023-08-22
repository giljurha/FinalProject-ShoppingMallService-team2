package com.test.campingusproject_seller.ui.user

import android.content.Intent
import android.content.res.Resources.Theme
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.FragmentJoinBinding
import com.test.campingusproject_seller.repository.UserInfoRepository
import com.test.campingusproject_seller.ui.main.MainActivity
import kotlin.concurrent.fixedRateTimer

class JoinFragment : Fragment() {
    lateinit var fragmentJoinBinding: FragmentJoinBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentJoinBinding = FragmentJoinBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        fragmentJoinBinding.run {
            //툴바 설정
            toolbarLogin.run {
                setNavigationIcon(R.drawable.arrow_back_24px)
                setNavigationOnClickListener {
                    //뒤로가기 버튼 눌렀을 때 로그인으로 이동
                }
            }

            //회원가입 버튼 눌렀을 때
            buttonJoin.setOnClickListener {
                if (editTextInputJoinName.text.toString().isEmpty()) {
                    MaterialAlertDialogBuilder(mainActivity,R.style.ThemeOverlay_App_MaterialAlertDialog).run {
                        setTitle("회원가입 오류")
                        setMessage("이름을 입력해주세요!")
                        setPositiveButton("확인", null)
                        show()
                    }
                    return@setOnClickListener
                }
                //아이디가 비어있을 때
                else if (editTextInputJoinId.text.toString().isEmpty()) {
                    MaterialAlertDialogBuilder(mainActivity,R.style.ThemeOverlay_App_MaterialAlertDialog).run {
                        setTitle("회원가입 오류")
                        setMessage("아이디를 입력해주세요!")
                        setPositiveButton("확인", null)
                        show()
                    }
                    return@setOnClickListener
                    //비밀번호를 입력하지 않았을 때
                } else if (editTextInputJoinPassword.text.toString().isEmpty()) {
                    MaterialAlertDialogBuilder(mainActivity,R.style.ThemeOverlay_App_MaterialAlertDialog).run {
                        setTitle("회원가입 오류")
                        setMessage("비밀번호를 입력해주세요!")
                        setPositiveButton("확인", null)
                        show()
                    }
                    return@setOnClickListener
                    //비밀번호화 확인이 일치하지 않을 때
                } else if (editTextInputJoinPassword.text.toString() != editTextInputJoinPasswordAgain.text.toString()) {
                    MaterialAlertDialogBuilder(mainActivity,R.style.ThemeOverlay_App_MaterialAlertDialog).run {

                        setTitle("회원가입 오류")
                        setMessage("비밀번호 일치 여부를 확인하세요")
                        setPositiveButton("확인", null)
                        show()
                    }
                    return@setOnClickListener
                }
                val joinName = editTextInputJoinName.text.toString()
                val joinId = editTextInputJoinId.text.toString()
                val joinPw = editTextInputJoinPassword.text.toString()
                val bundle=Bundle()
                bundle.putString("joinName",joinName)
                bundle.putString("joinId",joinId)
                bundle.putString("joinPw",joinPw)
                mainActivity.replaceFragment(MainActivity.AUTH_FRAGMENT,true,true,bundle)
            }
        }
        return fragmentJoinBinding.root
    }
}