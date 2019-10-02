package com.snc.farmaccount.event


import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.simplemobiletools.commons.extensions.toInt
import com.snc.farmaccount.`object`.Tag
import com.snc.farmaccount.helper.UserManager
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
    var time = MutableLiveData<String>()
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
        week()
        getOverage()
    }

    fun setTag() {
        _tag.value = mark.value
    }

    fun addEvent() {
        // Create a new user with a first and last name
        event["id"] = timeStamp
        event["price"] = priceInput.value!!
        event["tag"] = chooseTag.value!!.tag_name
        event["description"] = infoInput.value!!
        event["date"] = today.value!!
        event["time"] = time.value!!.toLong()
        event["status"] = chooseTag.value!!.tag_status
        event["month"] = monthly.value.toString()
        event["catalog"] = chooseTag.value!!.tag_catalog

        db.collection("User").document("${UserManager.userToken}")
            .collection("Event")
            .document("$timeStamp")
            .set(event)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "Sophie_add",
                    "DocumentSnapshot added with ID: $documentReference"
                )
                getBudget()
            }
            .addOnFailureListener { e ->
                Log.w("Sophie_add_fail", "Error adding document", e)
            }
    }

    fun getBudget() {
        db.collection("User").document("${UserManager.userToken}")
            .collection("Budget")
            .document("${UserManager.userToken}")
            .get()
            .addOnSuccessListener {document ->
                if (document != null) {
                    cycleDay.value = document.data?.get("cycleDay")?.toInt()
                    timeFormat()
                }
                compareWithCycle()
            }
    }

    private fun compareWithCycle() {
        when {
            thisDate.value!! in lastTime until futureTime -> {
                if (time.value!!.toInt() in lastTime until futureTime) {
                    price = priceInput.value!!.toLong()
                    updateOverage()
                    Log.d("Sophie_budget_over",
                        "$lastTime+$futureTime")
                }
            }
            thisDate.value!! in startTime until endTime -> {
                if (time.value!!.toInt() in startTime until endTime) {
                    price = priceInput.value!!.toLong()
                    updateOverage()
                    Log.d("Sophie_budget_over", "$startTime+$endTime")
                }
            }
            else -> {
                price = 0
                updateOverage()
            }
        }
    }

    private fun updateOverage() {
        var overageInt = overagePrice.value?.toInt()
        if (chooseTag.value?.tag_status == true) {
            overagePrice.value = (overageInt?.plus(price)).toString()
        } else {
            overagePrice.value = (overageInt?.minus(price)).toString()
        }
        db.collection("User").document("${UserManager.userToken}")
            .collection("Budget")
            .document("${UserManager.userToken}")
            .update("overage", "${overagePrice.value}")
            .addOnSuccessListener {
                Log.d(
                    "Sophie_budget_edit",
                    "DocumentSnapshot successfully written!"
                )
            }
            .addOnFailureListener { e ->
                Log.w("Sophie_budget_edit", "Error writing document", e)
            }

    }

    private fun getOverage() {
        db.collection("User").document("${UserManager.userToken}")
            .collection("Budget")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d("Sophie_db", "${document.id} => ${document.data["overage"]}")
                        overagePrice.value = document.data["overage"].toString()
                    }
                }
            }
    }

    @SuppressLint("SimpleDateFormat")
    private fun week() {
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

        val getDate = Date(year-1900, month, day)
        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd (EEEE)")
        val monthFormat = SimpleDateFormat("MM")
        val dateFormat = SimpleDateFormat("yyyyMMdd")
        monthly.value = monthFormat.format(getDate)
        today.value = simpleDateFormat.format(getDate)
        time.value = dateFormat.format(getDate)
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