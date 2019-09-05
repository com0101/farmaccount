package com.snc.farmaccount.login

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.snc.farmaccount.ApplicationContext
import com.snc.farmaccount.MainActivity
import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.Event
import com.snc.farmaccount.helper.UserManager
import kotlinx.coroutines.*

class LogInViewModel :  ViewModel() {
    var checkLogIn = MutableLiveData<Boolean>()
    var checkFirst = MutableLiveData<Boolean>()
    var idCheck = MutableLiveData<String>()
    var chaneFragment = MutableLiveData<Boolean>()

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)


    init {
        Log.i("Sophie_token", "${UserManager.userToken}+${UserManager.userEmail}+${UserManager.userName}")
        if (UserManager.userToken != null) {
            checkLogIn.value = true
        } else {
            checkLogIn.value = null
        }
    }

    fun getProfile() {
        val db = FirebaseFirestore.getInstance()
        // Create a new user with a first and last name
        val user = HashMap<String, Any>()
        user["id"] = UserManager.userToken!!
        user["name"] = UserManager.userName!!
        user["email"] = UserManager.userEmail!!

        idCheck.value = UserManager.userToken!!.substring(0,20)
        // Add a new document with a generated ID
        db.collection("User")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Sophie_userToken", "${UserManager.userToken}")
                    for (document in task.result!!) {
                        Log.d("Sophie_db", "${document.id} => ${document.data}")
                        if (document.id == UserManager.userToken) {
                            checkFirst.value = false
                            break
                        } else {
                            Log.d("Sophie_profile", "not such file: ")
                            db.collection("User").document("${UserManager.userToken}")
                                .set(user)
                                .addOnSuccessListener { Log.d("Sophie_profile_add", "DocumentSnapshot successfully written!") }
                                .addOnFailureListener { e -> Log.w("Sophie_profile_add", "Error writing document", e) }
                            checkFirst.value = true
                            Log.i("Sophie_check","${checkFirst.value}")
                        }
                    }

                }
            }
    }







//    fun getUserStatus() {
//        CoroutineScope(Dispatchers.IO).launch {
//            val deferredUser = GlobalScope.async {getProfile()}
//            deferredUser.invokeOnCompletion {
//                val user = deferredUser.isActive
//                Log.i("Sophie_profile", "${user}")
//                if(user) {
////                                viewModel.getProfile()
//                    Log.i("Sophie_profile", "${checkFirst.value}")
//
//                }
//
//            }
//        }
//
//    }



    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

//
//    fun getRes(callback: (Any)->String){
//        callback(getProfile())
//    }





}