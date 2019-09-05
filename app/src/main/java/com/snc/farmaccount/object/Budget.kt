package com.snc.farmaccount.`object`

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Budget (
    var farmImage: Int,
    var farmtype: Int,
    var rangeStart: String,
    var rangeEnd: String,
    var budgetPrice: String ?= null
): Parcelable