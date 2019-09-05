package com.snc.farmaccount.budget
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.simplemobiletools.commons.extensions.toBoolean
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
    var oldBudgetType = MutableLiveData<Budget>()
    var budgetPrice = MutableLiveData<String>()
    lateinit var firebaseBudget : Budget

    init {
        getBudgetPrice()
    }

    fun getBudget() {
        _budget.value = budgetType.value
    }

    private fun getBudgetPrice() {
        val db = FirebaseFirestore.getInstance()
        val decimalFormat = DecimalFormat("#,###")
        db.collection("User").document("${UserManager.userToken}").collection("Budget")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d("Sophie_db", "${document.id} => ${document.data["budgetPrice"]}")
                        budgetPrice.value = decimalFormat.format(document.data["budgetPrice"].toString().toDouble())
                        if (document.data != null) {
                            firebaseBudget = document.toObject(Budget::class.java)
                        }
                    }
                }
                oldBudgetType.value = firebaseBudget
            }
    }

    fun budgetType() {
        when( oldBudgetType.value?.farmImage ) {

        }

    }

    fun editBudgetPrice() {
        val db = FirebaseFirestore.getInstance()

        // Create a new user with a first and last name
        val budget = HashMap<String,Any>()
        budget["farmImage"] = detail.value?.farmImage!!
        budget["farmtype"] = detail.value?.farmtype!!
        budget["rangeStart"] = detail.value?.rangeStart!!
        budget["rangeEnd"] = detail.value?.rangeEnd!!
        budget["budgetPrice"] = budgetPrice.value!!

        // Add a new document with a generated ID

        db.collection("User").document("${UserManager.userToken}").collection("Budget")
            .document("${UserManager.userToken}")
            .update(budget)
            .addOnSuccessListener { Log.d("Sophie_budget_edit", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("Sophie_budget_edit", "Error writing document", e) }

    }




}