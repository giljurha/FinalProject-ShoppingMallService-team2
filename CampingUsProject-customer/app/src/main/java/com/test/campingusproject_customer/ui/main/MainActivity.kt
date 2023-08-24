package com.test.campingusproject_customer.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.ActivityMainBinding
import com.test.campingusproject_customer.ui.campsite.CampsiteFragment
import com.test.campingusproject_customer.ui.campsite.ContractCampsiteFragment
import com.test.campingusproject_customer.ui.shopping.ShoppingProductFragment
import com.test.campingusproject_customer.ui.comunity.PostWriteFragment
import com.test.campingusproject_customer.ui.comunity.ComunityFragment
import com.test.campingusproject_customer.ui.comunity.PostReadFragment
import com.test.campingusproject_customer.ui.myprofile.ModifyMyPostFragment
import com.test.campingusproject_customer.ui.myprofile.ModifyMyprofileFragment
import com.test.campingusproject_customer.ui.myprofile.MyPostListFragment
import com.test.campingusproject_customer.ui.myprofile.MyprofileFragment
import com.test.campingusproject_customer.ui.shopping.ShoppingFragment
import com.test.campingusproject_customer.ui.shopping.ShoppingPlusFragment
import com.test.campingusproject_customer.ui.user.AuthFragment
import com.test.campingusproject_customer.ui.user.JoinFragment
import com.test.campingusproject_customer.ui.user.LoginFragment
import com.test.campingusproject_customer.ui.payment.CartFragment
import com.test.campingusproject_customer.ui.payment.OrderDetailFragment
import com.test.campingusproject_customer.ui.payment.PaymentFragment

class MainActivity : AppCompatActivity() {
    lateinit var activityMainBinding: ActivityMainBinding

    companion object {
        val HOME_FRAGMENT = "HomeFragment"
        val SHOPPING_FRAGMENT = "ShoppingFragment"
        val COMUNITY_FRAGMENT = "ComunityFragment"
        val MYPROFILE_FRAGMENT = "MyProfileFragment"
        val CAMPSITE_FRAGMENT = "CampsiteFragment"
        val SHOPPING_PRODUCT_FRAGMENT = "ShoppingProductFragment"
        val POST_WRITE_FRAGMENT = "PostWriteFragment"
        val SHOPPING_PLUS_FRAGMENT = "ShoppingPlusFragment"
        val POST_READ_FRAGMENT = "PostReadFragment"
        val LOGIN_FRAGMENT = "LoginFragment"
        val JOIN_FRAGMENT = "JoinFragment"
        val AUTH_FRAGMENT = "AuthFragment"
        val CART_FRAGMENT = "CartFragment"
        val PAYMENT_FRAGMENT = "PaymentFragment"
        val ORDER_DETAIL_FRAGMENT = "OrderDetailFragment"
        val MODIFY_MYPROFILE_FRAGMENT = "ModifyMyprofileFragment"
        val CONTRACT_CAMPSITE_FRAGMENT = "ContractCampsiteFragment"
        val MY_POST_LIST_FRAGMENT = "MyPostListFragment"
        val MODIFY_MY_POST_FRAGMENT = "ModifyMyPostFragment"
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
                setOnItemSelectedListener {
                    //선택된 메뉴를 다시 클릭할 때 선택을 넘기는 조건문
                    if (it.itemId == selectedItemId){
                        return@setOnItemSelectedListener false
                    }
                    when (it.itemId) {
                        //홈 클릭
                        R.id.menuItemHome -> {
                            removeFragment(CAMPSITE_FRAGMENT)
                            replaceFragment(HOME_FRAGMENT, false, true, null)
                        }
                        //캠핑장 클릭
                        R.id.menuItemCamping -> {
                            replaceFragment(CAMPSITE_FRAGMENT,true,true,null)
                        }
                        //쇼핑 클릭
                        R.id.menuItemShopping -> {
                            removeFragment(CAMPSITE_FRAGMENT)
                            replaceFragment(SHOPPING_FRAGMENT, false, true, null)
                        }
                        //커뮤니티 클릭
                        R.id.menuItemComunity -> {
                            removeFragment(CAMPSITE_FRAGMENT)
                            replaceFragment(COMUNITY_FRAGMENT, false, true, null)
                        }
                        //내정보 클릭
                        R.id.menuItemMyProfile -> {
                            removeFragment(CAMPSITE_FRAGMENT)
                            replaceFragment(MYPROFILE_FRAGMENT, false, true, null)
                        }

                        else -> {
                            removeFragment(CAMPSITE_FRAGMENT)
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
            SHOPPING_PLUS_FRAGMENT -> ShoppingPlusFragment()
            LOGIN_FRAGMENT -> LoginFragment()
            JOIN_FRAGMENT -> JoinFragment()
            AUTH_FRAGMENT -> AuthFragment()
            CART_FRAGMENT -> CartFragment()
            PAYMENT_FRAGMENT -> PaymentFragment()
            ORDER_DETAIL_FRAGMENT -> OrderDetailFragment()
            MODIFY_MYPROFILE_FRAGMENT -> ModifyMyprofileFragment()
            MY_POST_LIST_FRAGMENT ->MyPostListFragment()
            MODIFY_MY_POST_FRAGMENT ->ModifyMyPostFragment()
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
}