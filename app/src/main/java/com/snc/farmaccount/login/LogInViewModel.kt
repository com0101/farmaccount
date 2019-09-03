package com.snc.farmaccount.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.snc.farmaccount.`object`.Event
import com.snc.farmaccount.helper.UserManager

class LogInViewModel : ViewModel() {
    var checkLogIn = MutableLiveData<Boolean>()
    var checkFirst = MutableLiveData<Boolean>()

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

        // Add a new document with a generated ID
        db.collection("User")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d("Sophie_db", "${document.id} => ${document.data}")

                        if (document?.id != UserManager.userToken) {
                            checkFirst.value = true
                            Log.i("Sophie_check","${checkFirst.value}")
                            Log.d("Sophie_profile", "not such file: ")
                            db.collection("User").document("${UserManager.userToken}")
                                .set(user)
                                .addOnSuccessListener { Log.d("Sophie_profile_add", "DocumentSnapshot successfully written!") }
                                .addOnFailureListener { e -> Log.w("Sophie_profile_add", "Error writing document", e) }
                        } else {
                            checkFirst.value = false
                        }

                    }

                }
            }
    }


}