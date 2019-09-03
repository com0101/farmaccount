package com.snc.farmaccount.`object`

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event (
    var price: String ?= null,
    var tag: String ?= null,
    var description: String ?= null,
    var date: String ?= null,
    var status: Boolean ?= null,
    var month: String ?= null,
    var catalog: String ?= null
): Parcelable