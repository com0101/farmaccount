package com.snc.farmaccount.event

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.snc.farmaccount.`object`.Event

class EditEventFactory(
    private val product: Event,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditEventViewModel::class.java)) {
            return EditEventViewModel(product, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
