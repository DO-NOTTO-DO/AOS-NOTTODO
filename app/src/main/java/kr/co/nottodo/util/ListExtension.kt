package kr.co.nottodo.util

import kr.co.nottodo.view.calendar.monthly.util.isTheSameDay
import java.util.Calendar
import java.util.Date

fun List<Date>.containToday(): Boolean = this.any { dateInList -> dateInList.isTheSameDay(Date()) }

fun List<Date>.containTomorrow(): Boolean {
    val tomorrow = Calendar.getInstance().apply {
        time = Date()
        add(Calendar.DAY_OF_YEAR, 1)
    }.time
    return this.any { dateInList -> dateInList.isTheSameDay(tomorrow) }
}