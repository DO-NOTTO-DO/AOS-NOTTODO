package kr.co.nottodo.view.calendar.monthly.util

import android.util.TypedValue
import android.view.View
import java.util.Calendar
import java.util.Date

fun Date.isTheSameDay(comparedDate: Date): Boolean {
    val calendar = Calendar.getInstance()
    calendar.withTime(this)
    val comparedCalendarDate = Calendar.getInstance()
    comparedCalendarDate.withTime(comparedDate)
    return calendar.get(Calendar.DAY_OF_YEAR) == comparedCalendarDate.get(Calendar.DAY_OF_YEAR) &&
            calendar.get(Calendar.YEAR) == comparedCalendarDate.get(Calendar.YEAR) &&
            calendar.get(Calendar.MONTH) == comparedCalendarDate.get(Calendar.MONTH) &&
            calendar.get(Calendar.DAY_OF_MONTH) == comparedCalendarDate.get(Calendar.DAY_OF_MONTH)
}

fun Date.isToday(): Boolean {
    val calendar = Calendar.getInstance()
    calendar.withTime(this)
    val todayCalendar = Calendar.getInstance()
    return (todayCalendar.get(Calendar.DAY_OF_YEAR)) == calendar.get(Calendar.DAY_OF_YEAR) &&
            todayCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
}

fun Date.isTomorrow(): Boolean {
    val tomorrow = Calendar.getInstance().apply {
        time = Date()
        add(Calendar.DAY_OF_YEAR, 1)
    }.time
    return this.isTheSameDay(tomorrow)
}

fun Calendar.withTime(date: Date) {
    clear()
    time = date
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}

fun View.addRipple() = with(TypedValue()) {
    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, this, true)
    setBackgroundResource(resourceId)
}

fun View.addCircleRipple() = with(TypedValue()) {
    context.theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, this, true)
    setBackgroundResource(resourceId)
}