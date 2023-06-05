package kr.co.nottodo.view.calendar.monthly.monthlycalendar.listener

import java.util.Date

fun interface MonthlyCalendarDayClickListener {
    fun onSelectDay(date: Date)
}