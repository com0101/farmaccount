package com.snc.farmaccount.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.snc.farmaccount.UserManager

class LogInViewModel : ViewModel() {
    var checkLogIn = MutableLiveData<Boolean>()

    init {
        Log.i("Sophie_token" , "${UserManager.userToken}")
        if(UserManager.userToken != null){
            checkLogIn.value = true
        } else {
            checkLogIn.value = null
        }

    }
}