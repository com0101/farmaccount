package com.snc.farmaccount.dialog

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.snc.farmaccount.`object`.Budget
import com.snc.farmaccount.helper.*
import java.util.HashMap


class AmountViewModel(budget: Budget, app: Application) : AndroidViewModel(app) {

    private val _detail = MutableLiveData<Budget>()
    val detail: LiveData<Budget>
        get() = _detail

    var rangeStart = ""
    var rangeEnd = ""
    var amount = MutableLiveData<String>()
    var isPriceMoreThan = MutableLiveData<Boolean>()
    private val db = FirebaseFirestore.getInstance()
    val budget = HashMap<String,Any>()

    init {
        _detail.value = budget
    }

    fun getInput() {
        when {
            amount.value?.toInt()!! > rangeEnd.toInt() -> {
                detail.value?.budgetPrice = rangeEnd
                isPriceMoreThan.value = true
                Log.i(SOPHIE, "getInput:${isPriceMoreThan.value}")
            }

            amount.value?.toInt()!! < rangeStart.toInt() -> {
                detail.value?.budgetPrice = rangeStart
                isPriceMoreThan.value = false
                Log.i(SOPHIE, "getInput:${isPriceMoreThan.value}")
            }

            else -> detail.value?.budgetPrice = amount.value!!
        }
    }

    fun getRange(msg: Budget) {
        rangeStart = msg.rangeStart
        rangeEnd = msg.rangeEnd
    }

    fun addBudget() {
        budget[FARM_IMAGE] = detail.value?.farmImage?:0
        budget[FARM_TYPE] = detail.value?.farmtype?:0
        budget[RANGE_START] = detail.value?.rangeStart?:0
        budget[RANGE_END] = detail.value?.rangeEnd?:0
        budget[BUDGET_PRICE] = detail.value?.budgetPrice?:0
        budget[POSITION] = detail.value?.position?:0
        budget[OVERAGE] = detail.value?.budgetPrice?:0
        budget[CYCLE_DAY] = detail.value?.cycleDay?:0

        db.collection(USER).document("${UserManager.userToken}")
            .collection(BUDGET).document("${UserManager.userToken}")
            .set(budget)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    SOPHIE,
                    "DocumentSnapshot addBudget with ID: $documentReference"
                )
            }
            .addOnFailureListener { e ->
                Log.w(SOPHIE, "Error addBudget document", e)
            }
    }

}
