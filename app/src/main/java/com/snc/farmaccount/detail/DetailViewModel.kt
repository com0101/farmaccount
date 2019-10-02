package com.snc.farmaccount.detail

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.simplemobiletools.commons.extensions.toInt
import com.snc.farmaccount.`object`.Event
import com.snc.farmaccount.helper.UserManager
import java.text.SimpleDateFormat
import java.util.*

class DetailViewModel(product: Event, app: Application) : AndroidViewModel(app) {

    var startTime = 0
    var endTime = 0
    var lastTime = 0
    var futureTime = 0
    var price = 0L
    var infoInput = MutableLiveData<String>()
    var overagePrice = MutableLiveData<String>()
    var circleDay = MutableLiveData<Int>()
    var time = MutableLiveData<Long>()
    var thisDate = MutableLiveData<Long>()
    private val db = FirebaseFirestore.getInstance()
    private val calendar = Calendar.getInstance()

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
        db.collection("User").document("${UserManager.userToken}")
            .collection("Event")
            .whereEqualTo("id", detail.value?.id)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d("Sophie_db_id", "${document.id} => ${document.data}")
                        db.collection("User").document("${UserManager.userToken}")
                            .collection("Event")
                            .document("${document.id}")
                            .delete()
                            .addOnSuccessListener { documentReference ->
                                Log.d(
                                    "Sophie_update",
                                    "DocumentSnapshot added with ID: $documentReference"
                                )
                                compareWithCycle()
                            }
                            .addOnFailureListener { e ->
                                Log.w("Sophie_add_fail", "Error adding document", e)
                            }
                    }
                }
            }

    }

    private fun compareWithCycle() {
        when {
            thisDate.value!!.toInt() in lastTime  until futureTime -> {
                if (time.value!!.toInt() in lastTime until futureTime) {
                    price = detail.value!!.price!!.toLong()
                    updateOverage()
                }
            }
            thisDate.value!!.toInt() in startTime until endTime -> {
                if (time.value!!.toInt() in startTime until endTime) {
                    price = detail.value!!.price!!.toLong()
                    updateOverage()
                }
            }
            else -> {
                price = 0
                updateOverage()
            }
        }
    }

    private fun updateOverage() {
        val overageInt = overagePrice.value?.toLong()
        if (detail.value!!.status == true) {
            overagePrice.value = (overageInt?.minus(price)).toString()
        } else {
            overagePrice.value = (overageInt?.plus(price)).toString()
        }

        db.collection("User").document("${UserManager.userToken}")
            .collection("Budget")
            .document("${UserManager.userToken}")
            .update("overage","${overagePrice.value}")
            .addOnSuccessListener {
                Log.d("Sophie_budget_edit", "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w("Sophie_budget_edit", "Error writing document", e)
            }
    }

    private fun getOverage() {
        db.collection("User").document("${UserManager.userToken}")
            .collection("Budget")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d("Sophie_db", "${document.id} => ${document.data["overage"]}")
                        overagePrice.value = document.data["overage"].toString()
                        circleDay.value = document.data["cycleDay"]?.toInt()
                        timeFormat()
                    }
                }
            }
    }

    @SuppressLint("SimpleDateFormat")
    private fun timeFormat() {
        val year = calendar.get(Calendar.YEAR)
        val monthly = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DATE)
        val thisMonth = Date(year-1900, monthly, circleDay.value?:0)
        val nextMonth = Date(year-1900, monthly+1, circleDay.value?:0.minus(1))
        val lastMonth = Date(year-1900, monthly-1,circleDay.value?:0)
        val futureDay = Date(year-1900, monthly, circleDay.value?:0.minus(1))
        val today = Date(year-1900, monthly, day)
        val timeFormat = SimpleDateFormat("yyyyMMdd")
        startTime = timeFormat.format(thisMonth).toInt()
        endTime = timeFormat.format(nextMonth).toInt()
        lastTime = timeFormat.format(lastMonth).toInt()
        futureTime = timeFormat.format(futureDay).toInt()
        thisDate.value = timeFormat.format(today).toLong()
    }



}