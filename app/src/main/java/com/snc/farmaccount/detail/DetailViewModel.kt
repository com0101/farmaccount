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
import com.snc.farmaccount.helper.*
import java.text.SimpleDateFormat
import java.util.*

class DetailViewModel(product: Event, app: Application) : AndroidViewModel(app) {

    var startTime = 0
    var endTime = 0
    var lastTime = 0
    var futureTime = 0
    var price = 0L
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
        time.value = detail.value?.time
        getOverage()
    }

    fun deleteEvent() {
        db.collection(USER).document("${UserManager.userToken}")
            .collection(EVENT)
            .whereEqualTo(ID, detail.value?.id)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d(SOPHIE, "deleteEvent:${document.id} => ${document.data}")

                        db.collection(USER).document("${UserManager.userToken}")
                            .collection(EVENT)
                            .document(document.id)
                            .delete()
                            .addOnSuccessListener { documentReference ->
                                Log.d(
                                    SOPHIE,
                                    "DocumentSnapshot delete with ID: $documentReference"
                                )
                                compareWithCycle()
                            }
                            .addOnFailureListener { e ->
                                Log.w("SOPHIE", "Error delete document", e)
                            }
                    }
                }
            }

    }

    private fun compareWithCycle() {
        when {
            thisDate.value?:0 in lastTime  until futureTime -> {
                if (time.value?:0 in lastTime until futureTime) {
                    price = detail.value!!.price!!.toLong()
                    updateOverage()
                }
            }

            thisDate.value?:0 in startTime until endTime -> {
                if (time.value?:0 in startTime until endTime) {
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

        when (detail.value?.status) {
            true -> overagePrice.value = (overageInt?.minus(price)).toString()
            else -> overagePrice.value = (overageInt?.plus(price)).toString()
        }

        db.collection(USER).document("${UserManager.userToken}")
            .collection(BUDGET)
            .document("${UserManager.userToken}")
            .update(OVERAGE,"${overagePrice.value}")
            .addOnSuccessListener {
                Log.d(SOPHIE, "DocumentSnapshot successfully updateOverage!")
            }
            .addOnFailureListener { e ->
                Log.w(SOPHIE, "Error updateOverage document", e)
            }
    }

    private fun getOverage() {
        db.collection(USER).document("${UserManager.userToken}")
            .collection(BUDGET)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        overagePrice.value = document.data[OVERAGE].toString()
                        circleDay.value = document.data[CYCLE_DAY]?.toInt()
                        timeFormat()
                    }
                }
            }
    }

    private fun timeFormat() {
        val year = calendar.get(Calendar.YEAR)
        val monthly = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DATE)
        val thisMonth = Date(year-1900, monthly, circleDay.value?:0)
        val nextMonth = Date(year-1900, monthly+1, circleDay.value?:0.minus(1))
        val lastMonth = Date(year-1900, monthly-1,circleDay.value?:0)
        val futureDay = Date(year-1900, monthly, circleDay.value?:0.minus(1))
        val today = Date(year-1900, monthly, day)

        startTime = Format.getDateFormat(thisMonth).toInt()
        endTime = Format.getDateFormat(nextMonth).toInt()
        lastTime = Format.getDateFormat(lastMonth).toInt()
        futureTime = Format.getDateFormat(futureDay).toInt()
        thisDate.value = Format.getDateFormat(today).toLong()
    }



}