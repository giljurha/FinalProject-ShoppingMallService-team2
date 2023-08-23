package com.test.campingusproject_customer.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.test.campingusproject_customer.R
import com.test.campingusproject_customer.databinding.ActivityMainBinding
import com.test.campingusproject_customer.ui.camping.CampingFragment
import com.test.campingusproject_customer.ui.comunity.ComunityFragment
import com.test.campingusproject_customer.ui.myprofile.MyprofileFragment
import com.test.campingusproject_customer.ui.shopping.ShoppingFragment

class MainActivity : AppCompatActivity() {
    lateinit var activityMainBinding: ActivityMainBinding

    companion object {
        val HOME_FRAGMENT = "HomeFragment"
        val CAMPING_FRAGMENT = "CampingFragment"
        val SHOPPING_FRAGMENT = "ShoppingFragment"
        val COMUNITY_FRAGMENT = "ComunityFragment"
        val MYPROFILE_FRAGMENT = "MyProfileFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        //시작하면 홈으로 가기
        replaceFragment(HOME_FRAGMENT, false, false, null)

        activityMainBinding.run {
            bottomNavigationViewMain.run {
                this.selectedItemId = R.id.menuItemHome
                setOnItemSelectedListener {
                    when (it.itemId) {
                        //홈 클릭
                        R.id.menuItemHome -> {
                            replaceFragment(HOME_FRAGMENT, false, false, null)
                        }
                        //캠핑장 클릭
                        R.id.menuItemCamping -> {
                            replaceFragment(CAMPING_FRAGMENT, false, false, null)
                        }
                        //쇼핑 클릭
                        R.id.menuItemShopping -> {
                            replaceFragment(SHOPPING_FRAGMENT, false, false, null)
                        }
                        //커뮤니티 클릭
                        R.id.menuItemComunity -> {
                            replaceFragment(COMUNITY_FRAGMENT, false, false, null)
                        }
                        //내정보 클릭
                        R.id.menuItemMyProfile -> {
                            replaceFragment(MYPROFILE_FRAGMENT, false, false, null)
                        }

                        else -> {
                            replaceFragment(HOME_FRAGMENT, false, false, null)
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
            CAMPING_FRAGMENT -> CampingFragment()
            SHOPPING_FRAGMENT -> ShoppingFragment()
            COMUNITY_FRAGMENT -> ComunityFragment()
            MYPROFILE_FRAGMENT -> MyprofileFragment()
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