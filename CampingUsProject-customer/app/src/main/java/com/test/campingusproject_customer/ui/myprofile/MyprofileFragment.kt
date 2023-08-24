package com.test.campingusproject_customer.ui.myprofile

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.FragmentCampingBinding
import com.test.campingusproject_customer.databinding.FragmentMyprofileBinding
import com.test.campingusproject_customer.ui.main.MainActivity
import kotlin.concurrent.thread

class MyprofileFragment : Fragment() {
    lateinit var fragmentMyprofileBinding: FragmentMyprofileBinding
    lateinit var mainActivity: MainActivity
    lateinit var callback: OnBackPressedCallback
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity
        fragmentMyprofileBinding = FragmentMyprofileBinding.inflate(layoutInflater)

        fragmentMyprofileBinding.run {
            materialToolbarMyProfile.run {
                title = "내정보"
                //장바구니 가는 버튼
                setOnMenuItemClickListener {
                    title = "장바구니"
                    true
                }
            }
            textViewMyProfileMyName.text = "강현구"
            textViewMyProfileMyPhoneNumber.text = "010-1234-1234"
            textViewMyprofileMyDestination.text = "서울특별시 송파구 올림픽로 300 시그니엘 꼭대기층임ㅋ"

            //수정
            textViewMyProfileEditMyInfo.run {
                setOnClickListener {
                    mainActivity.replaceFragment(MainActivity.MODIFY_MYPROFILE_FRAGMENT,true,false,null)
                }
            }

            //로그아웃
            textViewMyProfileLogout.run {
                setOnClickListener {
                    //다이얼로그 생성을 위한 객체를 생성한다
                    val builder = MaterialAlertDialogBuilder(mainActivity,R.style.ThemeOverlay_App_MaterialAlertDialog)

                    builder.run {
                        setTitle("로그아웃")
                        setMessage("로그아웃 하시겠습니까?")
                        setPositiveButton("예") { dialogInterface: DialogInterface, i: Int ->
                            textViewMyProfileLogout.text = "로그아웃 됨"
                        }
                        setNegativeButton("아니오") { dialogInterface: DialogInterface, i: Int ->
                            textViewMyProfileLogout.text = "로그아웃 안됨"
                        }
                        show()
                    }
                }
            }

            //회원탈퇴
            textViewMyProfileSignout.run {
                setOnClickListener {
                    //다이얼로그 생성을 위한 객체를 생성한다
                    val builder = MaterialAlertDialogBuilder(mainActivity,R.style.ThemeOverlay_App_MaterialAlertDialog)

                    builder.run {
                        setTitle("회원탈퇴")
                        setMessage("회원탈퇴 하시겠습니까?")
                        setPositiveButton("예") { dialogInterface: DialogInterface, i: Int ->
                            textViewMyProfileSignout.text = "회원탈퇴 됨"
                        }
                        setNegativeButton("아니오") { dialogInterface: DialogInterface, i: Int ->
                            textViewMyProfileSignout.text = "회원탈퇴 안됨"
                        }
                        show()
                    }
                }
            }
        }

        return fragmentMyprofileBinding.root
    }

    //뒤로가기 버튼 눌렀을 때 동작할 코드 onDetech까지
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mainActivity.removeFragment(MainActivity.MYPROFILE_FRAGMENT)
                mainActivity.activityMainBinding.bottomNavigationViewMain.selectedItemId = R.id.menuItemHome
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}