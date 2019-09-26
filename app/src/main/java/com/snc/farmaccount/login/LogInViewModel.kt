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
    var checkBudget = MutableLiveData<Boolean>()

    init {
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
                        Log.i("Sophie_check2","${checkFirst.value}")
                    }

                }
            }
    }

    fun getBudget() {
        val db = FirebaseFirestore.getInstance()
        db.collection("User").document("${UserManager.userToken}")
            .collection("Budget").document("${UserManager.userToken}")
            .get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    checkBudget.value = false
                    Log.d("Sophie", "DocumentSnapshot data: ${document.data}")
                    Log.d("Sophie", "${checkBudget.value}")

                } else {
                    checkBudget.value = true
                    Log.d("Sophie", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Sophie", "get failed with ", exception)
            }
    }


}