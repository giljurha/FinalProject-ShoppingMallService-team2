package com.test.campingusproject_seller.ui.main

import android.Manifest
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.test.campingusproject_seller.R
import com.test.campingusproject_seller.databinding.ActivityMainBinding
import com.test.campingusproject_seller.ui.notification.NotificationMainFragment
import com.test.campingusproject_seller.repository.UserInfoRepository
import com.test.campingusproject_seller.ui.inquiry.InquiryDetailFragment
import com.test.campingusproject_seller.ui.inquiry.InquiryFragment
import com.test.campingusproject_seller.ui.myinfo.MyInfoFragment
import com.test.campingusproject_seller.ui.product.ManageProductFragment
import com.test.campingusproject_seller.ui.product.ModifyProductFragment
import com.test.campingusproject_seller.ui.product.RegisterProductFragment
import com.test.campingusproject_seller.ui.sellstate.SellStateFragment
import com.test.campingusproject_seller.ui.sellstatedetail.SellStateDetailFragment
import com.test.campingusproject_seller.ui.updatemyinfo.UpdateMyInfoFragment
import com.test.campingusproject_seller.ui.user.AuthFragment
import com.test.campingusproject_seller.ui.user.JoinFragment
import com.test.campingusproject_seller.ui.user.LoginFragment

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding

    companion object{
        val MANAGE_PRODUCT_FRAGMENT = "ManageProductFragment"
        val MODIFY_PRODUCT_FRAGMENT = "ModifyProductFragment"
        val REGISTER_PRODUCT_FRAGMENT = "RegisterProductFragment"
        val SELL_STATE_FRAGMENT = "SellStateFragment"
        val SELL_STATE_DETAIL_FRAGMENT = "SellStateDetailFragment"
        val NOTIFICATION_MAIN_FRAGMENT = "NotificationMainFragment"
        val LOGIN_FRAGMENT="LoginFragment"
        val JOIN_FRAGMENT="joinFragment"
        val AUTH_FRAGMENT="AuthFragment"
        val MY_INFO_FRAGMENT="MyInfoFragment"
        val INQUIRY_FRAGMENT = "InquiryFragment"
        val INQUIRY_DETAIL_FRAGMENT = "InquiryDetailFragment"
        val UPDATE_MY_INFO_FRAGMENT = "UpdateMyInfoFragment"

        val productCountList = arrayOf(
            "0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50",
            "55", "60", "65", "70", "75", "80", "85", "90", "95", "100"
        )
    }

    val permissionList = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_MEDIA_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        //로그인한적 없으면 로그인 화면으로 넘어가고 바텀 네비게이션 안보이게
        if(!UserInfoRepository.checkPref(this)){
            replaceFragment(LOGIN_FRAGMENT,false,false,null)
            activityMainBinding.bottomNavigationViewMain.visibility= View.GONE
        }else{
            replaceFragment(SELL_STATE_FRAGMENT, false, false, null)
        }


        requestPermissions(permissionList, 0)

        activityMainBinding.run {
            bottomNavigationViewMain.menu.findItem(R.id.menuItemSellStatus).setChecked(true)
            bottomNavigationViewMain.setOnItemSelectedListener {
                when(it.itemId){
                    //제품관리 클릭
                    R.id.menuItemManageProduct->{
                        replaceFragment(MANAGE_PRODUCT_FRAGMENT, false, false, null)
                        it.isChecked = true
                    }
                    //판매현황 클릭
                    R.id.menuItemSellStatus->{
                        replaceFragment(SELL_STATE_FRAGMENT, false, false, null)
                        it.isChecked = true
                    }
                    //고객문의 클릭
                    R.id.menuItemCustomerAsk->{
                        replaceFragment(INQUIRY_FRAGMENT, false, false, null)
                        it.isChecked = true

                    }
                    //내정보 클릭
                    R.id.menuItemMyInfo->{
                        replaceFragment(MY_INFO_FRAGMENT, false, false, null)
                        it.isChecked = true

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
            SELL_STATE_FRAGMENT -> SellStateFragment()
            SELL_STATE_DETAIL_FRAGMENT -> SellStateDetailFragment()
            NOTIFICATION_MAIN_FRAGMENT -> NotificationMainFragment()
            LOGIN_FRAGMENT->LoginFragment()
            JOIN_FRAGMENT->JoinFragment()
            AUTH_FRAGMENT->AuthFragment()
            INQUIRY_FRAGMENT -> InquiryFragment()
            INQUIRY_DETAIL_FRAGMENT -> InquiryDetailFragment()
            MY_INFO_FRAGMENT->MyInfoFragment()
            UPDATE_MY_INFO_FRAGMENT -> UpdateMyInfoFragment()
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

    fun hideKeyboard(rootView:View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(rootView.windowToken, 0)
    }
}