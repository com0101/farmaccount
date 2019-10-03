package com.snc.farmaccount

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.simplemobiletools.commons.extensions.toBoolean
import com.simplemobiletools.commons.extensions.toInt
import com.snc.farmaccount.budget.BudgetViewModel
import com.snc.farmaccount.helper.*
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
        db.collection(USER).document("${UserManager.userToken}")
            .collection(BUDGET)
            .document("${UserManager.userToken}")
            .update(CYCLE_DAY, "${pickdate.value}")
            .addOnSuccessListener {
                Log.d(SOPHIE, "DocumentSnapshot successfully postCycleDay!")
            }
            .addOnFailureListener { e ->
                Log.w(SOPHIE, "Error postCycleDay document", e)
            }
    }

    fun getCycleDay() {
        maxDay.value = calendar .getActualMaximum(Calendar.DAY_OF_MONTH)
        db.collection(USER).document("${UserManager.userToken}")
            .collection(BUDGET)
            .document("${UserManager.userToken}")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    pickdate.value = document.data?.get(CYCLE_DAY)?.toInt()
                    setCurrentDate()
                }
            }
    }

    private fun compareWithCycle() {
        val today = calendar.get(Calendar.DAY_OF_MONTH)
        maxDay.value = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        when {
            today == pickdate.value -> {
                db.collection(USER).document("${UserManager.userToken}")
                    .collection(EVENT)
                    .whereEqualTo(DATE, dayMode)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            tagStatus.value = document.data[STATUS]?.toBoolean()

                            when(tagStatus.value) {
                                false -> allPrice = document.data[PRICE]!!.toString().toLong()
                                true -> allPrice = 0-document.data[PRICE]!!.toString().toLong()
                            }
                            getPriceSum()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(SOPHIE, "Error getting event documents: ", exception)
                    }
            }

            today > pickdate.value!! -> {
                db.collection(USER).document("${UserManager.userToken}")
                    .collection(EVENT)
                    .whereGreaterThan(TIME, startTime)
                    .whereLessThan(TIME, endTime)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            tagStatus.value = document.data[STATUS]?.toBoolean()

                            when(tagStatus.value) {
                                false -> allPrice = document.data[PRICE]!!.toString().toLong()
                                true -> allPrice = 0-document.data[PRICE]!!.toString().toLong()
                            }
                            getPriceSum()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(SOPHIE, "Error getting event documents: ", exception)
                    }
            }

            today < pickdate.value!! -> {
                db.collection(USER).document("${UserManager.userToken}")
                    .collection(EVENT)
                    .whereGreaterThan(TIME, lastTime)
                    .whereLessThan(TIME, futureTime)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            tagStatus.value = document.data[STATUS]?.toBoolean()

                            when(tagStatus.value) {
                                false -> allPrice = document.data[PRICE]!!.toString().toLong()
                                true -> allPrice = 0-document.data[PRICE]!!.toString().toLong()
                            }
                            getPriceSum()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(SOPHIE, "Error getting event documents: ", exception)
                    }
            }
        }
    }

    private fun getPriceSum() {
        priceList.add(allPrice)

        when {
            priceList.isEmpty() -> totalPrice.value = 0
            else -> totalPrice.value = priceList.sum()
        }
        updateOverage()
    }

    private fun updateOverage() {
        db.collection(USER).document("${UserManager.userToken}")
            .collection(BUDGET)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        document.data.let {
                            overagePrice.value =
                                (document.data[BUDGET_PRICE]!!.toInt()-totalPrice.value!!).toString()

                            db.collection(USER).document("${UserManager.userToken}")
                                .collection(BUDGET)
                                .document("${UserManager.userToken}")
                                .update(OVERAGE, "${overagePrice.value}")
                                .addOnSuccessListener {
                                    Log.d(SOPHIE, "DocumentSnapshot successfully updateOverage!")
                                }
                                .addOnFailureListener { e ->
                                    Log.w(SOPHIE, "Error updateOverage document", e)
                                }
                        }
                    }

                }

            }
    }

    private fun getBudget() {
        db.collection(USER).document("${UserManager.userToken}")
            .collection(BUDGET).document("${UserManager.userToken}")
            .get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    hasBudget.value = false
                    Log.d(SOPHIE, "DocumentSnapshot getBudget data: ${document.data}")

                } else {
                    hasBudget.value = true
                    Log.d(SOPHIE, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(SOPHIE, "getBudget failed with ", exception)
            }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setCurrentDate() {
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)
        week = calendar.get(Calendar.DAY_OF_WEEK)

        if (week == 1) {
            weekName = ApplicationContext.applicationContext().getString(R.string.sunday)
        }
        if (week == 2) {
            weekName = ApplicationContext.applicationContext().getString(R.string.monday)
        }
        if (week == 3) {
            weekName = ApplicationContext.applicationContext().getString(R.string.tuesday)
        }
        if (week == 4) {
            weekName = ApplicationContext.applicationContext().getString(R.string.wednesday)
        }
        if (week == 5) {
            weekName = ApplicationContext.applicationContext().getString(R.string.thursday)
        }
        if (week == 6) {
            weekName = ApplicationContext.applicationContext().getString(R.string.friday)
        }
        if (week == 7) {
            weekName = ApplicationContext.applicationContext().getString(R.string.sunday)
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