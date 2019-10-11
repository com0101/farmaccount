package com.snc.farmaccount.home

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.simplemobiletools.commons.extensions.toInt
import com.snc.farmaccount.`object`.Event
import com.snc.farmaccount.helper.*
import java.util.*
import kotlin.collections.ArrayList

class DayViewModel: ViewModel() {

    var dateMode = ""
    var currentDate = MutableLiveData<String>()
    var postPrice = MutableLiveData<Long>()
    var overagePrice = MutableLiveData<String>()
    var farmStatus = MutableLiveData<Int>()
    var eventList = ArrayList<Event>()
    private val db = FirebaseFirestore.getInstance()

    private val _event = MutableLiveData<List<Event>>()
    val event: LiveData<List<Event>>
        get() = _event

    private val _navigateToDetail = MutableLiveData<Event>()
    val navigateToDetail: LiveData<Event>
        get() = _navigateToDetail

    init {
        dateMode = Format.getCurrentDate()
        getOrderBy()
    }

    val sortByDate: LiveData<List<Event>> = Transformations.map(event) { it ->
        it.filter {
            it.date == "${currentDate.value}" // filter by current date
        }
    }

    private fun getOrderBy() { // sort event by create time
        db.collection(USER).document("${UserManager.userToken}")
            .collection(EVENT)
            .orderBy(ID, Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d(SOPHIE, "getOrderBy:${document.id} => ${document.data}")
                        eventList.add(document.toObject(Event::class.java))
                    }
                }
                _event.value = eventList
                Log.w(SOPHIE, "getOrderBy:$eventList")
            }
    }

    fun getOverage() {
        db.collection(USER).document("${UserManager.userToken}")
            .collection(BUDGET)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        postPrice.value = document.data[OVERAGE].toString().toLong()
                        overagePrice.value = Format.decimalFormat(postPrice.value.toString().toDouble())
                        farmStatus.value = document.data[POSITION]?.toInt()
                    }
                }
            }
    }

    fun displayPropertyDetails(product: Event) {
        _navigateToDetail.value = product
    }

    fun displayPropertyDetailsComplete() {
        _navigateToDetail.value = null
    }

}