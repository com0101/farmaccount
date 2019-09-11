package com.snc.farmaccount.budget

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.snc.farmaccount.`object`.Budget
import com.snc.farmaccount.dialog.AmountViewModel

//
class AmountViewModelFactory (
    private val budget: Budget,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AmountViewModel::class.java)) {
            return AmountViewModel(budget, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
