package com.test.campingusproject_seller.ui.main

import android.Manifest
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.ActivityMainBinding
import com.test.campingusproject_seller.repository.LoginRepository
import com.test.campingusproject_seller.ui.product.ManageProductFragment
import com.test.campingusproject_seller.ui.product.ModifyProductFragment
import com.test.campingusproject_seller.ui.product.RegisterProductFragment
import com.test.campingusproject_seller.ui.user.AuthFragment
import com.test.campingusproject_seller.ui.user.JoinFragment
import com.test.campingusproject_seller.ui.user.LoginFragment

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding

    companion object{
        val MANAGE_PRODUCT_FRAGMENT = "ManageProductFragment"
        val MODIFY_PRODUCT_FRAGMENT = "ModifyProductFragment"
        val REGISTER_PRODUCT_FRAGMENT = "RegisterProductFragment"
        val LOGIN_FRAGMENT="LoginFragment"
        val JOIN_FRAGMENT="joinFragment"
        val AUTH_FRAGMENT="AuthFragment"
    }

    val permissionList = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_MEDIA_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        //로그인한적 없으면 로그인 화면으로
        if(!LoginRepository.checkLogin(this)){
            replaceFragment(LOGIN_FRAGMENT,false,false,null)
            activityMainBinding.bottomNavigationViewMain.visibility= View.GONE
        }


        requestPermissions(permissionList, 0)

        activityMainBinding.run {

            bottomNavigationViewMain.setOnItemSelectedListener {
                when(it.itemId){
                    //제품관리 클릭
                    R.id.menuItemManageProduct->{
                        replaceFragment(MANAGE_PRODUCT_FRAGMENT, false, false, null)
                    }
                    //판매현황 클릭
                    R.id.menuItemSellStatus->{

                    }
                    //고객문의 클릭
                    R.id.menuItemCustomerAsk->{

                    }
                    //내정보 클릭
                    R.id.menuItemMyInfo->{

                    }
                }
                false
            }
        }
    }

    // 지정한 Fragment를 보여주는 메서드
    fun replaceFragment(name:String, addToBackStack:Boolean, animate:Boolean, bundle:Bundle?){
        // Fragment 교체 상태로 설정한다.
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        // 새로운 Fragment를 담을 변수
        var newFragment = when(name){
            MANAGE_PRODUCT_FRAGMENT -> ManageProductFragment()
            MODIFY_PRODUCT_FRAGMENT -> ModifyProductFragment()
            REGISTER_PRODUCT_FRAGMENT -> RegisterProductFragment()
            LOGIN_FRAGMENT->LoginFragment()
            JOIN_FRAGMENT->JoinFragment()
            AUTH_FRAGMENT->AuthFragment()
            else -> Fragment()
        }

        newFragment.arguments = bundle

        if(newFragment != null) {
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
    fun removeFragment(name:String){
        supportFragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}