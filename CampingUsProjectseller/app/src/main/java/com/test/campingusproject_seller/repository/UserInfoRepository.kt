package com.test.campingusproject_seller.repository

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.test.campingusproject_seller.dataclassmodel.UserModel
import com.test.campingusproject_seller.ui.main.MainActivity

class UserInfoRepository {
    companion object {
        //로그인 여부 가져오기 위한 메서드
        fun checkPref(mainActivity: MainActivity): Boolean {
            val pref = mainActivity.getSharedPreferences("user_info", Context.MODE_PRIVATE)
            val userName = pref.getString("userName", null)
            val userId = pref.getString("userId", null)
            val userPw = pref.getString("userPw", null)
            val userPhoneNum = pref.getString("userPhoneNum", null)

            return !(userName == null && userId == null && userPw == null && userPhoneNum == null)
        }

        //로그인시 앱 preference에 사용자 정보 저장
        fun savePre(mainActivity:MainActivity,userClass: UserModel){
            val pref = mainActivity.getSharedPreferences("user_info", Context.MODE_PRIVATE)

            //데이터 설정
            val editor=pref.edit()
            //값 설정
            editor.putString("userName",userClass.userName)
            editor.putString("userId",userClass.userId)
            editor.putString("userPw",userClass.userPassword)
            editor.putString("userPhoneNum",userClass.userPhoneNumber)

            editor.commit()
        }


        //회원가입 메서드(사용자 정보 저장)
        fun addUserInfo(userClass: UserModel, callback: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("Users")
            userDataRef.push().setValue(userClass).addOnCompleteListener(callback)
        }

        fun checkDuplicationUser(userPhoneNum:String,listener:ValueEventListener){
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("Users")

            //유저 정보중에 가입한 번호가 있는지 찾는다
            userDataRef.orderByChild("userPhoneNumber").equalTo(userPhoneNum).addListenerForSingleValueEvent(listener)
        }
    }
}