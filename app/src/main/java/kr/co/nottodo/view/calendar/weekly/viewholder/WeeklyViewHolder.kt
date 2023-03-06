package kr.co.nottodo.view.calendar.weekly.viewholder

import android.text.format.DateUtils
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kr.co.nottodo.R
import kr.co.nottodo.databinding.ViewWeeklyCalendarDayBinding
import kr.co.nottodo.view.calendar.monthly.util.dayNameParseToKorea
import kr.co.nottodo.view.calendar.weekly.listener.OnWeeklyDayClickListener
import java.sql.Date
import java.time.LocalDate

class WeeklyViewHolder(
    private val binding: ViewWeeklyCalendarDayBinding,
    private val onWeeklyDayClickListener: OnWeeklyDayClickListener
) : ViewHolder(binding.root),
    View.OnClickListener,
    View.OnLongClickListener {

    private lateinit var weeklyDate: LocalDate

    init {
        binding.root.setOnClickListener(this)
        binding.root.setOnLongClickListener(this)
    }

    fun onBind(localDate: LocalDate) {
        val date: Date = Date.valueOf(localDate.toString()) as Date
        this.weeklyDate = localDate
        with(binding) {
            bindDay(localDate)
            bindDayOfWeek(localDate)
            bindDayTextColor(R.color.white)
            bindTodayBullet(date = date)
            bindSelectedDayIndicator(isShow = false)
            bindEmptyNotToDoAchievementBackground()
        }
    }

    fun onSelectBind(localDate: LocalDate) {
        val date: Date = Date.valueOf(localDate.toString()) as Date
        this.weeklyDate = localDate
        with(binding) {
            bindDay(localDate)
            bindDayOfWeek(localDate)
            bindDayTextColor(R.color.white)
            bindTodayBullet(date = date)
            bindSelectedDayIndicator(isShow = true)
            bindEmptyNotToDoAchievementBackground()
        }
    }

    fun onNotToDoBind(localDate: LocalDate, notToDoCount: Double) {
        val date: Date = Date.valueOf(localDate.toString()) as Date
        this.weeklyDate = localDate
        with(binding) {
            bindDay(localDate)
            bindDayOfWeek(localDate)
            bindTodayBullet(date = date)
            bindSelectedDayIndicator(isShow = false)
            bindNotToDoCountIndicator(notToDoCount = notToDoCount)
        }
    }

    fun onSelectedNotToDoBind(localDate: LocalDate, notToDoCount: Double) {
        val date: Date = Date.valueOf(localDate.toString()) as Date
        this.weeklyDate = localDate
        with(binding) {
            bindDay(localDate)
            bindDayOfWeek(localDate)
            bindTodayBullet(date = date)
            bindSelectedDayIndicator(isShow = true)
            bindNotToDoCountIndicator(notToDoCount = notToDoCount)
        }
    }

    override fun onClick(view: View) {
        onWeeklyDayClickListener.onWeeklyDayClick(view, weeklyDate)
    }

    override fun onLongClick(v: View?): Boolean = false

    private fun ViewWeeklyCalendarDayBinding.bindDay(localDate: LocalDate) {
        day = localDate.dayOfMonth.toString()
    }

    private fun ViewWeeklyCalendarDayBinding.bindDayOfWeek(localDate: LocalDate) {
        dayOfWeek = localDate.dayOfWeek.name.dayNameParseToKorea()
    }

    private fun ViewWeeklyCalendarDayBinding.bindTodayBullet(date: Date) {
        ivTodayBullet.visibility = if (DateUtils.isToday(date.time)) View.VISIBLE else View.GONE
    }

    private fun ViewWeeklyCalendarDayBinding.bindSelectedDayIndicator(isShow: Boolean) {
        ivSelectDayBackground.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    private fun ViewWeeklyCalendarDayBinding.bindDayTextColor(@ColorRes resourceId: Int) {
        val textColor = ContextCompat.getColor(this.root.context, resourceId)
        tvWeeklyCalendarDay.setTextColor(textColor)
    }

    private fun ViewWeeklyCalendarDayBinding.bindEmptyNotToDoAchievementBackground() {
        tvWeeklyCalendarDay.background = null
    }

    private fun ViewWeeklyCalendarDayBinding.bindNotToDoLevel1AchievementBackground() {
        tvWeeklyCalendarDay.setBackgroundResource(
            R.drawable.bg_weekly_calendar_achieve_1
        )
    }

    private fun ViewWeeklyCalendarDayBinding.bindNotToDoLevel2AchievementBackground() {
        tvWeeklyCalendarDay.setBackgroundResource(
            R.drawable.bg_weekly_calendar_achieve_2
        )
    }

    private fun ViewWeeklyCalendarDayBinding.bindNotToDoLevel3AchievementBackground() {
        tvWeeklyCalendarDay.setBackgroundResource(
            R.drawable.bg_weekly_calendar_achieve_3
        )
    }

    private fun ViewWeeklyCalendarDayBinding.bindNotToDoCountIndicator(notToDoCount: Double) {
        when (notToDoCount) {
            in 0f..0.15f -> {
                /** no - options **/
                bindEmptyNotToDoAchievementBackground()
            }
            in 0.15f..0.5f -> {
                bindNotToDoLevel1AchievementBackground()
                bindDayTextColor(R.color.white)
            }
            in 0.5f..0.75f -> {
                bindNotToDoLevel2AchievementBackground()
                bindDayTextColor(R.color.black)
            }
            in 0.75f..1f -> {
                bindNotToDoLevel3AchievementBackground()
                bindDayTextColor(R.color.black)
            }
        }
    }


}