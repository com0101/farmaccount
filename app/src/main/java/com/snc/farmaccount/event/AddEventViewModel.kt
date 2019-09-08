package com.snc.farmaccount.event


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.snc.farmaccount.`object`.Event
import com.snc.farmaccount.`object`.Tag
import com.snc.farmaccount.helper.Format
import com.snc.farmaccount.helper.UserManager
import org.joda.time.DateTime
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
    var idCheck = MutableLiveData<String>()
    var monthFormat = MutableLiveData<String>()

    private val _tag = MutableLiveData<List<Tag>>()
    val tag: LiveData<List<Tag>>
        get() = _tag

    init {
        week()
    }

    fun getTag() {
        _tag.value = mark.value
    }

    fun addFirebase() {
        val db = FirebaseFirestore.getInstance()

        // Create a new user with a first and last name
        val event = HashMap<String,Any>()
        event["price"] = priceInput.value!!
        event["tag"] = chooseTag.value!!.tag_name
        event["description"] = infoInput.value!!
        event["date"] = today.value!!
        event["status"] = chooseTag.value!!.tag_status
        event["month"] = monthFormat.value.toString()
        event["catalog"] = chooseTag.value!!.tag_catalog

        idCheck.value = UserManager.userToken!!.substring(0,20)
        // Add a new document with a generated ID
        db.collection("User").document("${UserManager.userToken}").collection("Event")
            .add(event)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "Sophie_add",
                    "DocumentSnapshot added with ID: " + documentReference.id
                )
            }
            .addOnFailureListener { e -> Log.w("Sophie_add_fail", "Error adding document", e) }

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
        monthFormat.value = monthformat.format(getDate)
        today.value = simpledateformat.format(getDate)
        Log.i("Sophie_date_mode", "${monthFormat.value}")

    }

}