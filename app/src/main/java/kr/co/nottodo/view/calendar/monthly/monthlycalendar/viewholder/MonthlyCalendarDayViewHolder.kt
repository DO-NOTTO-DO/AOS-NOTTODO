package kr.co.nottodo.view.calendar.monthly.monthlycalendar.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kr.co.nottodo.R
import kr.co.nottodo.databinding.ViewMonthlyCalendarDayBinding
import kr.co.nottodo.view.calendar.monthly.model.MonthlyCalendarDay

class MonthlyCalendarDayViewHolder(
    private val binding: ViewMonthlyCalendarDayBinding
) : ViewHolder(binding.root) {

    private lateinit var dayData: MonthlyCalendarDay.DayMonthly

    fun onBind(data: MonthlyCalendarDay) {
        if (data is MonthlyCalendarDay.DayMonthly) {
            dayData = data
            binding.apply {
                day = data
                executePendingBindings()
            }
        }
    }

    fun onNotToDoBind(data: MonthlyCalendarDay, notToDoCount: Int) {
        if (data is MonthlyCalendarDay.DayMonthly) {
            dayData = data
            binding.apply {
                when (notToDoCount) {
                    1 -> {
                        ivNotToDo.visibility = View.VISIBLE
                    }
                    2 -> {
                        ivNotToDo.visibility = View.VISIBLE
                        ivNotToDo.setImageDrawable(
                            ContextCompat.getDrawable(
                                binding.root.context,
                                R.drawable.half_circle_solid_gray
                            )
                        )
                    }
                    3 -> {
                        ivNotToDo.visibility = View.VISIBLE
                        ivNotToDo.setImageDrawable(
                            ContextCompat.getDrawable(
                                binding.root.context,
                                R.drawable.circle_solid_white_width_38dp
                            )
                        )
                    }
                    else -> {
                        ivNotToDo.visibility = View.GONE
                    }
                }
                day = data
            }
        }
    }
}