package com.snc.farmaccount.`object`

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Budget (
    var farmImage: Int = 0,
    var farmtype: Int = -1,
    var rangeStart: String = "",
    var rangeEnd: String = "",
    var budgetPrice: String = "",
    var position: Int = -1,
    var overage: String = "",
    var cycleDay: Long = 0L
): Parcelable