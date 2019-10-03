package com.snc.farmaccount.home

import android.util.Log
import androidx.lifecycle.*
import com.firebase.ui.auth.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.simplemobiletools.commons.extensions.toInt
import com.snc.farmaccount.ApplicationContext
import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.Event
import com.snc.farmaccount.helper.*
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class DayViewModel: ViewModel() {

    var year = 0
    var month = 0
    var day = 0
    var week = 0
    var weekName = ""
    var dateMode = ""
    var currentDate = MutableLiveData<String>()
    var postPrice = MutableLiveData<Long>()
    var overagePrice = MutableLiveData<String>()
    var farmStatus = MutableLiveData<Int>()
    var eventList = ArrayList<Event>()
    private val calendar = Calendar.getInstance()
    private val db = FirebaseFirestore.getInstance()
    lateinit var firebaseEvent : Event

    private val _event = MutableLiveData<List<Event>>()
    val event: LiveData<List<Event>>
        get() = _event

    private val _navigateToDetail = MutableLiveData<Event>()
    val navigateToDetail: LiveData<Event>
        get() = _navigateToDetail

    init {
        setCurrentDate()
        getOrderBy()
    }

    val sortByTime: LiveData<List<Event>> = Transformations.map(event) { it ->
        it.filter {
            it.date == "${currentDate.value}"
        }
    }

    private fun getOrderBy() {
        db.collection(USER).document("${UserManager.userToken}")
            .collection(EVENT)
            .orderBy(ID, Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d(SOPHIE, "getOrderBy:${document.id} => ${document.data}")
                        firebaseEvent = document.toObject(Event::class.java)
                        eventList.add(firebaseEvent)
                    }
                }
                _event.value = eventList
                Log.w(SOPHIE, "getOrderBy:$eventList")
            }
    }

    fun getOverage() {
        val decimalFormat = DecimalFormat("#,###")
        db.collection(USER).document("${UserManager.userToken}")
            .collection(BUDGET)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        overagePrice.value = decimalFormat.format(
                            document.data[OVERAGE].toString().toDouble())
                        postPrice.value = document.data[OVERAGE].toString().toLong()
                        farmStatus.value = document.data[POSITION]?.toInt()
                    }
                }
            }
    }

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

        dateMode = "$year.${month+1}.$day ($weekName)"

    }

    fun displayPropertyDetails(product: Event) {
        _navigateToDetail.value = product
    }

    fun displayPropertyDetailsComplete() {
        _navigateToDetail.value = null
    }

}