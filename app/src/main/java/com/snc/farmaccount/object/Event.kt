package com.snc.farmaccount.`object`

import java.util.*

data class Event (
    var id: Long,
    var price: String,
    var tag: String,
    var description: String,
    var date: Date = Calendar.getInstance().time,
    var status: Boolean
)