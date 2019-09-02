package com.snc.farmaccount.home

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.snc.farmaccount.`object`.Event
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList




class DayViewModel: ViewModel() {

    private var year: Int = 0
    private var month:Int = 0
    private var day:Int = 0
    private var week:Int = 0
    private var weekName = ""
    private var pickDate = MutableLiveData<String>()
    lateinit var firebaseEvent : Event
    var currentDate = MutableLiveData<String>()
    var date = MutableLiveData<String>()
    var dataList = ArrayList<Event>()
    var DATE_MODE = ""

    private val _event = MutableLiveData<List<Event>>()
    val event: LiveData<List<Event>>
        get() = _event

    private val _navigateToDetail = MutableLiveData<Event>()
    val navigateToDetail: LiveData<Event>
        get() = _navigateToDetail

    init {
        pickDate
        week()
    }

    fun getFirebase() {
        val db = FirebaseFirestore.getInstance()
        db.collection("User").document("LAkilE0ErjYqmncg1cVq").collection("Event")
            .whereEqualTo("date","${pickDate.value}")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d("Sophie_db", "${document.id} => ${document.data}")
                        firebaseEvent = document.toObject(Event::class.java)
                        dataList.add(firebaseEvent)

//                        val sub = FirebaseFirestore.getInstance()
//                        sub.collection("User/${document.id}/Event")
//                            .get()
//                            .addOnCompleteListener { task ->
//                                if (task.isSuccessful) {
//                                    for (document in task.result!!) {
//                                        val timestamp = document["date"] as com.google.firebase.Timestamp
//                                        val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
//                                        val sdf = SimpleDateFormat("yyyy.MM.dd (EEEE)")
//                                        val netDate = Date(milliseconds)
//                                        val date = sdf.format(netDate).toString()
//                                        firebaseEvent = document.toObject(Event::class.java)
//                                        dataList.add(firebaseEvent)
//                                    }
//
//                                } else {
//                                    Log.w("Sophie_db_fail", "Error getting documents.", task.exception)
//                                }
//                                _event.value = dataList
//                                Log.w("Sophie_db_list", "$dataList")
//                            }
                    }
                }
                _event.value = dataList
                Log.w("Sophie_db_list", "$dataList")
            }
    }

    fun getCurrentDate() {
        pickDate.value = currentDate.value
    }

    private fun week() {
        val c = Calendar.getInstance()
        year = c.get(Calendar.YEAR)
        month = c.get(Calendar.MONTH)
        day = c.get(Calendar.DAY_OF_MONTH)
        week = c.get(Calendar.DAY_OF_WEEK)

        if(week==1) {
            weekName = "星期日"
        }
        if(week==2) {
            weekName = "星期一"
        }
        if(week==3) {
            weekName = "星期二"
        }
        if(week==4) {
            weekName = "星期三"
        }
        if(week==5) {
            weekName = "星期四"
        }
        if(week==6) {
            weekName = "星期五"
        }
        if(week==7) {
            weekName = "星期六"
        }
        DATE_MODE = "$year.${month+1}.$day ($weekName)"

    }

    fun displayPropertyDetails(product: Event) {
        _navigateToDetail.value = product
    }

    fun displayPropertyDetailsComplete() {
        _navigateToDetail.value = null
    }

}