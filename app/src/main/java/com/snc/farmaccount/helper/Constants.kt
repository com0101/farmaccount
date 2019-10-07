package com.snc.farmaccount.helper

import android.util.DisplayMetrics
import com.snc.farmaccount.ApplicationContext

const val DAY_CODE = "day_code"

// Shared Preferences
const val TOKEN = "Token"
const val NAME = "Name"
const val EMAIL = "email"

// firebase key value
const val ID = "id"
const val PRICE = "price"
const val TAG = "tag"
const val DESCRIPTION = "description"
const val DATE = "date"
const val TIME = "time"
const val STATUS = "status"
const val MONTH = "month"
const val CATALOG = "catalog"
const val FARM_IMAGE = "farmImage"
const val FARM_TYPE = "farmtype"
const val RANGE_START = "rangeStart"
const val RANGE_END = "rangeEnd"
const val BUDGET_PRICE = "budgetPrice"
const val POSITION = "position"
const val OVERAGE = "overage"
const val CYCLE_DAY = "cycleDay"
const val TAG_IMAGE = "tagImage"
const val TAG_TYPE = "tagType"
const val TOTAL_PRICE = "totalPrice"
const val USER = "User"
const val EVENT = "Event"
const val BUDGET = "Budget"
const val SOPHIE = "Sophie"
const val INTERNET = "android.net.conn.CONNECTIVITY_CHANGE"

fun Number.dpToPx(): Int {
    return (this.toFloat() * (ApplicationContext.applicationContext()
        .resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
}