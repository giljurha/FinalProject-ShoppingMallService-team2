package com.test.campingusproject_customer.ui.main

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.ActivityMainBinding
import com.test.campingusproject_customer.repository.CustomerUserRepository
import com.test.campingusproject_customer.ui.campsite.CampsiteFragment
import com.test.campingusproject_customer.ui.campsite.ContractCampsiteFragment
import com.test.campingusproject_customer.ui.shopping.ShoppingProductFragment
import com.test.campingusproject_customer.ui.comunity.PostWriteFragment
import com.test.campingusproject_customer.ui.comunity.ComunityFragment
import com.test.campingusproject_customer.ui.comunity.PostReadFragment
import com.test.campingusproject_customer.ui.inquiry.InquiryFragment
import com.test.campingusproject_customer.ui.myprofile.ModifyMyPostFragment
import com.test.campingusproject_customer.ui.myprofile.ModifyMyprofileFragment
import com.test.campingusproject_customer.ui.myprofile.MyQuestionDetailFragment
import com.test.campingusproject_customer.ui.myprofile.MyQuestionListFragment
import com.test.campingusproject_customer.ui.myprofile.MyPostListFragment
import com.test.campingusproject_customer.ui.myprofile.MyprofileFragment
import com.test.campingusproject_customer.ui.myprofile.PurchaseHistoryFragment
import com.test.campingusproject_customer.ui.myprofile.ReviewWriteFragment
import com.test.campingusproject_customer.ui.shopping.ShoppingFragment
import com.test.campingusproject_customer.ui.user.AuthFragment
import com.test.campingusproject_customer.ui.user.JoinFragment
import com.test.campingusproject_customer.ui.user.LoginFragment
import com.test.campingusproject_customer.ui.payment.CartFragment
import com.test.campingusproject_customer.ui.payment.OrderDetailFragment
import com.test.campingusproject_customer.ui.payment.PaymentFragment
import com.test.campingusproject_customer.ui.review.ReviewDetailFragment
import com.test.campingusproject_customer.ui.review.ReviewFragment
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    lateinit var activityMainBinding: ActivityMainBinding
    var currentFragment = 0
    var selectMenu = 0

    companion object {
        val HOME_FRAGMENT = "HomeFragment"
        val SHOPPING_FRAGMENT = "ShoppingFragment"
        val COMUNITY_FRAGMENT = "ComunityFragment"
        val MYPROFILE_FRAGMENT = "MyProfileFragment"
        val CAMPSITE_FRAGMENT = "CampsiteFragment"
        val SHOPPING_PRODUCT_FRAGMENT = "ShoppingProductFragment"
        val POST_WRITE_FRAGMENT = "PostWriteFragment"
        val POST_READ_FRAGMENT = "PostReadFragment"
        val LOGIN_FRAGMENT = "LoginFragment"
        val JOIN_FRAGMENT = "JoinFragment"
        val AUTH_FRAGMENT = "AuthFragment"
        val CART_FRAGMENT = "CartFragment"
        val PAYMENT_FRAGMENT = "PaymentFragment"
        val ORDER_DETAIL_FRAGMENT = "OrderDetailFragment"
        val MODIFY_MYPROFILE_FRAGMENT = "ModifyMyprofileFragment"
        val CONTRACT_CAMPSITE_FRAGMENT = "ContractCampsiteFragment"
        val INQUIRY_FRAGMENT = "InquiryFragment"
        val MY_QUESTION_LIST_FRAGMENT = "MyQuestionListFragment"
        val MY_QUESTION_DETAIL_FRAGMENT = "MyQuestionDetailFragment"
        val MY_POST_LIST_FRAGMENT = "MyPostListFragment"
        val MODIFY_MY_POST_FRAGMENT = "ModifyMyPostFragment"
        val PURCHASE_HISTORY_FRAGMENT = "PurchaseHistoryFragment"
        val REVIEW_FRAGMENT = "ReviewFragment"
        val REVIEW_DETAIL_FRAGMENT = "ReviewDetailFragment"
        val REVIEW_WRITE_FRAGMENT = "ReviewWriteFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        //시작하면 홈으로 가기
        replaceFragment(HOME_FRAGMENT, false, true, null)

        activityMainBinding.run {
            bottomNavigationViewMain.run {
                this.selectedItemId = R.id.menuItemHome
                selectMenu = R.id.menuItemHome
                Log.d("aaaa","$selectMenu")
                setOnItemSelectedListener {
                    //선택된 메뉴를 다시 클릭할 때 선택을 넘기는 조건문
                    if (it.itemId == selectedItemId){
                        return@setOnItemSelectedListener false
                    }
                    when (it.itemId) {
                        //홈 클릭
                        R.id.menuItemHome -> {
                            selectMenu = it.itemId
                            Log.d("aaaa","$selectMenu")
                            replaceFragment(HOME_FRAGMENT, false, true, null)
                        }
                        //캠핑장 클릭
                        R.id.menuItemCamping -> {
                            selectMenu = it.itemId
                            Log.d("aaaa","$selectMenu")
                            replaceFragment(CAMPSITE_FRAGMENT,false,true,null)
                        }
                        //쇼핑 클릭
                        R.id.menuItemShopping -> {
                            selectMenu = it.itemId
                            Log.d("aaaa","$selectMenu")
                            replaceFragment(SHOPPING_FRAGMENT, false, true, null)
                        }
                        //커뮤니티 클릭
                        R.id.menuItemComunity -> {
                            val sharedPreferences = getSharedPreferences("customer_user_info", Context.MODE_PRIVATE)
                            if(CustomerUserRepository.checkLoginStatus(sharedPreferences) == false) {
                                replaceFragment(COMUNITY_FRAGMENT, false, true, null)
                                val builder = MaterialAlertDialogBuilder(this@MainActivity, R.style.ThemeOverlay_App_MaterialAlertDialog)
                                builder.run {
                                    setTitle("로그인 필요")
                                    setMessage("로그인이 필요합니다.")
                                    setPositiveButton("닫기") { dialogInterface: DialogInterface, i: Int ->
                                        removeFragment(COMUNITY_FRAGMENT)
                                        selectedItemId = selectMenu
                                    }
                                    show()
                                }
                                Log.d("aaaa","$selectMenu")
                            }
                            else{
                                selectMenu = it.itemId
                                Log.d("aaaa","$selectMenu")
                                val boardType: Long = 1L
                                val newBundle = Bundle()
                                newBundle.putLong("boardType", boardType)
                                replaceFragment(COMUNITY_FRAGMENT, false, true, newBundle)
                            }

                        }
                        //내정보 클릭
                        R.id.menuItemMyProfile -> {
                            selectMenu = it.itemId
                            replaceFragment(MYPROFILE_FRAGMENT, false, true, null)
                        }

                        else -> {
                            replaceFragment(HOME_FRAGMENT, false, true, null)
                        }
                    }
                    true
                }
            }
        }
    }

    // 지정한 Fragment를 보여주는 메서드
    fun replaceFragment(name: String, addToBackStack: Boolean, animate: Boolean, bundle: Bundle?) {
        // Fragment 교체 상태로 설정한다.
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        // 새로운 Fragment를 담을 변수
        var newFragment = when (name) {
            HOME_FRAGMENT -> HomeFragment()
            SHOPPING_FRAGMENT -> ShoppingFragment()
            SHOPPING_PRODUCT_FRAGMENT -> ShoppingProductFragment()
            COMUNITY_FRAGMENT -> ComunityFragment()
            MYPROFILE_FRAGMENT -> MyprofileFragment()
            CAMPSITE_FRAGMENT-> CampsiteFragment()
            CONTRACT_CAMPSITE_FRAGMENT-> ContractCampsiteFragment()
            POST_WRITE_FRAGMENT -> PostWriteFragment()
            POST_READ_FRAGMENT -> PostReadFragment()
            LOGIN_FRAGMENT -> LoginFragment()
            JOIN_FRAGMENT -> JoinFragment()
            AUTH_FRAGMENT -> AuthFragment()
            CART_FRAGMENT -> CartFragment()
            PAYMENT_FRAGMENT -> PaymentFragment()
            ORDER_DETAIL_FRAGMENT -> OrderDetailFragment()
            INQUIRY_FRAGMENT -> InquiryFragment()
            MODIFY_MYPROFILE_FRAGMENT -> ModifyMyprofileFragment()
            MY_QUESTION_LIST_FRAGMENT -> MyQuestionListFragment()
            MY_QUESTION_DETAIL_FRAGMENT -> MyQuestionDetailFragment()
            MY_POST_LIST_FRAGMENT ->MyPostListFragment()
            MODIFY_MY_POST_FRAGMENT ->ModifyMyPostFragment()
            PURCHASE_HISTORY_FRAGMENT -> PurchaseHistoryFragment()
            REVIEW_FRAGMENT -> ReviewFragment()
            REVIEW_DETAIL_FRAGMENT -> ReviewDetailFragment()
            REVIEW_WRITE_FRAGMENT -> ReviewWriteFragment()

            else -> Fragment()
        }

        newFragment.arguments = bundle

        if (newFragment != null) {

            // Fragment를 교체한다.
            fragmentTransaction.replace(R.id.fragmentContainerMain, newFragment)

            if (animate == true) {
                // 애니메이션을 설정한다.
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            }

            if (addToBackStack == true) {
                // Fragment를 Backstack에 넣어 이전으로 돌아가는 기능이 동작할 수 있도록 한다.
                fragmentTransaction.addToBackStack(name)
            }

            // 교체 명령이 동작하도록 한다.
            fragmentTransaction.commit()
        }
    }

    // Fragment를 BackStack에서 제거한다.
    fun removeFragment(name: String) {
        supportFragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    //인자로 전달된 뷰에 포커스주고 키보드 생성
    fun focusOnView(view:View){
        view.requestFocus()
        thread {
            SystemClock.sleep(500)
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, 0)
        }
    }
}