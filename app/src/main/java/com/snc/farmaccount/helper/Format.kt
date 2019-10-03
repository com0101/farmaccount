package com.snc.farmaccount.helper

import android.annotation.SuppressLint
import android.content.Context
import com.snc.farmaccount.ApplicationContext
import com.snc.farmaccount.R
import com.snc.farmaccount.extensions.config
import com.snc.farmaccount.extensions.seconds
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


object Format {
    const val DAYCODE_PATTERN = "yyyy.MM.dd (EEEE)"
    const val DATE_PATTERN = "yyyyMMdd"
    const val YEAR_PATTERN = "YYYY"
    const val TIME_PATTERN = "HHmmss"
    const val MONTH_PATTERN = "MM"
    private var weekName: String = ""
    private const val DAY_PATTERN = "d"
    private const val DAY_OF_WEEK_PATTERN = "EEEE"
    private const val LONGEST_PATTERN = "MMMM d YYYY (EEEE)"
    private const val PATTERN_TIME_12 = "hh:mm a"
    private const val PATTERN_TIME_24 = "HH:mm"

    private const val PATTERN_HOURS_12 = "h a"
    private const val PATTERN_HOURS_24 = "HH"

    fun getDateFromCode(context: Context, dayCode: String, shortMonth: Boolean = false): String {
        val dateTime = getDateTimeFromCode(dayCode)
        val day = dateTime.toString(DAY_PATTERN)
        val year = dateTime.toString(YEAR_PATTERN)
        val monthIndex = Integer.valueOf(dayCode.substring(4, 6))
        var month = getMonthName(context, monthIndex)
        if (shortMonth)
            month = month.substring(0, Math.min(month.length, 3))
        var date = "$month $day"
        if (year != DateTime().toString(YEAR_PATTERN))
            date += " $year"
        return date
    }

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
            weekName = ApplicationContext.applicationContext().getString(R.string.sunday)
        }

        return "$year.${month+1}.$day ($weekName)"
    }

    fun getDayTitle(context: Context, dayCode: String, addDayOfWeek: Boolean = true): String {
        val date = getDateFromCode(context, dayCode)
        val dateTime = getDateTimeFromCode(dayCode)
        val day = dateTime.toString(DAY_OF_WEEK_PATTERN)
        return if (addDayOfWeek)
            "$date ($day)"
        else
            date
    }

    fun getLongestDate(ts: Long) = getDateTimeFromTS(ts).toString(LONGEST_PATTERN)

    fun getDate(context: Context, dateTime: DateTime, addDayOfWeek: Boolean = true) = getDayTitle(context, getDayCodeFromDateTime(dateTime), addDayOfWeek)

    fun getFullDate(context: Context, dateTime: DateTime): String {
        val day = dateTime.toString(DAY_PATTERN)
        val year = dateTime.toString(YEAR_PATTERN)
        val monthIndex = dateTime.monthOfYear
        val month = getMonthName(context, monthIndex)
        return "$month $day $year"
    }

    @SuppressLint("SimpleDateFormat")
    fun getSimpleDateFormat(date: Date) = SimpleDateFormat(DAYCODE_PATTERN).format(date)

    @SuppressLint("SimpleDateFormat")
    fun getDateFormat(date: Date) = SimpleDateFormat(DATE_PATTERN).format(date)

    @SuppressLint("SimpleDateFormat")
    fun getMonthFormat(date: Date) = SimpleDateFormat(MONTH_PATTERN).format(date)

    @SuppressLint("SimpleDateFormat")
    fun datePicker(date: Date) = SimpleDateFormat(DAY_OF_WEEK_PATTERN).format(date)

    fun decimalFormat(decimal: Double?) = DecimalFormat("#,###").format(decimal)

    fun getTodayCode() = getDayCodeFromTS(getNowSeconds())

    fun getHours(context: Context, dateTime: DateTime) = dateTime.toString(getHourPattern(context))

    fun getTime(context: Context, dateTime: DateTime) = dateTime.toString(getTimePattern(context))

    fun getDateTimeFromCode(dayCode: String) = DateTimeFormat.forPattern(DAYCODE_PATTERN).withZone(DateTimeZone.UTC).parseDateTime(dayCode)

    fun getLocalDateTimeFromCode(dayCode: String) = DateTimeFormat.forPattern(DAYCODE_PATTERN).withZone(DateTimeZone.getDefault()).parseLocalDate(dayCode).toDateTimeAtStartOfDay()

    fun getTimeFromTS(context: Context, ts: Long) = getTime(context, getDateTimeFromTS(ts))

    fun getDayStartTS(dayCode: String) = getLocalDateTimeFromCode(dayCode).seconds()

    fun getDayEndTS(dayCode: String) = getLocalDateTimeFromCode(dayCode).plusDays(1).minusMinutes(1).seconds()

    fun getDayCodeFromDateTime(dateTime: DateTime) = dateTime.toString(DAYCODE_PATTERN)

    fun getDateFromTS(ts: Long) = LocalDate(ts * 1000L, DateTimeZone.getDefault())

    fun getDateTimeFromTS(ts: Long) = DateTime(ts * 1000L, DateTimeZone.getDefault())

    fun getUTCDateTimeFromTS(ts: Long) = DateTime(ts * 1000L, DateTimeZone.UTC)

    // use manually translated month names, as DateFormat and Joda have issues with a lot of languages
    fun getMonthName(context: Context, id: Int) = context.resources.getStringArray(R.array.months)[id - 1]

    fun getHourPattern(context: Context) = if (context.config.use24HourFormat) PATTERN_HOURS_24 else PATTERN_HOURS_12

    fun getTimePattern(context: Context) = if (context.config.use24HourFormat) PATTERN_TIME_24 else PATTERN_TIME_12

    fun getExportedTime(ts: Long): String {
        val dateTime = DateTime(ts, DateTimeZone.UTC)
        return "${dateTime.toString(DAYCODE_PATTERN)}T${dateTime.toString(TIME_PATTERN)}Z"
    }

    fun getDayCodeFromTS(ts: Long): String {
        val daycode = getDateTimeFromTS(ts).toString(DAYCODE_PATTERN)
        return if (daycode.isNotEmpty()) {
            daycode
        } else {
            "0"
        }
    }

    fun getShiftedImportTimestamp(ts: Long) = getUTCDateTimeFromTS(ts).withTime(13, 0, 0, 0).withZoneRetainFields(DateTimeZone.getDefault()).seconds()

}