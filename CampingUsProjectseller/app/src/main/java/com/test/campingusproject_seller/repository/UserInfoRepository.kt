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
            editor.putString("userName",userClass.sellerUserName)
            editor.putString("userId",userClass.sellerUserId)
            editor.putString("userPw",userClass.sellerUserPassword)
            editor.putString("userPhoneNum",userClass.sellerUserPhoneNumber)

            editor.commit()
        }

        fun deletePre(mainActivity:MainActivity){
            val pref = mainActivity.getSharedPreferences("user_info", Context.MODE_PRIVATE)

            //데이터 설정
            val editor=pref.edit()
            //값 모두 삭제
           editor.clear()

            editor.commit()
        }


        //회원가입 메서드(사용자 정보 저장)
        fun addUserInfo(userClass: UserModel, callback: (Task<Void>) -> Unit) {
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("SellerUsers")
            userDataRef.push().setValue(userClass).addOnCompleteListener(callback)
        }

        //해당 전화번호로 가입한 사람이 있는지 확인하는 메서드
        fun checkDuplicationUser(userPhoneNum:String,listener:ValueEventListener){
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("SellerUsers")

            //유저 정보중에 가입한 번호가 있는지 찾는다
            userDataRef.orderByChild("sellerUserPhoneNumber").equalTo(userPhoneNum).addListenerForSingleValueEvent(listener)
        }

        //시용자 아이디를 통해 사용자 정보를 가져온다
        fun getUserInfoByUserId(loginId:String,callback: (Task<DataSnapshot>) -> Unit){
            val database = FirebaseDatabase.getInstance()
            val userDataRef = database.getReference("SellerUsers")

            //유저 아이디가 사용자가 입력환 아이디와 같은 데이터를 가져온다
            userDataRef.orderByChild("sellerUserId").equalTo(loginId).get().addOnCompleteListener(callback)
        }
    }
}