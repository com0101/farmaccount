package com.snc.farmaccount.dialog

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.snc.farmaccount.`object`.Budget
import com.snc.farmaccount.helper.UserManager
import java.text.DecimalFormat
import java.util.HashMap


class AmountViewModel(budget: Budget, app: Application) : AndroidViewModel(app) {

    private val _detail = MutableLiveData<Budget>()
    val detail: LiveData<Budget>
        get() = _detail

    var rangeStart = ""
    var rangeEnd = ""
    var amount = MutableLiveData<String>()
    var amountCheck = MutableLiveData<Boolean>()
    private val db = FirebaseFirestore.getInstance()
    val budget = HashMap<String,Any>()

    init {
        _detail.value = budget
    }

    fun getInput() {
        when {
            amount.value?.toInt()!! > rangeEnd.toInt() -> {
                detail.value?.budgetPrice = rangeEnd
                amountCheck.value = true
                Log.i("Sophie_amount", "${amountCheck.value}")
            }
            amount.value?.toInt()!! < rangeStart.toInt() -> {
                detail.value?.budgetPrice = rangeStart
                amountCheck.value = false
                Log.i("Sophie_amount", "${amountCheck.value}")
            }
            else -> detail.value?.budgetPrice = amount.value!!
        }
    }

    fun getRange(msg: Budget) {
        rangeStart = msg.rangeStart
        rangeEnd = msg.rangeEnd
    }

    fun addBudget() {
        budget["farmImage"] = detail.value?.farmImage!!
        budget["farmtype"] = detail.value?.farmtype!!
        budget["rangeStart"] = detail.value?.rangeStart!!
        budget["rangeEnd"] = detail.value?.rangeEnd!!
        budget["budgetPrice"] = detail.value?.budgetPrice!!
        budget["position"] = detail.value?.position!!
        budget["overage"] = detail.value?.budgetPrice!!
        budget["cycleDay"] = detail.value?.cycleDay!!

        db.collection("User").document("${UserManager.userToken}")
            .collection("Budget").document("${UserManager.userToken}")
            .set(budget)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "Sophie_add",
                    "DocumentSnapshot added with ID: $documentReference"
                )
            }
            .addOnFailureListener { e ->
                Log.w("Sophie_add_fail", "Error adding document", e)
            }
    }

}
