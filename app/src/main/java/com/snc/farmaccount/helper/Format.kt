package com.snc.farmaccount.helper

import android.annotation.SuppressLint
import android.app.Dialog
import android.util.DisplayMetrics
import com.snc.farmaccount.ApplicationContext
import com.snc.farmaccount.R
import com.snc.farmaccount.databinding.DialogCheckBinding
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


object Format {
    const val DAYCODE_PATTERN = "yyyy.MM.dd (EEEE)"
    const val DATE_PATTERN = "yyyyMMdd"
    const val MONTH_PATTERN = "MM"
    private var weekName: String = ""
    private const val DAY_OF_WEEK_PATTERN = "EEEE"

    private val replace: Regex
        get() = Regex("[^A-Za-z0-9]")

    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val week = calendar.get(Calendar.DAY_OF_WEEK)

        if (week == 1) {
            weekName = ApplicationContext.applicationContext().getString(R.string.sunday)
        }
        if (week == 2) {
            weekName = ApplicationContext.applicationContext().getString(R.string.monday)
        }
        if (week == 3) {
            weekName = ApplicationContext.applicationContext().getString(R.string.tuesday)
        }
        if (week == 4) {
            weekName = ApplicationContext.applicationContext().getString(R.string.wednesday)
        }
        if (week == 5) {
            weekName = ApplicationContext.applicationContext().getString(R.string.thursday)
        }
        if (week == 6) {
            weekName = ApplicationContext.applicationContext().getString(R.string.friday)
        }
        if (week == 7) {
            weekName = ApplicationContext.applicationContext().getString(R.string.saturday)
        }

        return "$year.${month+1}.$day ($weekName)"
    }

    fun removePunctuation(text: String): String = replace.replace(text,"")

    @SuppressLint("SimpleDateFormat")
    fun getSimpleDateFormat(date: Date): String = SimpleDateFormat(DAYCODE_PATTERN).format(date)

    @SuppressLint("SimpleDateFormat")
    fun getDateFormat(date: Date): String = SimpleDateFormat(DATE_PATTERN).format(date)

    @SuppressLint("SimpleDateFormat")
    fun getMonthFormat(date: Date): String = SimpleDateFormat(MONTH_PATTERN).format(date)

    @SuppressLint("SimpleDateFormat")
    fun datePicker(date: Date): String = SimpleDateFormat(DAY_OF_WEEK_PATTERN).format(date)

    fun decimalFormat(decimal: Double?): String = DecimalFormat("#,###").format(decimal)

    fun getDateTimeFromCode(dayCode: String): DateTime = DateTimeFormat.forPattern(DAYCODE_PATTERN).withZone(DateTimeZone.UTC).parseDateTime(dayCode)

    fun getDayCodeFromDateTime(dateTime: DateTime): String = dateTime.toString(DAYCODE_PATTERN)

}