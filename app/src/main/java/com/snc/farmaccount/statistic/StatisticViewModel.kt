package com.snc.farmaccount.statistic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.snc.farmaccount.`object`.Event
import com.snc.farmaccount.`object`.StatisticCatalog
import com.snc.farmaccount.helper.UserManager
import java.util.*
import kotlin.collections.ArrayList

class StatisticViewModel : ViewModel() {

    private val _event = MutableLiveData<List<Event>>()
    val event: LiveData<List<Event>>
        get() = _event

//    private val _firebaseEvent = MutableLiveData<Event>()
//
//    val firebaseEvent: LiveData<Event>
//        get() = _firebaseEvent

    val catagory = MutableLiveData<StatisticCatalog>()
    private var year: Int = 0
    private var month:Int = 0
    private var day:Int = 0
    private var week:Int = 0
    private var weekName = ""
    private var pickMonth = MutableLiveData<String>()
    var currentMonth = MutableLiveData<String>()
    var date = MutableLiveData<String>()
    var DATE_MODE = ""


    val eventByCatagory: LiveData<List<Event>> = Transformations.map(catagory) { catalog ->
        catalog?.let {
            event.value?.filter {
                it.catalog == catalog.name
            }
        }
    }

    val eventByTotalPrice: LiveData<Int> = Transformations.map(eventByCatagory) { it ->
        it.sumBy { it.price!!.toInt() }
    }


    var dataList = ArrayList<Event>()
    lateinit var firebaseEvent : Event

    init {
        week()
    }

    fun getCurrentMonth() {
        pickMonth.value = currentMonth.value
    }

    fun getFirebase() {
        val db = FirebaseFirestore.getInstance()
        db.collection("User").document("${UserManager.userToken}").collection("Event")
            .whereEqualTo("month","${pickMonth.value}")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d("Sophie_db", "${document.id} => ${document.data}")
                        if (document.data != null) {
                            firebaseEvent = document.toObject(Event::class.java)
                            dataList.add(firebaseEvent)
                        } else {
                            Log.d("Sophie_db", "no data")
                        }

                    }
                }
                _event.value = dataList
                Log.w("Sophie_db_list", "$dataList")
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

        DATE_MODE = "$year.${month+1}.$day ($weekName)"

    }

}