package com.snc.farmaccount.`object`

import java.util.*

data class Event (
    var id: Long ?= null,
    var price: String ?= null,
    var tag: String ?= null,
    var description: String ?= null,
    var date: Date = Calendar.getInstance().time,
    var status: Boolean ?= null
)