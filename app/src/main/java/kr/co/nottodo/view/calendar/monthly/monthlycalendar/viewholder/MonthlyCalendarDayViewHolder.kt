package kr.co.nottodo.view.calendar.monthly.monthlycalendar.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kr.co.nottodo.R
import kr.co.nottodo.databinding.ViewMonthlyCalendarDayBinding
import kr.co.nottodo.view.calendar.monthly.model.MonthlyCalendarDay
import java.util.Date

class MonthlyCalendarDayViewHolder(
    private val binding: ViewMonthlyCalendarDayBinding,
    private val onSelectDay: (date: Date) -> Unit
) : ViewHolder(binding.root) {

    private lateinit var dayData: MonthlyCalendarDay.DayMonthly

    init {
        binding.root.setOnClickListener {
            onSelectDay(dayData.date)
        }
    }

    fun onBind(data: MonthlyCalendarDay) {
        if (data is MonthlyCalendarDay.DayMonthly) {
            dayData = data
            binding.apply {
                day = data
                executePendingBindings()
            }
        }
    }

    fun onNotToDoBind(data: MonthlyCalendarDay, notToDoPercentage: Float) {
        if (data is MonthlyCalendarDay.DayMonthly) {
            dayData = data
            binding.apply {
                when (notToDoPercentage) {
                    0f -> {
                        ivNotToDo.visibility = View.VISIBLE
                        tvDay.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
                    }
                    in 0.1f..0.99f -> {
                        ivNotToDo.visibility = View.VISIBLE
                        ivNotToDo.setImageDrawable(
                            ContextCompat.getDrawable(
                                binding.root.context,
                                R.drawable.half_circle_solid_gray
                            )
                        )
                        tvDay.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
                    }
                    1f -> {
                        ivNotToDo.visibility = View.VISIBLE
                        ivNotToDo.setImageDrawable(
                            ContextCompat.getDrawable(
                                binding.root.context,
                                R.drawable.circle_solid_white_width_38dp
                            )
                        )
                        tvDay.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
                    }
                    else -> {
                        ivNotToDo.visibility = View.GONE
                        tvDay.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
                    }
                }
                day = data
            }
        }
    }
}