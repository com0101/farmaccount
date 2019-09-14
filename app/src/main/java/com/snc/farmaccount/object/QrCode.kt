package com.snc.farmaccount.`object`

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QrCode (
    var totalPrice: String,
    var date: String
): Parcelable