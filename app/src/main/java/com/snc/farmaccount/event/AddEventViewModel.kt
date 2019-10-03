package com.snc.farmaccount.event


import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.simplemobiletools.commons.extensions.toInt
import com.snc.farmaccount.ApplicationContext
import com.snc.farmaccount.R
import com.snc.farmaccount.`object`.Tag
import com.snc.farmaccount.helper.*
import java.text.SimpleDateFormat
import java.util.*


class AddEventViewModel : ViewModel() {

    var year = 0
    var month = 0
    var day = 0
    var week = 0
    var weekName = ""
    var startTime = 0
    var endTime = 0
    var lastTime = 0
    var futureTime = 0
    var price = 0L
    var mark = MutableLiveData<List<Tag>>()
    var chooseTag = MutableLiveData<Tag>()
    var priceInput = MutableLiveData<String>()
    var infoInput = MutableLiveData<String>()
    var today = MutableLiveData<String>()
    var someDay = MutableLiveData<String>()
    var getPrice = MutableLiveData<String>()
    var monthly = MutableLiveData<String>()
    var getMonth = MutableLiveData<String>()
    var overagePrice = MutableLiveData<String>()
    var time = MutableLiveData<Long>()
    var getTime = MutableLiveData<String>()
    var cycleDay = MutableLiveData<Int>()
    var thisDate = MutableLiveData<Long>()
    private val db = FirebaseFirestore.getInstance()
    private val timeStamp = System.currentTimeMillis()
    private val calendar = Calendar.getInstance()
    val event = HashMap<String,Any>()

    private val _tag = MutableLiveData<List<Tag>>()
    val tag: LiveData<List<Tag>>
        get() = _tag

    init {
        setCurrentDate()
        getOverage()
    }

    fun setTag() {
        _tag.value = mark.value
    }

    fun addEvent() {
        // Create a new user with a first and last name
        event[ID] = timeStamp
        event[PRICE] = priceInput.value.toString()
        event[TAG] = chooseTag.value?.tag_name.toString()
        event[DESCRIPTION] = infoInput.value.toString()
        event[DATE] = today.value.toString()
        event[TIME] = time.value?:0L
        event[STATUS] = chooseTag.value?.tag_status?:true
        event[MONTH] = monthly.value.toString()
        event[CATALOG] = chooseTag.value?.tag_catalog.toString()

        db.collection(USER).document("${UserManager.userToken}")
            .collection(EVENT)
            .document("$timeStamp")
            .set(event)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    SOPHIE,
                    "DocumentSnapshot addEvent with ID: $documentReference"
                )
                getBudget()
            }
            .addOnFailureListener { e ->
                Log.w(SOPHIE, "Error addEvent document", e)
            }
    }

    private fun getBudget() {
        db.collection(USER).document("${UserManager.userToken}")
            .collection(BUDGET)
            .document("${UserManager.userToken}")
            .get()
            .addOnSuccessListener {document ->
                if (document != null) {
                    cycleDay.value = document.data?.get(CYCLE_DAY)?.toInt()
                    timeFormat()
                }
                compareWithCycle()
            }
    }

    private fun compareWithCycle() {
        when {
            thisDate.value?:0 in lastTime until futureTime -> {
                if (time.value?:0 in lastTime until futureTime) {
                    price = priceInput.value!!.toLong()
                    updateOverage()
                }
            }

            thisDate.value?:0 in startTime until endTime -> {
                if (time.value?:0 in startTime until endTime) {
                    price = priceInput.value!!.toLong()
                    updateOverage()
                }
            }

            else -> {
                price = 0
                updateOverage()
            }
        }
    }

    private fun updateOverage() {
        val overageInt = overagePrice.value?.toInt()

        if (chooseTag.value?.tag_status == true) {
            overagePrice.value = (overageInt?.plus(price)).toString()
        } else {
            overagePrice.value = (overageInt?.minus(price)).toString()
        }

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
                        Log.d(SOPHIE, "getOverage:${document.id} => ${document.data[OVERAGE]}")
                        overagePrice.value = document.data[OVERAGE].toString()
                    }
                }
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

        val getDate = Date(year-1900, month, day)
        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd (EEEE)")
        val monthFormat = SimpleDateFormat("MM")
        val dateFormat = SimpleDateFormat("yyyyMMdd")
        monthly.value = monthFormat.format(getDate)
        today.value = simpleDateFormat.format(getDate)
        time.value = dateFormat.format(getDate).toLong()
        thisDate.value = dateFormat.format(getDate).toLong()

    }

    @SuppressLint("SimpleDateFormat")
    private fun timeFormat() {
        val thisMonth = Date(year-1900, month, cycleDay.value?:0)
        val nextMonth = Date(year-1900, month+1, cycleDay.value?:0.minus(1))
        val lastMonth = Date(year-1900, month-1,cycleDay.value?:0)
        val futureDay = Date(year-1900, month, cycleDay.value?:0.minus(1))
        val timeFormat = SimpleDateFormat("yyyyMMdd")
        startTime = timeFormat.format(thisMonth).toInt()
        endTime = timeFormat.format(nextMonth).toInt()
        lastTime = timeFormat.format(lastMonth).toInt()
        futureTime = timeFormat.format(futureDay).toInt()
    }

}