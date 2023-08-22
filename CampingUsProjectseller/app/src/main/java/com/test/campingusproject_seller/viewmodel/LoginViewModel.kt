package com.test.campingusproject_seller.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    var userName = MutableLiveData<String>()
    var userId = MutableLiveData<String>()
    var userPw = MutableLiveData<String>()
    var userPhoneNum = MutableLiveData<String>()


}