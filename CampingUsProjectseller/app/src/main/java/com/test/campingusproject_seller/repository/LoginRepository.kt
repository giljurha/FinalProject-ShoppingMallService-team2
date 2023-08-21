package com.test.campingusproject_seller.repository

import android.content.Context
import com.test.campingusproject_seller.ui.main.MainActivity

class LoginRepository {
    companion object{
        fun checkLogin(mainActivity: MainActivity):Boolean{
            val pre=mainActivity.getSharedPreferences("user_info",Context.MODE_PRIVATE)
            val userName=pre.getString("userName",null)
            val userId=pre.getString("userId",null)
            val userPw=pre.getString("userPw",null)
            val userPhoneNum=pre.getString("userPhoneNum",null)

            return !(userName==null&&userId==null&&userPw==null&&userPhoneNum==null)
        }
    }
}