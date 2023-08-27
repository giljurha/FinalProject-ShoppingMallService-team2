package com.test.campingusproject_customer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CustomerUserViewModel : ViewModel() {
    val verificationCompleted = MutableLiveData<Boolean>()
    val verificationFailed = MutableLiveData<Boolean>()
    val codeSent = MutableLiveData<Boolean>()

    val verificationId = MutableLiveData<String>()

    init {
        verificationCompleted.value = false
        verificationFailed.value = false
        codeSent.value = false

        verificationId.value = ""
    }
}