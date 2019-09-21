package com.snc.farmaccount

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.simplemobiletools.commons.extensions.toInt
import com.snc.farmaccount.`object`.Event
import com.snc.farmaccount.helper.UserManager
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel: ViewModel() {

    private var year: Int = 0
    private var month: Int = 0
    private var day:Int = 0
    private var week:Int = 0
    private var weekName = ""
    var DATE_MODE = ""
    var startTime: Long = 0
    var endTime: Long = 0
    var overagePrice = MutableLiveData<String>()
    var totalPrice = MutableLiveData<Int>()
    var priceList = ArrayList<Int>()
    var pickdate = MutableLiveData<Int>()
    var maxDay = MutableLiveData<Int>()


    init {
        pickdate.value = 1
//        week()
        updateOverage()
    }

    fun postCircleDay() {
        val db = FirebaseFirestore.getInstance()
        db.collection("User").document("${UserManager.userToken}").collection("Budget")
            .document("${UserManager.userToken}")
            .update("circleDay", "${pickdate.value}")
            .addOnSuccessListener { Log.d("Sophie_circle_edit", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("Sophie_circle_edit", "Error writing document", e) }
    }

    fun getCircle() {
        val db = FirebaseFirestore.getInstance()
        var lastDay = Calendar.getInstance()
        var today = lastDay.get(Calendar.DAY_OF_MONTH)
        maxDay.value = lastDay.getActualMaximum(Calendar.DAY_OF_MONTH)
        db.collection("User").document("${UserManager.userToken}").collection("Budget")
            .document("${UserManager.userToken}")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    pickdate.value = document.data?.get("circleDay")!!.toInt()
                    week()

                }
            }
    }

    private fun updateOverage() {
        val db = FirebaseFirestore.getInstance()
        var lastDay = Calendar.getInstance()
        var today = lastDay.get(Calendar.DAY_OF_MONTH)
        maxDay.value = lastDay.getActualMaximum(Calendar.DAY_OF_MONTH)
        Log.d("Sophie_today", "$today+${pickdate.value}+$DATE_MODE")
        if (today == pickdate.value) {
            db.collection("User").document("${UserManager.userToken}").collection("Event")
                .whereEqualTo("time","$DATE_MODE")
                .get()
                .addOnSuccessListener { documents ->
                    if (documents != null) {
                        Log.d("Sophie", "DocumentSnapshot data: ${documents.documents}")
                        if (documents.documents.isEmpty()) {
                            totalPrice.value = 0

                        } else {
                            for (document in documents) {
                                Log.d("Sophie", "${document.id} => ${document.data}")
                                priceList.add(document.data["price"]!!.toInt())
                                totalPrice.value = priceList.sum()
                                Log.d("Sophie_list", "${totalPrice.value}")
                            }
                        }

                        db.collection("User").document("${UserManager.userToken}").collection("Budget")
                            .get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    for (document in task.result!!) {
                                        Log.d("Sophie_db", "${document.id} => ${document.data}")
                                        if (document.data != null) {
                                            Log.d("Sophie_list", "${totalPrice.value}")
                                            overagePrice.value = (document.data["budgetPrice"]!!.toInt()- totalPrice.value!!).toString()
                                            Log.d("Sophie_ overagePrice.value", "${overagePrice.value}")
                                            db.collection("User").document("${UserManager.userToken}").collection("Budget")
                                                .document("${UserManager.userToken}")
                                                .update("overage", "${overagePrice.value}")
                                                .addOnSuccessListener {
                                                    Log.d("Sophie_list", "${overagePrice.value}")
                                                    Log.d("Sophie_budget_edit", "DocumentSnapshot successfully written!")
                                                }
                                                .addOnFailureListener { e -> Log.w("Sophie_budget_edit", "Error writing document", e) }
                                        } else {
                                            Log.d("Sophie_db", "no data")
                                        }
                                    }

                                }

                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("Sophie", "Error getting documents: ", exception)
                }
        }
        else {
            Log.d("Sophie", "not today")
            db.collection("User").document("${UserManager.userToken}").collection("Event")
                .whereGreaterThan("time", startTime)
                .whereLessThan("time", endTime)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        Log.d("Sophie", "${document.id} => ${document.data}")
                        priceList.add(document.data["price"]!!.toInt())
                        if (priceList.isEmpty()) {
                            totalPrice.value = 0
                        } else {
                            totalPrice.value = priceList.sum()
                        }
                        Log.d("Sophie_list", "${totalPrice.value}")
                        db.collection("User").document("${UserManager.userToken}").collection("Budget")
                            .get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    for (document in task.result!!) {
                                        Log.d("Sophie_db", "${document.id} => ${document.data}")
                                        if (document.data != null) {
                                            overagePrice.value = (document.data["budgetPrice"]!!.toInt()- totalPrice.value!!).toString()
                                            db.collection("User").document("${UserManager.userToken}").collection("Budget")
                                                .document("${UserManager.userToken}")
                                                .update("overage", "${overagePrice.value}")
                                                .addOnSuccessListener { Log.d("Sophie_budget_edit", "DocumentSnapshot successfully written!") }
                                                .addOnFailureListener { e -> Log.w("Sophie_budget_edit", "Error writing document", e) }
                                        } else {
                                            Log.d("Sophie_db", "no data")
                                        }
                                    }

                                }

                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("Sophie", "Error getting documents: ", exception)
                }
        }
    }


    private fun week() {
        val c = Calendar.getInstance()
        year = c.get(Calendar.YEAR)
        month = c.get(Calendar.MONTH)
        day = c.get(Calendar.DAY_OF_MONTH)
        week = c.get(Calendar.DAY_OF_WEEK)

        if (week==1) {
            weekName = "星期日"
        }
        if (week==2) {
            weekName = "星期一"
        }
        if (week==3) {
            weekName = "星期二"
        }
        if (week==4) {
            weekName = "星期三"
        }
        if (week==5) {
            weekName = "星期四"
        }
        if (week==6) {
            weekName = "星期五"
        }
        if (week==7) {
            weekName = "星期六"
        }
        val getDate = Date(year-1900, month, day)
        val thisMonth = Date(year-1900, month, pickdate.value!!.minus(1))
        val nextMonth = Date(year-1900, month+1, pickdate.value!!)
        val simpledateformat = SimpleDateFormat("yyyy.MM.dd (EEEE)")
        val timeformat = SimpleDateFormat("yyyyMMdd")
        startTime = timeformat.format(thisMonth).toLong()
        endTime = timeformat.format(nextMonth).toLong()
        DATE_MODE = simpledateformat.format(getDate)
        Log.i("today","$endTime + $startTime")
        updateOverage()
    }
}