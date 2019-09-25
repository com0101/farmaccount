package com.snc.farmaccount.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.simplemobiletools.commons.extensions.toBoolean
import com.simplemobiletools.commons.extensions.toInt
import com.snc.farmaccount.`object`.Event
import com.snc.farmaccount.helper.UserManager
import java.text.SimpleDateFormat
import java.util.*

class DetailViewModel(product: Event, app: Application) : AndroidViewModel(app) {

    var infoInput = MutableLiveData<String>()
    var idCheck = MutableLiveData<String>()
    var overagePrice = MutableLiveData<String>()
    var circleDay = MutableLiveData<Int>()
    var time = MutableLiveData<Long>()
    var startTime = 0
    var endTime = 0
    var lastTime = 0
    var futureTime = 0
    var thisDate = MutableLiveData<Long>()
    var price = 0L

    private val _detail = MutableLiveData<Event>()

    val detail: LiveData<Event>
        get() = _detail


    init {
        _detail.value = product
        infoInput.value = detail.value?.description
        time.value = detail.value?.time
        getOverage()
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
                                when {
                                    thisDate.value!!.toInt() in (lastTime + 1) until (futureTime-1) -> {
                                        if (time.value!!.toInt() in (lastTime + 1) until (futureTime-1)) {
                                            price = detail.value!!.price!!.toLong()
                                            updateOverage()
                                            Log.d("Sophie_budget_over",
                                                "in!")
                                        }
                                    }
                                    thisDate.value!!.toInt() in (startTime + 1) until (endTime-1) -> {
                                        if (time.value!!.toInt() in (startTime + 1) until (endTime-1)) {
                                            price = detail.value!!.price!!.toLong()
                                            updateOverage()
                                            Log.d("Sophie_budget_over",
                                                "inagain!")
                                        }
                                    }
                                    else -> {
                                        price = 0
                                        updateOverage()
                                    }
                                }
//                                var price = detail.value!!.price!!.toLong()
//                                var overageInt = overagePrice.value?.toLong()
//                                if (detail.value!!.status == true) {
//                                    overagePrice.value = (overageInt?.minus(price)).toString()
//                                } else {
//                                    overagePrice.value = (overageInt?.plus(price)).toString()
//                                }
//                                db.collection("User").document("${UserManager.userToken}").collection("Budget")
//                                    .document("${UserManager.userToken}")
//                                    .update("overage","${overagePrice.value}")
//                                    .addOnSuccessListener { Log.d("Sophie_budget_edit", "DocumentSnapshot successfully written!") }
//                                    .addOnFailureListener { e -> Log.w("Sophie_budget_edit", "Error writing document", e) }

                            }
                            .addOnFailureListener { e -> Log.w("Sophie_add_fail", "Error adding document", e) }
                    }
                }
            }

    }

    private fun updateOverage() {
        val db = FirebaseFirestore.getInstance()
        var overageInt = overagePrice.value?.toLong()
        if (detail.value!!.status == true) {
            overagePrice.value = (overageInt?.minus(price)).toString()
        } else {
            overagePrice.value = (overageInt?.plus(price)).toString()
        }
        db.collection("User").document("${UserManager.userToken}").collection("Budget")
            .document("${UserManager.userToken}")
            .update("overage","${overagePrice.value}")
            .addOnSuccessListener { Log.d("Sophie_budget_edit", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("Sophie_budget_edit", "Error writing document", e) }

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
                        circleDay.value = document.data["circleDay"]?.toInt()
                        val c = Calendar.getInstance()
                        val year = c.get(Calendar.YEAR)
                        val monthly = c.get(Calendar.MONTH)
                        val day = c.get(Calendar.DATE)
                        val thisMonth = Date(year-1900, monthly, circleDay.value!!.minus(1))
                        val nextMonth = Date(year-1900, monthly+1, circleDay.value!!)
                        val lastMonth = Date(year-1900, monthly-1,circleDay.value!!.minus(1))
                        val futureDay = Date(year-1900, monthly, circleDay.value!!)
                        val today = Date(year-1900, monthly, day)
                        val timeformat = SimpleDateFormat("yyyyMMdd")
                        startTime = timeformat.format(thisMonth).toInt()
                        endTime = timeformat.format(nextMonth).toInt()
                        lastTime = timeformat.format(lastMonth).toInt()
                        futureTime = timeformat.format(futureDay).toInt()
                        thisDate.value = timeformat.format(today).toLong()
                        Log.d("Sophie_budget_time",
                            "$startTime + $endTime + $lastTime + $futureTime + ${time.value} + $thisDate")
                    }
                }
            }
    }


}