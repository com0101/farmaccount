package com.snc.farmaccount.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.snc.farmaccount.`object`.Event

class DetailViewModel(product: Event, app: Application) : AndroidViewModel(app) {

    private val _detail = MutableLiveData<Event>()

    val detail: LiveData<Event>
        get() = _detail
    
    init {
        _detail.value = product
    }


}