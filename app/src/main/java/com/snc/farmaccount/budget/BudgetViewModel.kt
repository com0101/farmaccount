package com.snc.farmaccount.budget
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.simplemobiletools.commons.extensions.toBoolean
import com.simplemobiletools.commons.extensions.toInt
import com.snc.farmaccount.`object`.Budget
import com.snc.farmaccount.`object`.Event
import com.snc.farmaccount.helper.UserManager
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
    var oldBudget = MutableLiveData<String>()
    var position = MutableLiveData<Int>()
    var farmImage = MutableLiveData<Int>()
    var farmtype = MutableLiveData<Int>()
    var rangeStart= MutableLiveData<String>()
    var rangeEnd= MutableLiveData<String>()
    var overage= MutableLiveData<String>()
    var newBudget = MutableLiveData<Budget>()
    var amountCheck = MutableLiveData<Boolean>()
    var circle = MutableLiveData<Int>()


    fun getBudget() {
        _budget.value = budgetType.value
    }

    fun getBudgetPrice() {
        val db = FirebaseFirestore.getInstance()
        val decimalFormat = DecimalFormat("#,###")
        db.collection("User").document("${UserManager.userToken}")
            .collection("Budget").document("${UserManager.userToken}")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    budgetPrice.value = decimalFormat.format(document.data?.get("budgetPrice").toString().toDouble())
                    oldBudget.value = document.data?.get("budgetPrice").toString()
                    position.value = document.data?.get("position")?.toInt()
                    farmImage.value = document.data?.get("farmImage")?.toInt()
                    farmtype.value = document.data?.get("farmtype")?.toInt()
                    rangeStart.value = document.data?.get("rangeStart").toString()
                    rangeEnd.value = document.data?.get("rangeEnd").toString()
                    overage.value = document.data?.get("overage").toString()
                    circle.value = document.data?.get("circleDay")?.toInt()
                    Log.d("Sophie_db", "${position.value}")
                } else {
                    Log.d("Sophie_db", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Sophie_db", "get failed with ", exception)
            }

    }

    private fun budgetType() {
        postPrice.value = DecimalFormat().parse(budgetPrice.value!!)!!.toDouble().toInt().toString()
        Log.i("Sophie_budgetType_old", "${postPrice.value}")

        if (position.value != selectPosition.value) {
            newBudget.value = getBudgetType.value
            Log.i("Sophie_budgetType", "${getBudgetType.value}")
        } else {
            newBudget.value = Budget(farmImage.value!!,
                farmtype.value!!,
                rangeStart.value!!,
                rangeEnd.value!!,
                postPrice.value!!,
                position.value!!,
                overage.value!!,
                circle.value!!.toLong())
        }

        when {
            postPrice.value?.toInt()!! > getBudgetType.value?.rangeEnd!!.toInt() -> {
                newBudget.value?.budgetPrice = getBudgetType.value?.rangeEnd!!
                amountCheck.value = true
                Log.i("Sophie_amount", "${amountCheck.value}")
            }
            postPrice.value?.toInt()!! < getBudgetType.value?.rangeStart!!.toInt() -> {
                newBudget.value?.budgetPrice = getBudgetType.value?.rangeStart!!
                amountCheck.value = false
                Log.i("Sophie_amount", "${amountCheck.value}")
            }
            else -> {
                newBudget.value?.budgetPrice = postPrice.value!!
                amountCheck.value = null
            }

        }
    }

    fun editBudgetPrice() {
        budgetType()
        Log.i("Sophie_budgetType_edit", "${newBudget.value?.position}")
        val db = FirebaseFirestore.getInstance()
        var newOverage = postPrice.value?.toInt()?.minus((oldBudget.value?.toInt()!!))?.plus(overage.value!!.toInt())

        if (amountCheck.value == null) {
            val budget = HashMap<String,Any>()
            budget["farmImage"] = newBudget.value?.farmImage!!
            budget["farmtype"] = newBudget.value?.farmtype!!
            budget["rangeStart"] = newBudget.value?.rangeStart!!
            budget["rangeEnd"] = newBudget.value?.rangeEnd!!
            budget["budgetPrice"] = postPrice.value!!
            budget["position"] = newBudget.value?.position!!
            budget["overage"] = newOverage.toString()
            budget["circleDay"] = circle.value!!


            // Add a new document with a generated ID

            db.collection("User").document("${UserManager.userToken}").collection("Budget")
                .document("${UserManager.userToken}")
                .update(budget)
                .addOnSuccessListener { Log.d("Sophie_budget_edit", "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w("Sophie_budget_edit", "Error writing document", e) }
        }
        // Create a new user with a first and last name
    }

}