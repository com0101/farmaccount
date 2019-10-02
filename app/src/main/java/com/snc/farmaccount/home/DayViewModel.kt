package com.snc.farmaccount.home

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.simplemobiletools.commons.extensions.toInt
import com.snc.farmaccount.`object`.Event
import com.snc.farmaccount.helper.UserManager
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
        db.collection("User").document("${UserManager.userToken}")
            .collection("Event")
            .orderBy("id", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d("Sophie_db", "${document.id} => ${document.data}")
                        if (document.data != null) {
                            firebaseEvent = document.toObject(Event::class.java)
                            eventList.add(firebaseEvent)
                        } else {
                            Log.d("Sophie_db", "no data")
                        }
                    }
                }
                _event.value = eventList
                Log.w("Sophie_db_list", "$eventList")
            }
    }

    fun getOverage() {
        val decimalFormat = DecimalFormat("#,###")
        db.collection("User").document("${UserManager.userToken}")
            .collection("Budget")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        overagePrice.value = decimalFormat.format(document.data["overage"].toString().toDouble())
                        postPrice.value = document.data["overage"].toString().toLong()
                        farmStatus.value = document.data["position"]?.toInt()
                    }
                }
                Log.d("Sophie_db", "overagePrice.value = ${farmStatus.value}")
            }
    }


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

        dateMode = "$year.${month+1}.$day ($weekName)"

    }

    fun displayPropertyDetails(product: Event) {
        _navigateToDetail.value = product
    }

    fun displayPropertyDetailsComplete() {
        _navigateToDetail.value = null
    }

}