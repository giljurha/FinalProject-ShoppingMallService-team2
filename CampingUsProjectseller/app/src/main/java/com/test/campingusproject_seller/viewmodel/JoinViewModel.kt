package com.test.campingusproject_seller.viewmodel

import androidx.lifecycle.MutableLiveData

class JoinViewModel {
    val userName=MutableLiveData<String>()
    val userId=MutableLiveData<String>()
    val userPw=MutableLiveData<String>()
    val userPw2=MutableLiveData<String>()
    val userPhoneNum=MutableLiveData<String>()
}