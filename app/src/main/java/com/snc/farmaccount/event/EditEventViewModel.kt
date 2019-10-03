package com.snc.farmaccount.event

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.simplemobiletools.commons.extensions.toInt
import com.snc.farmaccount.`object`.Event
import com.snc.farmaccount.`object`.Tag
import com.snc.farmaccount.helper.*
import java.text.SimpleDateFormat
import java.util.*

class EditEventViewModel(product: Event, app: Application) : AndroidViewModel(app) {

    var month :String = ""
    var startTime = 0
    var endTime = 0
    var lastTime = 0
    var futureTime = 0
    var priceChange = 0L
    var mark = MutableLiveData<List<Tag>>()
    var chooseTag = MutableLiveData<Tag>()
    var priceInput = MutableLiveData<String>()
    var infoInput = MutableLiveData<String>()
    var category = MutableLiveData<String>()
    var tagName = MutableLiveData<String>()
    var today = MutableLiveData<String>()
    var overagePrice = MutableLiveData<String>()
    var time = MutableLiveData<Long>()
    var thisDate = MutableLiveData<Long>()
    var cycleDay = MutableLiveData<Int>()
    var isRevenue = MutableLiveData<Boolean>()
    private val db = FirebaseFirestore.getInstance()
    private val currentTimestamp = System.currentTimeMillis()
    private val calendar = Calendar.getInstance()
    val event = HashMap<String,Any>()

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
        month = detail.value?.month?:""
        time.value = detail.value?.time
        tagName.value = detail.value?.tag
        getOverage()
    }

    fun editEvent() {
        when {
            chooseTag.value != null -> {
                tagName.value = chooseTag.value?.tag_name
                isRevenue.value = chooseTag.value?.tag_status
                category.value = chooseTag.value?.tag_catalog
            }

            else -> {
                tagName.value = detail.value?.tag
                isRevenue.value = detail.value?.status
                category.value = detail.value?.catalog
            }
        }

        event[ID] = currentTimestamp
        event[PRICE] = priceInput.value.toString()
        event[TAG] = tagName.value.toString()
        event[DESCRIPTION] = infoInput.value.toString()
        event[DATE] = today.value.toString()
        event[TIME] = time.value?:0L
        event[STATUS] = isRevenue.value?:true
        event[MONTH] = month
        event[CATALOG] = category.value.toString()

        db.collection(USER).document("${UserManager.userToken}")
            .collection(EVENT)
            .whereEqualTo(ID, detail.value?.id)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d(SOPHIE, "editEvent:${document.id} => ${document.data}")

                        db.collection(USER).document("${UserManager.userToken}")
                            .collection(EVENT)
                            .document(document.id)
                            .update(event)
                            .addOnSuccessListener { documentReference ->
                                Log.d(
                                    SOPHIE,
                                    "DocumentSnapshot editEvent with ID: $documentReference"
                                )
                                compareWithCycle()
                            }
                            .addOnFailureListener { e ->
                                Log.w(SOPHIE, "Error editEvent document", e)
                            }
                    }
                }
            }
    }

    private fun compareWithCycle() {
        when {
            thisDate.value?:0 in lastTime until futureTime -> {
                if (time.value?:0 in lastTime until futureTime) {
                    updateOverage()
                }
            }

            thisDate.value?:0 in startTime until endTime -> {
                if (time.value?:0 in startTime until endTime) {
                    updateOverage()
                }
            }
        }
    }

    private fun compareWithPrice() {
        val overageInt = overagePrice.value?.toInt()

        if (isRevenue.value != detail.value?.status) {
            when {
                priceInput.value != detail.value?.price -> {
                    priceChange = (detail.value?.price!!.toLong())*2+
                            (priceInput.value!!.toLong()-detail.value?.price!!.toLong())

                    if (chooseTag.value?.tag_status == true) {
                        overagePrice.value = (overageInt?.plus(priceChange)).toString()
                    } else {
                        overagePrice.value = (overageInt?.minus(priceChange)).toString()
                    }

                }

                else -> {
                    priceChange = priceInput.value!!.toLong()

                    if (chooseTag.value?.tag_status == true) {
                        overagePrice.value = (overageInt?.plus(priceChange*2)).toString()
                    } else {
                        overagePrice.value = (overageInt?.minus(priceChange*2)).toString()
                    }

                }
            }

        } else {
            when {
                priceInput.value != detail.value?.price -> {
                    priceChange = priceInput.value!!.toLong()-detail.value?.price!!.toLong()

                    if (isRevenue.value == true) {
                        overagePrice.value = (overageInt?.plus(priceChange)).toString()
                    } else {
                        overagePrice.value = (overageInt?.minus(priceChange)).toString()
                    }

                }

                else -> overagePrice.value = overageInt.toString()
            }
        }
    }

    private fun updateOverage() {
        compareWithPrice()
        db.collection(USER).document("${UserManager.userToken}")
            .collection(BUDGET)
            .document("${UserManager.userToken}")
            .update(OVERAGE, "${overagePrice.value}")
            .addOnSuccessListener {
                Log.d(
                    SOPHIE,
                    "DocumentSnapshot successfully updateOverage!"
                )
            }
            .addOnFailureListener { e ->
                Log.w(SOPHIE, "Error updateOverage document", e)
            }
    }

    private fun getOverage() {
        db.collection(USER).document("${UserManager.userToken}")
            .collection(BUDGET)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d(SOPHIE, "${document.id} => ${document.data[OVERAGE]}")
                        overagePrice.value = document.data[OVERAGE].toString()
                        cycleDay.value = document.data[CYCLE_DAY]?.toInt()
                        timeFormat()
                    }
                }
            }
    }

    @SuppressLint("SimpleDateFormat")
    private fun timeFormat() {
        val year = calendar.get(Calendar.YEAR)
        val monthly = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DATE)
        val thisMonth = Date(year-1900, monthly, cycleDay.value?:0)
        val nextMonth = Date(year-1900, monthly+1, cycleDay.value?:0.minus(1))
        val lastMonth = Date(year-1900, monthly-1,cycleDay.value?:0)
        val futureDay = Date(year-1900, monthly, cycleDay.value?:0.minus(1))
        val today = Date(year-1900, monthly, day)
        val timeFormat = SimpleDateFormat("yyyyMMdd")
        startTime = timeFormat.format(thisMonth).toInt()
        endTime = timeFormat.format(nextMonth).toInt()
        lastTime = timeFormat.format(lastMonth).toInt()
        futureTime = timeFormat.format(futureDay).toInt()
        thisDate.value = timeFormat.format(today).toLong()
    }

    fun setTag() {
        _tag.value = mark.value
    }

}