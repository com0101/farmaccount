package com.snc.farmaccount.event

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.simplemobiletools.commons.extensions.toBoolean
import com.simplemobiletools.commons.extensions.toInt
import com.snc.farmaccount.`object`.Event
import com.snc.farmaccount.`object`.Tag
import com.snc.farmaccount.helper.UserManager
import java.text.SimpleDateFormat
import java.util.*

class EditEventViewModel(product: Event, app: Application) : AndroidViewModel(app) {

    var mark = MutableLiveData<List<Tag>>()
    var priceInput = MutableLiveData<String>()
    var infoInput = MutableLiveData<String>()
    var chooseTag = MutableLiveData<Tag>()
    var today = MutableLiveData<String>()
    var month :String = ""
    var overagePrice = MutableLiveData<String>()
    var time = MutableLiveData<Long>()
    var circleDay = MutableLiveData<Int>()
    var startTime = 0
    var endTime = 0
    var lastTime = 0
    var futureTime = 0
    var price = 0L
    var tagName = MutableLiveData<String>()
    var tagStatus = MutableLiveData<Boolean>()
    var category = MutableLiveData<String>()

    private val _tag = MutableLiveData<List<Tag>>()
    val tag: LiveData<List<Tag>>
        get() = _tag

    private val _detail = MutableLiveData<Event>()
    val detail: LiveData<Event>
        get() = _detail

    init {
        _detail.value = product
        priceInput.value = detail.value?.price
        infoInput.value = detail.value?.description
        today.value = detail.value?.date
        month = detail.value?.month!!
        time.value = detail.value?.time
        tagName.value = detail.value?.tag
        getOverage()
        Log.i("tag","${detail.value?.status}")
    }



    fun editFirebase() {
        val db = FirebaseFirestore.getInstance()
        val currentTimestamp = System.currentTimeMillis()
        // Create a new user with a first and last name
        val event = HashMap<String,Any>()

        if (chooseTag.value != null) {
            tagName.value = chooseTag.value?.tag_name
            tagStatus.value = chooseTag.value?.tag_status
            category.value = chooseTag.value?.tag_catalog
        } else {
            tagName.value = detail.value?.tag
            tagStatus.value = detail.value?.status
            category.value = detail.value?.catalog
        }
        event["price"] = priceInput.value!!
        event["tag"] = tagName.value.toString()
        event["description"] = infoInput.value!!
        event["date"] = today.value!!
        event["time"] = time.value!!
        event["status"] = tagStatus.value?:true
        event["month"] = month
        event["catalog"] = category.value.toString()
        // Add a new document with a generated ID

        db.collection("User").document("${UserManager.userToken}").collection("Event")
            .whereEqualTo("description","${detail.value?.description}")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d("Sophie_db", "${document.id} => ${document.data}")
                        db.collection("User").document("${UserManager.userToken}").collection("Event")
                            .document("${document.id}")
                            .update(event)
                            .addOnSuccessListener { documentReference ->
                                Log.d(
                                    "Sophie_update",
                                    "DocumentSnapshot added with ID: $documentReference"
                                )
                                when {
                                    time.value!!.toInt() in (lastTime + 1) until (futureTime-1) -> {
                                        updateOverage()
                                        Log.d("Sophie_budget_over",
                                            "in!")
                                    }
                                    time.value!!.toInt() in (startTime + 1) until (endTime-1) -> {
                                        updateOverage()
                                        Log.d("Sophie_budget_over",
                                            "inagain!")
                                    }
                                }
                            }
                            .addOnFailureListener { e -> Log.w("Sophie_add_fail", "Error adding document", e) }

                    }
                }
            }

    }

    private fun updateOverage() {
        val db = FirebaseFirestore.getInstance()
        var overageInt = overagePrice.value?.toInt()
        if (tagStatus.value != detail.value?.status) {

            if (priceInput.value != detail.value?.price) {
                price = (detail.value?.price!!.toLong())*2+
                        (priceInput.value!!.toLong()-detail.value?.price!!.toLong())
                if (chooseTag.value?.tag_status == true) {
                    overagePrice.value = (overageInt?.plus(price)).toString()
                } else {
                    overagePrice.value = (overageInt?.minus(price)).toString()
                }
            } else {
                price = priceInput.value!!.toLong()
                if (chooseTag.value?.tag_status == true) {
                    overagePrice.value = (overageInt?.plus(price*2)).toString()
                } else {
                    overagePrice.value = (overageInt?.minus(price*2)).toString()
                }
            }

        } else {
            if (priceInput.value != detail.value?.price) {
                price = priceInput.value!!.toLong()-detail.value?.price!!.toLong()
                if (tagStatus.value == true) {
                    overagePrice.value = (overageInt?.plus(price)).toString()
                } else {
                    overagePrice.value = (overageInt?.minus(price)).toString()
                }
            } else {
                overagePrice.value = overageInt.toString()
            }
        }
        db.collection("User").document("${UserManager.userToken}").collection("Budget")
            .document("${UserManager.userToken}")
            .update("overage", "${overagePrice.value}")
            .addOnSuccessListener {
                Log.d(
                    "Sophie_budget_edit",
                    "DocumentSnapshot successfully written!"
                )
            }
            .addOnFailureListener { e -> Log.w("Sophie_budget_edit", "Error writing document", e) }

    }

    private fun getOverage() {
        val db = FirebaseFirestore.getInstance()
        db.collection("User").document("${UserManager.userToken}").collection("Budget")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d("Sophie_db", "${document.id} => ${document.data["overage"]}")
                        overagePrice.value = document.data["overage"].toString()
                        circleDay.value = document.data["circleDay"]?.toInt()
                        val c = Calendar.getInstance()
                        val year = c.get(Calendar.YEAR)
                        val monthly = c.get(Calendar.MONTH)
                        val thisMonth = Date(year-1900, monthly, circleDay.value!!.minus(1))
                        val nextMonth = Date(year-1900, monthly+1, circleDay.value!!)
                        val lastMonth = Date(year-1900, monthly-1,circleDay.value!!.minus(1))
                        val futureDay = Date(year-1900, monthly, circleDay.value!!)
                        val timeformat = SimpleDateFormat("yyyyMMdd")
                        startTime = timeformat.format(thisMonth).toInt()
                        endTime = timeformat.format(nextMonth).toInt()
                        lastTime = timeformat.format(lastMonth).toInt()
                        futureTime = timeformat.format(futureDay).toInt()
                        Log.d("Sophie_budget_time",
                            "$startTime + $endTime + $lastTime + $futureTime +${time.value}")
                    }
                }
            }
    }


    fun getTag() {
        _tag.value = mark.value
    }

}