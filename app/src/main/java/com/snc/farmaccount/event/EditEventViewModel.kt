package com.snc.farmaccount.event

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.simplemobiletools.commons.extensions.toBoolean
import com.snc.farmaccount.`object`.Event
import com.snc.farmaccount.`object`.Tag
import com.snc.farmaccount.helper.UserManager
import java.text.SimpleDateFormat
import java.util.*

class EditEventViewModel(product: Event, app: Application) : AndroidViewModel(app) {

    var mark = MutableLiveData<List<Tag>>()
    var priceInput = MutableLiveData<String>()
    var infoInput = MutableLiveData<String>()
    var chooseTag = MutableLiveData<Tag>()
    var today = MutableLiveData<String>()
    var month :String = ""
    var overagePrice = MutableLiveData<String>()


    private val _tag = MutableLiveData<List<Tag>>()
    val tag: LiveData<List<Tag>>
        get() = _tag

    private val _detail = MutableLiveData<Event>()
    val detail: LiveData<Event>
        get() = _detail

    init {
        _detail.value = product
        priceInput.value = detail.value?.price
        infoInput.value = detail.value?.description
        today.value = detail.value?.date
        month = detail.value?.month!!
        getOverage()
        Log.i("tag","${chooseTag.value}")
    }



    fun editFirebase() {
        val db = FirebaseFirestore.getInstance()
        val currentTimestamp = System.currentTimeMillis()
        // Create a new user with a first and last name
        val event = HashMap<String,Any>()
        if (chooseTag != null) {
            detail.value?.tag = chooseTag.value?.tag_name
            detail.value?.status = chooseTag.value?.tag_status
        }
        event["price"] = priceInput.value!!
        event["tag"] = detail.value!!.tag.toString()
        event["description"] = infoInput.value!!
        event["date"] = today.value!!
        event["time"] = currentTimestamp
        event["status"] = detail.value!!.status!!.toBoolean()
        event["month"] = month
        // Add a new document with a generated ID

        db.collection("User").document("${UserManager.userToken}").collection("Event")
            .whereEqualTo("description","${detail.value?.description}")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d("Sophie_db", "${document.id} => ${document.data}")
                        db.collection("User").document("${UserManager.userToken}").collection("Event")
                            .document("${document.id}")
                            .update(event)
                            .addOnSuccessListener { documentReference ->
                                Log.d(
                                    "Sophie_update",
                                    "DocumentSnapshot added with ID: $documentReference"
                                )
                                var price = priceInput.value!!.toLong() - detail.value!!.price!!.toLong()
                                var overageInt = overagePrice.value?.toLong()
                                if (chooseTag.value?.tag_status == true) {
                                    overagePrice.value = (overageInt?.plus(price)).toString()
                                } else {
                                    overagePrice.value = (overageInt?.minus(price)).toString()
                                }
                                db.collection("User").document("${UserManager.userToken}").collection("Budget")
                                    .document("${UserManager.userToken}")
                                    .update("overage","${overagePrice.value}")
                                    .addOnSuccessListener { Log.d("Sophie_budget_edit", "DocumentSnapshot successfully written!") }
                                    .addOnFailureListener { e -> Log.w("Sophie_budget_edit", "Error writing document", e) }
                            }
                            .addOnFailureListener { e -> Log.w("Sophie_add_fail", "Error adding document", e) }

                    }
                }
            }

    }

    private fun getOverage() {
        val db = FirebaseFirestore.getInstance()
        db.collection("User").document("${UserManager.userToken}").collection("Budget")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d("Sophie_db", "${document.id} => ${document.data["overage"]}")
                        overagePrice.value = document.data["overage"].toString()
                    }
                }
            }
    }


    fun getTag() {
        _tag.value = mark.value
    }

}