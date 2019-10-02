package com.snc.farmaccount

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.simplemobiletools.commons.extensions.toBoolean
import com.simplemobiletools.commons.extensions.toInt
import com.snc.farmaccount.helper.UserManager
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel: ViewModel() {

    var year = 0
    var month = 0
    var day = 0
    var week = 0
    var startTime = 0L
    var endTime = 0L
    var lastTime = 0L
    var futureTime = 0L
    var allPrice = 0L
    var weekName = ""
    var dayMode = ""
    var overagePrice = MutableLiveData<String>()
    var totalPrice = MutableLiveData<Long>()
    var priceList = ArrayList<Long>()
    var pickdate = MutableLiveData<Int>()
    var maxDay = MutableLiveData<Int>()
    var tagStatus = MutableLiveData<Boolean>()
    var activityRestart = MutableLiveData<Boolean>()
    var isLogIn = MutableLiveData<Boolean>()
    var hasBudget = MutableLiveData<Boolean>()
    private val db = FirebaseFirestore.getInstance()
    private val calendar = Calendar.getInstance()

    init {
        pickdate.value = 1
        compareWithCycle()

        UserManager.userToken?.let {
            isLogIn.value = true
            getBudget()
        }
    }

    fun postCycleDay() {
        db.collection("User").document("${UserManager.userToken}")
            .collection("Budget")
            .document("${UserManager.userToken}")
            .update("cycleDay", "${pickdate.value}")
            .addOnSuccessListener {
                Log.d("Sophie_circle_edit", "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w("Sophie_circle_edit", "Error writing document", e)
            }
    }

    fun getCycleDay() {
        maxDay.value = calendar .getActualMaximum(Calendar.DAY_OF_MONTH)
        db.collection("User").document("${UserManager.userToken}")
            .collection("Budget")
            .document("${UserManager.userToken}")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    pickdate.value = document.data?.get("cycleDay")?.toInt()
                    setCurrentDate()
                }
            }
    }

    private fun compareWithCycle() {
        val today = calendar.get(Calendar.DAY_OF_MONTH)
        maxDay.value = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        when {
            today == pickdate.value -> {
                db.collection("User").document("${UserManager.userToken}")
                    .collection("Event")
                    .whereEqualTo("date", dayMode)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            Log.d("Sophie", "${document.id} => ${document.data}")
                            tagStatus.value = document.data["status"]?.toBoolean()
                            if (tagStatus.value == false) {
                                allPrice = document.data["price"]!!.toString().toLong()
                            }
                            if (tagStatus.value == true)  {
                                allPrice = 0-document.data["price"]!!.toString().toLong()
                            }
                            getPriceSum()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w("Sophie", "Error getting documents: ", exception)
                    }
            }

            today > pickdate.value!! -> {
                db.collection("User").document("${UserManager.userToken}")
                    .collection("Event")
                    .whereGreaterThan("time", startTime)
                    .whereLessThan("time", endTime)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            Log.d("Sophie", "${document.id} => ${document.data}")
                            tagStatus.value = document.data["status"]?.toBoolean()
                            if (tagStatus.value == false) {
                                allPrice = document.data["price"]!!.toString().toLong()
                            }
                            if (tagStatus.value == true)  {
                                allPrice = 0-document.data["price"]!!.toString().toLong()
                            }
                            getPriceSum()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w("Sophie", "Error getting documents: ", exception)
                    }
            }

            today < pickdate.value!! -> {
                db.collection("User").document("${UserManager.userToken}")
                    .collection("Event")
                    .whereGreaterThan("time", lastTime)
                    .whereLessThan("time", futureTime)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            Log.d("Sophie", "${document.id} => ${document.data}")
                            tagStatus.value = document.data["status"]?.toBoolean()
                            if (tagStatus.value == false) {
                                allPrice = document.data["price"]!!.toString().toLong()
                            }
                            if (tagStatus.value == true)  {
                                allPrice = 0-document.data["price"]!!.toString().toLong()
                            }
                            getPriceSum()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w("Sophie", "Error getting documents: ", exception)
                    }
            }
        }
    }

    private fun getPriceSum() {
        priceList.add(allPrice)
        if (priceList.isEmpty()) {
            totalPrice.value = 0
        } else {
            Log.i("Sophie_minus", "$priceList")
            totalPrice.value = priceList.sum()
        }
        updateOverage()
    }

    private fun updateOverage() {
        db.collection("User").document("${UserManager.userToken}")
            .collection("Budget")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d("Sophie_db", "${document.id} => ${document.data}")
                        if (document.data != null) {
                            overagePrice.value = (document.data["budgetPrice"]!!.toInt()- totalPrice.value!!).toString()
                            db.collection("User").document("${UserManager.userToken}")
                                .collection("Budget")
                                .document("${UserManager.userToken}")
                                .update("overage", "${overagePrice.value}")
                                .addOnSuccessListener {
                                    Log.d("Sophie_budget_edit", "DocumentSnapshot successfully written!")
                                }
                                .addOnFailureListener { e ->
                                    Log.w("Sophie_budget_edit", "Error writing document", e)
                                }
                        } else {
                            Log.d("Sophie_db", "no data")
                        }
                    }

                }

            }
    }

    private fun getBudget() {
        db.collection("User").document("${UserManager.userToken}")
            .collection("Budget").document("${UserManager.userToken}")
            .get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    hasBudget.value = false
                    Log.d("Sophie", "DocumentSnapshot data: ${document.data}")
                    Log.d("Sophie", "${hasBudget.value}")

                } else {
                    hasBudget.value = true
                    Log.d("Sophie", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Sophie", "get failed with ", exception)
            }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setCurrentDate() {
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)
        week = calendar.get(Calendar.DAY_OF_WEEK)

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

        if (pickdate.value == null) {
            pickdate.value = 1
        }

        val thisMonth = Date(year-1900, month, pickdate.value!!.minus(1))
        val nextMonth = Date(year-1900, month+1, pickdate.value!!)
        val lastMonth = Date(year-1900, month-1,pickdate.value!!.minus(1))
        val futureDay = Date(year-1900, month, pickdate.value!!)
        val timeFormat = SimpleDateFormat("yyyyMMdd")
        startTime = timeFormat.format(thisMonth).toLong()
        endTime = timeFormat.format(nextMonth).toLong()
        lastTime = timeFormat.format(lastMonth).toLong()
        futureTime = timeFormat.format(futureDay).toLong()

        val getDate = Date(year-1900, month, day)
        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd (EEEE)")
        dayMode = simpleDateFormat.format(getDate)
        compareWithCycle()
    }




}