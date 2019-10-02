package com.snc.farmaccount.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.snc.farmaccount.helper.UserManager

class LogInViewModel :  ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    var hasNewUser = MutableLiveData<Boolean>()
    var hasBudget = MutableLiveData<Boolean>()
    val user = HashMap<String, Any>()

    fun getProfile() {
        db.collection("User")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d("Sophie_db", "${document.id} => ${document.data}")
                        if (document.id == UserManager.userToken) {
                            hasNewUser.value = false
                            break
                        } else {
                            Log.d("Sophie_profile", "not such file: ")
                            setUser()
                        }
                    }

                }
            }
    }

    private fun setUser() {
        user["id"] = UserManager.userToken!!
        user["name"] = UserManager.userName!!
        user["email"] = UserManager.userEmail!!

        db.collection("User").document("${UserManager.userToken}")
            .set(user)
            .addOnSuccessListener {
                Log.d("Sophie_profile_add", "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w("Sophie_profile_add", "Error writing document", e)
            }
        hasNewUser.value = true
        Log.i("Sophie_check","${hasNewUser.value}")
    }

    fun getBudget() {
        val db = FirebaseFirestore.getInstance()
        db.collection("User").document("${UserManager.userToken}")
            .collection("Budget").document("${UserManager.userToken}")
            .get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    hasBudget.value = false
                    Log.d("Sophie", "DocumentSnapshot data: ${document.data}")

                } else {
                    hasBudget.value = true
                    Log.d("Sophie", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Sophie", "get failed with ", exception)
            }
    }


}