package com.snc.farmaccount.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.snc.farmaccount.`object`.Tag


class AddEventViewModel: ViewModel() {

    var mark = MutableLiveData<List<Tag>>()

    private val _tag = MutableLiveData<List<Tag>>()

    val tag: LiveData<List<Tag>>
        get() = _tag

    fun getTag() {
        _tag.value = mark.value
    }

}