package com.snc.farmaccount.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.simplemobiletools.commons.extensions.toBoolean
import com.snc.farmaccount.`object`.Event
import com.snc.farmaccount.helper.UserManager
import java.util.HashMap

class DetailViewModel(product: Event, app: Application) : AndroidViewModel(app) {

    var infoInput = MutableLiveData<String>()
    var idCheck = MutableLiveData<String>()

    private val _detail = MutableLiveData<Event>()

    val detail: LiveData<Event>
        get() = _detail


    init {
        _detail.value = product
        infoInput.value = detail.value?.description

    }

    fun deleteEvent() {
        val db = FirebaseFirestore.getInstance()
        idCheck.value = UserManager.userToken!!.substring(0,20)
        // Add a new document with a generated ID
        db.collection("User").document("${UserManager.userToken}").collection("Event")
            .whereEqualTo("description","${infoInput.value}")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d("Sophie_db", "${document.id} => ${document.data}")
                        db.collection("User").document("${UserManager.userToken}").collection("Event")
                            .document("${document.id}")
                            .delete()
                            .addOnSuccessListener { documentReference ->
                                Log.d(
                                    "Sophie_update",
                                    "DocumentSnapshot added with ID: $documentReference"
                                )
                            }
                            .addOnFailureListener { e -> Log.w("Sophie_add_fail", "Error adding document", e) }
                    }
                }
            }

    }


}