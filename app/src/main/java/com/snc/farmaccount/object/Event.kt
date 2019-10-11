package com.snc.farmaccount.`object`

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event (
    var id: Long = 0L,
    var price: String = "",
    var tag: String = "",
    var description: String = "",
    var date: String = "",
    var time: Long = 0,
    var status: Boolean = false,
    var month: String = "",
    var catalog: String = ""
): Parcelable