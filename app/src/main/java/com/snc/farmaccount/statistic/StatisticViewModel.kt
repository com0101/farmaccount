package com.snc.farmaccount.statistic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.snc.farmaccount.`object`.Event
import com.snc.farmaccount.helper.UserManager

class StatisticViewModel : ViewModel() {

    private val _event = MutableLiveData<List<Event>>()
    val event: LiveData<List<Event>>
        get() = _event

    var dataList = ArrayList<Event>()
    lateinit var firebaseEvent : Event

    init {

    }

    fun getFirebase() {
        val db = FirebaseFirestore.getInstance()
        db.collection("User").document("${UserManager.userToken}").collection("Event")
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

}