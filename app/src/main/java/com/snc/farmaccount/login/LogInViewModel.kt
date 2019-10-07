package com.snc.farmaccount.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.snc.farmaccount.helper.*

class LogInViewModel :  ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    var hasNewUser = MutableLiveData<Boolean>()
    var hasBudget = MutableLiveData<Boolean>()
    val user = HashMap<String, Any>()

    fun getProfile() { // check the user account existence
        db.collection(USER)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d(SOPHIE, "getProfile:${document.id} => ${document.data}")
                        if (document.id == UserManager.userToken) {
                            hasNewUser.value = false
                            break
                        } else {
                            Log.d(SOPHIE, "not such Profile: ")
                            setUser()
                        }
                    }

                }
            }
    }

    private fun setUser() {
        user[ID] = UserManager.userToken!!
        user[NAME] = UserManager.userName!!
        user[EMAIL] = UserManager.userEmail!!

        db.collection(USER).document("${UserManager.userToken}")
            .set(user)
            .addOnSuccessListener {
                Log.d(SOPHIE, "DocumentSnapshot successfully setUser!")
            }
            .addOnFailureListener { e ->
                Log.w(SOPHIE, "Error setUser document", e)
            }
        hasNewUser.value = true
    }

    fun getBudget() {
        db.collection(USER).document("${UserManager.userToken}")
            .collection(BUDGET).document("${UserManager.userToken}")
            .get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    hasBudget.value = false
                    Log.d(SOPHIE, "DocumentSnapshot getBudget data: ${document.data}")

                } else {
                    hasBudget.value = true
                    Log.d(SOPHIE, "No such getBudget document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(SOPHIE, "getBudget failed with ", exception)
            }
    }


}