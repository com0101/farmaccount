package com.snc.farmaccount

fun getPriceSumTest(priceList: ArrayList<Long>): Long { // Unit Test for summing all price
    return when {
        priceList.isEmpty() -> 0
        else -> priceList.sum()
    }
}