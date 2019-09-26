package com.snc.farmaccount.`object`

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event (
    var id:Long ?= null,
    var price: String ?= null,
    var tag: String ?= null,
    var description: String ?= null,
    var date: String ?= null,
    var time: Long ?= null,
    var status: Boolean ?= null,
    var month: String ?= null,
    var catalog: String ?= null
): Parcelable