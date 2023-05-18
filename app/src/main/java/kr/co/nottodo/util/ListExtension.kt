package kr.co.nottodo.util

import kr.co.nottodo.view.calendar.monthly.util.isTheSameDay
import java.util.Calendar
import java.util.Date

fun List<Date>.containToday(): Boolean {
    val today = Date()
    this.forEach { dateInList ->
        if (dateInList.isTheSameDay(today)) return true
    }
    return false
}

fun List<Date>.containTomorrow(): Boolean {
    val calendar = Calendar.getInstance().apply {
        time = Date()
        add(Calendar.DAY_OF_YEAR, 1)
    }
    val tomorrow = calendar.time  // 내일의 날짜를 얻습니다.
    this.forEach { dateInList ->
        if (dateInList.isTheSameDay(tomorrow)) return true
    }
    return false
}