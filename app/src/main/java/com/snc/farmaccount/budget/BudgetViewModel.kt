package com.snc.farmaccount.budget
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.simplemobiletools.commons.extensions.toInt
import com.snc.farmaccount.`object`.Budget
import com.snc.farmaccount.helper.*
import java.text.DecimalFormat
import java.util.HashMap

class BudgetViewModel: ViewModel() {

    private val _budget = MutableLiveData<List<Budget>>()
    val budget: LiveData<List<Budget>>
        get() = _budget

    var budgetType = MutableLiveData<List<Budget>>()
    var selectPosition =  MutableLiveData<Int>()
    var getBudgetType = MutableLiveData<Budget>()
    var budgetPrice = MutableLiveData<String>()
    var postPrice = MutableLiveData<String>()
    var position = MutableLiveData<Int>()
    var newBudget = MutableLiveData<Budget>()
    var isPriceMoreThan = MutableLiveData<Boolean>()
    var circle = MutableLiveData<Long>()
    private val db = FirebaseFirestore.getInstance()
    lateinit var firebaseBudget : Budget

    fun getBudget() {
        _budget.value = budgetType.value
    }

    fun getBudgetPrice() {
        db.collection(USER).document("${UserManager.userToken}")
            .collection(BUDGET).document("${UserManager.userToken}")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    firebaseBudget = document.toObject(Budget::class.java)!!
                    budgetPrice.value = Format.decimalFormat(firebaseBudget.budgetPrice.toDouble())
                    position.value = firebaseBudget.position
                } else {
                    Log.d(SOPHIE, "No such getBudgetPrice document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(SOPHIE, "get getBudgetPrice failed with ", exception)
            }

    }

    private fun budgetType() {
        postPrice.value = DecimalFormat().parse(budgetPrice.value!!)!!.toDouble().toInt().toString()

        if (position.value != selectPosition.value) {
            newBudget.value = getBudgetType.value
        } else {
            newBudget.value = Budget(
                firebaseBudget.farmImage,
                firebaseBudget.farmtype,
                firebaseBudget.rangeStart,
                firebaseBudget.rangeEnd,
                postPrice.value!!,
                position.value!!,
                firebaseBudget.overage,
                firebaseBudget.cycleDay)
        }

        when {
            postPrice.value?.toInt()!! > getBudgetType.value?.rangeEnd!!.toInt() -> {
                newBudget.value?.budgetPrice = getBudgetType.value?.rangeEnd!!
                isPriceMoreThan.value = true
            }

            postPrice.value?.toInt()!! < getBudgetType.value?.rangeStart!!.toInt() -> {
                newBudget.value?.budgetPrice = getBudgetType.value?.rangeStart!!
                isPriceMoreThan.value = false
            }

            else -> {
                newBudget.value?.budgetPrice = postPrice.value!!
                isPriceMoreThan.value = null
            }

        }
    }

    fun editBudgetPrice() {
        budgetType()
        val newOverage = postPrice.value?.toInt()?.
            minus((firebaseBudget.budgetPrice.toInt()))?.plus(firebaseBudget.overage.toInt())

        if (isPriceMoreThan.value == null) {
            val budget = HashMap<String,Any>()
            budget[FARM_IMAGE] = newBudget.value?.farmImage?:0
            budget[FARM_TYPE] = newBudget.value?.farmtype?:0
            budget[RANGE_START] = newBudget.value?.rangeStart.toString()
            budget[RANGE_END] = newBudget.value?.rangeEnd.toString()
            budget[BUDGET_PRICE] = postPrice.value?:0
            budget[POSITION] = newBudget.value?.position?:0
            budget[OVERAGE] = newOverage.toString()
            budget[CYCLE_DAY] = circle.value?:0

            db.collection(USER).document("${UserManager.userToken}")
                .collection(BUDGET)
                .document("${UserManager.userToken}")
                .update(budget)
                .addOnSuccessListener {
                    Log.d(SOPHIE, "DocumentSnapshot successfully editBudgetPrice!")
                }
                .addOnFailureListener { e ->
                    Log.w(SOPHIE, "Error editBudgetPrice document", e)
                }
        }

    }

}