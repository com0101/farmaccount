package com.snc.farmaccount.`object`

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SumEvent (
    var tagImage: Int,
    var tagType: String,
    var status: Boolean,
    var totalPrice: String
): Parcelable