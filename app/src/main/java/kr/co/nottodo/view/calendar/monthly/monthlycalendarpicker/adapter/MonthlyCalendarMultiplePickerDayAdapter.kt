package kr.co.nottodo.view.calendar.monthly.monthlycalendarpicker.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.nottodo.R
import kr.co.nottodo.databinding.ViewMonthlyCalendarPickerDayBinding
import kr.co.nottodo.databinding.ViewMonthlyCalendarPickerEmptyBinding
import kr.co.nottodo.view.calendar.monthly.model.CalendarType
import kr.co.nottodo.view.calendar.monthly.model.MonthlyCalendarDay
import kr.co.nottodo.view.calendar.monthly.monthlycalendarpicker.listener.MonthlyCalendarPickerClickListener
import kr.co.nottodo.view.calendar.monthly.monthlycalendarpicker.viewholder.MonthlyCalendarPickerDayViewHolder
import kr.co.nottodo.view.calendar.monthly.monthlycalendarpicker.viewholder.MonthlyCalendarPickerEmptyViewHolder
import kr.co.nottodo.view.calendar.monthly.util.isTheSameDay
import java.util.Date

// 오늘과 이전은 클릭되면 안된다.
class MonthlyCalendarMultiplePickerDayAdapter(
    private val monthlyCalendarPickerClickListener: MonthlyCalendarPickerClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val calendarPickerItems = mutableListOf<MonthlyCalendarDay>()

    private val selectedDateList = mutableListOf<Date>()

    // 이미 낫투두가 설정되어 있는 날 리스트
    private val notTodoScheduledDateList = mutableListOf<Date>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            CalendarType.DAY.ordinal -> {
                val binding: ViewMonthlyCalendarPickerDayBinding = DataBindingUtil.inflate(
                    layoutInflater, R.layout.view_monthly_calendar_picker_day, parent, false
                )
                MonthlyCalendarPickerDayViewHolder(binding, monthlyCalendarPickerClickListener)
            }

            CalendarType.EMPTY.ordinal -> {
                val binding: ViewMonthlyCalendarPickerEmptyBinding = DataBindingUtil.inflate(
                    layoutInflater, R.layout.view_monthly_calendar_picker_empty, parent, false
                )
                MonthlyCalendarPickerEmptyViewHolder(binding)
            }

            CalendarType.WEEK.ordinal -> {
                val binding: ViewMonthlyCalendarPickerEmptyBinding = DataBindingUtil.inflate(
                    layoutInflater, R.layout.view_monthly_calendar_picker_empty, parent, false
                )
                MonthlyCalendarPickerEmptyViewHolder(binding)
            }

            else -> {
                val binding: ViewMonthlyCalendarPickerEmptyBinding = DataBindingUtil.inflate(
                    layoutInflater, R.layout.view_monthly_calendar_picker_empty, parent, false
                )
                MonthlyCalendarPickerEmptyViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MonthlyCalendarPickerDayViewHolder -> {
                if (calendarPickerItems[position] is MonthlyCalendarDay.DayMonthly) {
                    if (notTodoScheduledDateList.any { it.isTheSameDay((calendarPickerItems[position] as MonthlyCalendarDay.DayMonthly).date) }) {
                        holder.onBindScheduledState(calendarPickerItems[position])
                    } else {
                        if (selectedDateList.any { it.isTheSameDay((calendarPickerItems[position] as MonthlyCalendarDay.DayMonthly).date) }) {
                            holder.onBindSelectedState(calendarPickerItems[position])
                        } else {
                            holder.onBind(calendarPickerItems[position])
                        }
                    }
                } else {
                    holder.onBind(calendarPickerItems[position])
                }
            }

            is MonthlyCalendarPickerEmptyViewHolder -> {
                holder.onBind(calendarPickerItems[position])
            }

            else -> {
                throw IllegalStateException("how dare you....")
            }
        }
    }

    override fun getItemCount(): Int = calendarPickerItems.size

    override fun getItemViewType(position: Int): Int = calendarPickerItems[position].calendarType

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<MonthlyCalendarDay>) {
        calendarPickerItems.clear()
        calendarPickerItems.addAll(list)
        notifyDataSetChanged()
    }

    /**
     * 이미 낫투두 날짜가 정해져 있는 경우의 데이터를 확인하기 위한 함수
     */
    @SuppressLint("NotifyDataSetChanged")
    fun submitScheduledNotTodoList(list: List<Date>) {
        notTodoScheduledDateList.clear()
        notTodoScheduledDateList.addAll(list)
        notifyDataSetChanged()
    }

    /**
     * ssong-develop
     *
     * 1차 QA에서 Fix 되었습니다.
     * Date객체의 시간대가 달라서 동일객체로 판단되지 않습니다. 그로 인해 contains와 remove도 동작하지 않았습니다.
     *
     * Util클래스를 사용요망
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setSelectedDay(date: Date) {
        if (selectedDateList.any { it.isTheSameDay(date) }) {
            if (selectedDateList.size > 1) {
                selectedDateList.remove(selectedDateList.find { it.isTheSameDay(date) })
            }
        } else {
            selectedDateList.add(date)
        }
        notifyDataSetChanged()
    }

    /** 선택한 날 갯수 가져오는 함수 **/
    fun getSelectedDateCount() = selectedDateList.size
}