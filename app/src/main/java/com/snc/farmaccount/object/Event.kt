package com.snc.farmaccount.`object`


data class Event (
    var price: String ?= null,
    var tag: String ?= null,
    var description: String ?= null,
    var date: String ?= null,
    var status: Boolean ?= null
)