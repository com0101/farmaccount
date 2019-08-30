package com.snc.farmaccount.home

import androidx.lifecycle.*
import java.util.*

class DayViewModel: ViewModel() {

    private var year: Int = 0
    private var month:Int = 0
    private var day:Int = 0
    private var week:Int = 0
    private var weekName = ""
    var date = MutableLiveData<String>()
    var pickDate = MutableLiveData<String>()

    var DATE_MODE = ""

    init {
        pickDate
        week()
    }

    private fun week() {
        val c = Calendar.getInstance()
        year = c.get(Calendar.YEAR)
        month = c.get(Calendar.MONTH)
        day = c.get(Calendar.DAY_OF_MONTH)
        week = c.get(Calendar.DAY_OF_WEEK)

        if(week==1) {
            weekName = "星期日"
        }
        if(week==2) {
            weekName = "星期一"
        }
        if(week==3) {
            weekName = "星期二"
        }
        if(week==4) {
            weekName = "星期三"
        }
        if(week==5) {
            weekName = "星期四"
        }
        if(week==6) {
            weekName = "星期五"
        }
        if(week==7) {
            weekName = "星期六"
        }

        DATE_MODE = "$year.${month+1}.$day ($weekName)"
    }

}