package com.software3.paws_hub_android.core.ex

import java.util.Calendar
import java.util.Date

fun Date.yearsSince(): Int {
    val calendarNow = Calendar.getInstance()
    val calendarThen = Calendar.getInstance()
    calendarThen.time = this

    val yearNow = calendarNow.get(Calendar.YEAR)
    val yearThen = calendarThen.get(Calendar.YEAR)

    val monthNow = calendarNow.get(Calendar.MONTH)
    val monthThen = calendarThen.get(Calendar.MONTH)

    val dayOfMonthNow = calendarNow.get(Calendar.DAY_OF_MONTH)
    val dayOfMonthThen = calendarThen.get(Calendar.DAY_OF_MONTH)

    var yearsDifference = yearNow - yearThen

    // Ajuste si el día del año de la fecha actual aún no ha pasado
    if (monthNow < monthThen || (monthNow == monthThen && dayOfMonthNow < dayOfMonthThen)) {
        yearsDifference--
    }

    return yearsDifference
}