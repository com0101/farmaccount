package com.snc.farmaccount.budget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.snc.farmaccount.`object`.Budget

class BudgetViewModel : ViewModel() {

    private val _budget = MutableLiveData<List<Budget>>()
    val budget: LiveData<List<Budget>>
        get() = _budget

    var budgetType = MutableLiveData<List<Budget>>()

    init {

    }

    fun getBudget() {
        _budget.value = budgetType.value
    }

}