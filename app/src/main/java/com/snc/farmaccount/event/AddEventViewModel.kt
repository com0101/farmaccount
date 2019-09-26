package com.snc.farmaccount.event


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

    private var year: Int = 0
    private var month:Int = 0
    private var day:Int = 0
    private var week:Int = 0
    private var weekName = ""
    var mark = MutableLiveData<List<Tag>>()
    var priceInput = MutableLiveData<String>()
    var infoInput = MutableLiveData<String>()
    var chooseTag = MutableLiveData<Tag>()
    var today = MutableLiveData<String>()
    var someDay = MutableLiveData<String>()
    var getPrice = MutableLiveData<String>()
    var idCheck = MutableLiveData<String>()
    var monthFormat = MutableLiveData<String>()
    var getMonth = MutableLiveData<String>()
    var overagePrice = MutableLiveData<String>()
    var time = MutableLiveData<String>()
    var getTime = MutableLiveData<String>()
    var circleDay = MutableLiveData<Int>()
    var thisDate = MutableLiveData<Long>()
    var startTime = 0
    var endTime = 0
    var lastTime = 0
    var futureTime = 0
    var price = 0L

    private val _tag = MutableLiveData<List<Tag>>()
    val tag: LiveData<List<Tag>>
        get() = _tag

    init {
        week()
        getOverage()
        Log.i("Sophie_today","${today.value}")

    }

    fun getTag() {
        _tag.value = mark.value
    }

    fun addFirebase() {
        val db = FirebaseFirestore.getInstance()
        var timeStamp = System.currentTimeMillis()
        // Create a new user with a first and last name
        val event = HashMap<String,Any>()
        event["id"] = timeStamp
        event["price"] = priceInput.value!!
        event["tag"] = chooseTag.value!!.tag_name
        event["description"] = infoInput.value!!
        event["date"] = today.value!!
        event["time"] = time.value!!.toLong()
        event["status"] = chooseTag.value!!.tag_status
        event["month"] = monthFormat.value.toString()
        event["catalog"] = chooseTag.value!!.tag_catalog

        idCheck.value = UserManager.userToken!!.substring(0,20)
        // Add a new document with a generated ID
        db.collection("User").document("${UserManager.userToken}").collection("Event")
            .document("$timeStamp")
            .set(event)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "Sophie_add",
                    "DocumentSnapshot added with ID: $documentReference"
                )

                db.collection("User").document("${UserManager.userToken}").collection("Budget")
                    .document("${UserManager.userToken}")
                    .get()
                    .addOnSuccessListener {document ->
                        if (document != null) {
                            circleDay.value = document.data?.get("circleDay")!!.toInt()
                            val thisMonth = Date(year-1900, month, circleDay.value!!.minus(1))
                            val nextMonth = Date(year-1900, month+1, circleDay.value!!)
                            val lastMonth = Date(year-1900, month-1,circleDay.value!!.minus(1))
                            val futureDay = Date(year-1900, month, circleDay.value!!)
                            val timeformat = SimpleDateFormat("yyyyMMdd")
                            startTime = timeformat.format(thisMonth).toInt()
                            endTime = timeformat.format(nextMonth).toInt()
                            lastTime = timeformat.format(lastMonth).toInt()
                            futureTime = timeformat.format(futureDay).toInt()
                            Log.d("Sophie_budget_time",
                                "$startTime + $endTime + $lastTime + $futureTime +${time.value}")
                        }

                        when {
                            thisDate.value!! in (lastTime + 1) until (futureTime-1) -> {
                                if (time.value!!.toInt() in (lastTime + 1) until (futureTime-1)) {
                                    price = priceInput.value!!.toLong()
                                    updateOverage()
                                    Log.d("Sophie_budget_over",
                                        "in!")
                                }
                            }
                            thisDate.value!! in (startTime + 1) until (endTime-1) -> {
                                if (time.value!!.toInt() in (startTime + 1) until (endTime-1)) {
                                    price = priceInput.value!!.toLong()
                                    updateOverage()
                                    Log.d("Sophie_budget_over",
                                        "inagain!")
                                }
                            }
                            else -> {
                                price = 0
                                updateOverage()
                            }
                        }

                    }
            }
            .addOnFailureListener { e -> Log.w("Sophie_add_fail", "Error adding document", e) }

    }

    private fun updateOverage() {
        val db = FirebaseFirestore.getInstance()
        var overageInt = overagePrice.value?.toInt()
        if (chooseTag.value?.tag_status == true) {
            overagePrice.value = (overageInt?.plus(price)).toString()
        } else {
            overagePrice.value = (overageInt?.minus(price)).toString()
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
        val simpledateformat = SimpleDateFormat("yyyy.MM.dd (EEEE)")
        val monthformat = SimpleDateFormat("MM")
        val dateformat = SimpleDateFormat("yyyyMMdd")
        monthFormat.value = monthformat.format(getDate)
        today.value = simpledateformat.format(getDate)
        time.value = dateformat.format(getDate)
        thisDate.value = dateformat.format(getDate).toLong()
        Log.i("Sophie_date_mode", "${monthFormat.value}")

    }

}