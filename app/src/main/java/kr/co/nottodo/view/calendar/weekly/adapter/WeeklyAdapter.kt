package kr.co.nottodo.view.calendar.weekly.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.R
import kr.co.nottodo.databinding.ViewWeeklyCalendarDayBinding
import kr.co.nottodo.view.calendar.weekly.viewholder.WeeklyViewHolder
import kr.co.nottodo.view.calendar.weekly.listener.OnWeeklyDayClickListener
import java.time.LocalDate
import java.util.*

// TODO : 오늘 날짜는 . 으로 표시해줌
// TODO : 선택 날짜는 라운딩 표시
// 일단 ui적으로는 이렇고 더 봐야할 듯?
class WeeklyAdapter(
    private val onWeeklyDayClickListener: OnWeeklyDayClickListener
) : RecyclerView.Adapter<WeeklyViewHolder>() {

    companion object {
        private const val WEEKLY_CALENDAR_START_POSITION = 0
        private const val WEEKLY_CALENDAR_END_POSITION = 7
    }

    private val weeklyDays = mutableListOf<LocalDate>()

    private var selectedDay: LocalDate = LocalDate.now()

    private val notToDoCountList = mutableListOf<Pair<LocalDate?,Float>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewWeeklyCalendarDayBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.view_weekly_calendar_day, parent, false
        )
        return WeeklyViewHolder(binding, onWeeklyDayClickListener)
    }

    override fun onBindViewHolder(holder: WeeklyViewHolder, position: Int) {
        notToDoCountList.indexOfLast {
            it.first?.isEqual(weeklyDays[position]) == true
        }.also { index ->
            val isContainNotToDoCount = index != -1
            if (isContainNotToDoCount) {
                if (selectedDay.isEqual(weeklyDays[position])) {
                    holder.onSelectedNotToDoBind(weeklyDays[position],notToDoCountList[index].second)
                } else {
                    holder.onNotToDoBind(weeklyDays[position],notToDoCountList[index].second)
                }
            } else {
                if (selectedDay.isEqual(weeklyDays[position])) {
                    holder.onSelectBind(weeklyDays[position])
                } else {
                    holder.onBind(weeklyDays[position])
                }
            }
        }
    }

    override fun getItemCount(): Int = weeklyDays.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<LocalDate>) {
        weeklyDays.clear()
        weeklyDays.addAll(list)
        notifyItemRangeChanged(
            WEEKLY_CALENDAR_START_POSITION, WEEKLY_CALENDAR_END_POSITION
        )
    }

    fun setSelectedDay(localDate: LocalDate) {
        val lastPosition = weeklyDays.indexOfLast { it == selectedDay }
        selectedDay = localDate
        if (lastPosition != -1) {
            notifyItemChanged(lastPosition)
        }
        val currentPosition = weeklyDays.indexOfLast { it == selectedDay }
        if (currentPosition != -1) {
            notifyItemChanged(currentPosition)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitNotTodoCountList(list: List<Pair<LocalDate?, Float>>) {
        notToDoCountList.clear()
        notToDoCountList.addAll(list)
        notifyDataSetChanged()
    }
}