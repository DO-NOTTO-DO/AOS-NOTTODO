package kr.co.nottodo.view.calendar.monthly.monthlycalendarpicker.viewholder

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kr.co.nottodo.R
import kr.co.nottodo.databinding.ViewMonthlyCalendarPickerDayBinding
import kr.co.nottodo.view.calendar.monthly.model.DateType
import kr.co.nottodo.view.calendar.monthly.model.MonthlyCalendarDay
import kr.co.nottodo.view.calendar.monthly.monthlycalendar.MonthlyCalendar
import kr.co.nottodo.view.calendar.monthly.monthlycalendarpicker.listener.MonthlyCalendarPickerClickListener

class MonthlyCalendarPickerDayViewHolder(
    private val binding: ViewMonthlyCalendarPickerDayBinding,
    private val clickHandler: MonthlyCalendarPickerClickListener
) : ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

    private lateinit var dayData: MonthlyCalendarDay.DayMonthly

    init {
        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)
    }

    @SuppressLint("ResourceType")
    fun onBind(data: MonthlyCalendarDay) {
        if (data is MonthlyCalendarDay.DayMonthly) {
            dayData = data
            with(binding) {
                day = data
                executePendingBindings()

                // ui
                ivMonthlyCalendarPickerDayIndicator.visibility = View.GONE
                tvDay.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        when (data.state) {
                            DateType.WEEKDAY,
                            DateType.WEEKEND -> R.color.white
                            DateType.DISABLED -> R.color.gray_7_8e8e93
                        }
                    )
                )
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    fun onBindSelectedState(data: MonthlyCalendarDay) {
        if (data is MonthlyCalendarDay.DayMonthly) {
            dayData = data
            with(binding) {
                day = data
                executePendingBindings()

                // ui
                with(ivMonthlyCalendarPickerDayIndicator) {
                    visibility = View.VISIBLE
                    setImageDrawable(ContextCompat.getDrawable(context,R.drawable.circle_solid_white_width_38dp))
                }

                tvDay.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        when (data.state) {
                            DateType.WEEKDAY,
                            DateType.WEEKEND -> R.color.black_000000
                            DateType.DISABLED -> {
                                throw IllegalStateException("check day state")
                            }
                        }
                    )
                )
            }
        }
    }

    fun onBindScheduledState(data: MonthlyCalendarDay) {
        // 클릭 안되게 해야함
        if (data is MonthlyCalendarDay.DayMonthly) {
            dayData = data
            with(binding) {
                day = data
                executePendingBindings()

                // ui

                // 클릭 방지 블록
                if (root.hasOnClickListeners()) {
                    root.setOnClickListener(null)
                }

                with(ivMonthlyCalendarPickerDayIndicator) {
                    visibility = View.VISIBLE
                    setImageDrawable(
                        ContextCompat.getDrawable(context,R.drawable.circle_gray_5d5d6b)
                    )
                }

                tvDay.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        when (data.state) {
                            DateType.WEEKDAY,
                            DateType.WEEKEND -> R.color.white
                            DateType.DISABLED -> R.color.gray_7_8e8e93
                        }
                    )
                )
            }
        }
    }

    override fun onClick(view: View) {
        if (dayData.state != DateType.DISABLED) {
            clickHandler.onDayClick(view, dayData.date)
        }
    }

    override fun onLongClick(view: View): Boolean = false
}