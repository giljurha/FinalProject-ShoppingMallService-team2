package com.test.campingusproject_customer.ui.myprofile

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
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
import com.test.campingusproject_customer.databinding.FragmentMyprofileBinding
import com.test.campingusproject_customer.repository.CustomerUserRepository
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

        val sharedPreferences = mainActivity.getSharedPreferences("customer_user_info", Context.MODE_PRIVATE)

        loginStatusCheck(sharedPreferences)

        //하단 nav bar 보이게
        mainActivity.activityMainBinding.bottomNavigationViewMain.visibility = View.VISIBLE

        fragmentMyprofileBinding.run {

            materialToolbarMyProfile.run {
                title = "내정보"
                //장바구니 가는 버튼
                setOnMenuItemClickListener {
                    mainActivity.replaceFragment(MainActivity.CART_FRAGMENT, true, true, null)
                    true
                }
            }

            //내가 쓴 글
            textViewMyProfileMyPost.setOnClickListener {
                val userId =  sharedPreferences.getString("customerUserId", null).toString()
                val newBundle = Bundle()
                newBundle.putString("userId",userId)
                mainActivity.replaceFragment(MainActivity.MY_POST_LIST_FRAGMENT,true,true,newBundle)
            }

            //수정
            textViewMyProfileEditMyInfo.run {
                setOnClickListener {
                    mainActivity.replaceFragment(MainActivity.MODIFY_MYPROFILE_FRAGMENT,true,true,null)
                }
            }

            // 구매내역
            textViewMyProfilePurchaseDetails.run {
                setOnClickListener {
                    mainActivity.replaceFragment(MainActivity.PURCHASE_HISTORY_FRAGMENT, true, true, null)
                }
            }

            //로그아웃
            textViewMyProfileLogout.setOnClickListener {

                MaterialAlertDialogBuilder(mainActivity,R.style.ThemeOverlay_App_MaterialAlertDialog).run {
                    setTitle("로그아웃")
                    setMessage("로그아웃 하시겠습니까?")
                    setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                        //로그아웃된 상태

                        //sharedreferences 값 삭제
                        val editor = sharedPreferences.edit()
                        editor.remove("customerUserName")
                        editor.remove("customerUserId")
                        editor.remove("customerUserPw")
                        editor.remove("customerUserShipRecipient")
                        editor.remove("customerUserShipContact")
                        editor.remove("customerUserShipAddress")
                        editor.remove("customerUserPhoneNumber")
                        editor.remove("customerUserProfileImage")
                        editor.apply()

                        loginStatusCheck(sharedPreferences)
                    }
                    setNegativeButton("아니오", null)
                    show()
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

            //문의 내역
            textViewMyProfileMyQuestion.setOnClickListener {
                mainActivity.replaceFragment(MainActivity.MY_QUESTION_LIST_FRAGMENT, true, true, null)
            }
        }

        return fragmentMyprofileBinding.root
    }

    fun loginStatusCheck(sharedPreferences: SharedPreferences){
        if(CustomerUserRepository.checkLoginStatus(sharedPreferences)){
            fragmentMyprofileBinding.textViewMyProfileMyName.text = sharedPreferences.getString("customerUserName", null).toString()
            fragmentMyprofileBinding.textViewMyProfileMyPhoneNumber.text = sharedPreferences.getString("customerUserPhoneNumber", null).toString()
            fragmentMyprofileBinding.textViewMyprofileMyDestination.text = sharedPreferences.getString("customerUserShipAddress", null).toString()
        }else{
            fragmentMyprofileBinding.textViewMyProfileMyName.text = ""
            fragmentMyprofileBinding.textViewMyProfileMyPhoneNumber.text = ""
            fragmentMyprofileBinding.textViewMyprofileMyDestination.text = ""
            MaterialAlertDialogBuilder(mainActivity,R.style.ThemeOverlay_App_MaterialAlertDialog).run {
                setTitle("접근 권한 없음")
                setMessage("로그인이 필요한 서비스입니다.")
                setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                    mainActivity.replaceFragment(MainActivity.LOGIN_FRAGMENT, true, true, null)
                }
                show()
            }
        }
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