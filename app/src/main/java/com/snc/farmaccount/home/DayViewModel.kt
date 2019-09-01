package com.snc.farmaccount.home

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.snc.farmaccount.`object`.Event
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DayViewModel: ViewModel() {

    private var year: Int = 0
    private var month:Int = 0
    private var day:Int = 0
    private var week:Int = 0
    private var weekName = ""
    var date = MutableLiveData<String>()
    private var pickDate = MutableLiveData<String>()
    lateinit var firebaseEvent : Event
    var dataList = ArrayList<Event>()

    private val _event = MutableLiveData<List<Event>>()
    val event: LiveData<List<Event>>
        get() = _event

    var DATE_MODE = ""

    init {
        pickDate
        week()
    }

    fun getFirebase() {
        val db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build()
        db.firestoreSettings = settings
        db.collection("User")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val sub = FirebaseFirestore.getInstance()
                        sub.collection("User/${document.id}/Event")
                            .get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    for (document in task.result!!) {
                                        val timestamp = document["date"] as com.google.firebase.Timestamp
                                        val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
                                        val sdf = SimpleDateFormat("yyyy.MM.dd (EEEE)")
                                        val netDate = Date(milliseconds)
                                        val date = sdf.format(netDate).toString()
                                        firebaseEvent = document.toObject(Event::class.java)
                                        dataList.add(firebaseEvent)
                                    }

                                } else {
                                    Log.w("Sophie_db_fail", "Error getting documents.", task.exception)
                                }
                                _event.value = dataList
                                Log.w("Sophie_db_list", "$dataList")
                            }
                    }
                }
            }
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

}