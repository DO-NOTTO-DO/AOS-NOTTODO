package kr.co.nottodo.view.calendar.weekly

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import kr.co.nottodo.R
import kr.co.nottodo.view.calendar.monthly.util.dpToPx
import kr.co.nottodo.view.calendar.weekly.listener.OnWeeklyCalendarSwipeListener
import kr.co.nottodo.view.calendar.weekly.listener.OnWeeklyDayClickListener
import java.time.LocalDate
import java.time.YearMonth

/**
 * 년 월       오늘
 *      달력뷰
 *
 * 이렇게 표현되는 뷰, 이친구를 써야합니다
 */
class WeeklyCalendarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    /** 주간 캘린더 **/
    private val weeklyCalendar = WeeklyCalendar(context).apply {
        id = ViewCompat.generateViewId()
        layoutParams =
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        setPadding(
            context.dpToPx(8f),
            0,
            context.dpToPx(8f),
            context.dpToPx(4f)
        )
    }

    /** 오늘 날짜의 yearMonth **/
    var yearMonth: YearMonth = YearMonth.from(weeklyCalendar.selectedDate)
        set(value) {
            field = value
            yearMonthText = "${field.year}년 ${field.monthValue}월"
        }

    /** 년 월을 보여주는 Text **/
    var yearMonthText: String = "${yearMonth.year}년 ${yearMonth.monthValue}월"
        set(value) {
            field = value
            currentYearAndMonthTextView.text = field
        }

    /** 년, 월을 보여주는 텍스트 뷰 **/
    private val currentYearAndMonthTextView = TextView(context).apply {
        id = ViewCompat.generateViewId()
        text = yearMonthText
        layoutParams =
            LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        setTextColor(ContextCompat.getColor(context, R.color.white))
        typeface = ResourcesCompat.getFont(context, R.font.pretendard_semibold)
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18f)
    }

    /** 오늘 날짜로 돌아오는 버튼 위젯 **/
    private val returnToTodayButton = ImageView(context).apply {
        id = ViewCompat.generateViewId()
        layoutParams =
            LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        background = ContextCompat.getDrawable(context, R.drawable.ic_weekly_calendar_today_btn)
            ?: throw IllegalStateException("[${TAG}] ic_weekly_calendar_today_btn resource id is null")

        setOnClickListener {
            weeklyCalendar.refresh()
            currentYearAndMonthTextView.text = yearMonthText
        }
    }

    /** 년, 월을 보여주는 텍스트뷰 + 오늘 날짜로 돌아오는 버튼을 담은 HeaderView **/
    private val headerLinearLayout = LinearLayout(context).apply {
        id = ViewCompat.generateViewId()
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        layoutParams =
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        setPadding(
            context.dpToPx(20f),
            context.dpToPx(8f),
            context.dpToPx(20f),
            context.dpToPx(8f)
        )

        addView(currentYearAndMonthTextView)

        addView(
            View(context).apply {
                layoutParams = LayoutParams(0, 0, 1f)
            }
        )

        addView(returnToTodayButton)
    }

    init {
        initWeeklyCalendarView()
    }

    private fun initWeeklyCalendarView() {
        orientation = VERTICAL
        initWidgets()
        initBackgroundColor()
        initChangeYearMonthTextListener()
    }

    private fun initWidgets() {
        addView(headerLinearLayout)
        addView(weeklyCalendar)
    }

    private fun initBackgroundColor() {
        setBackgroundColor(ContextCompat.getColor(context, R.color.gray_1_2a2a2e))
    }

    private fun initChangeYearMonthTextListener() {
        weeklyCalendar.setOnWeeklyCalendarViewChangeYearMonthTextListener {
            yearMonth = YearMonth.from(weeklyCalendar.selectedDate)
        }
    }

    /** 서버 통신된 NotToDo 갯수 갱신 **/
    fun setNotToDoCount(list: List<Pair<LocalDate?, Float>>) {
        weeklyCalendar.setNotToDoCount(
            list = list
        )
    }

    /** 주간 캘린더 아이템 클릭 리스너 **/
    fun setOnWeeklyDayClickListener(onWeeklyDayClickListener: OnWeeklyDayClickListener) {
        weeklyCalendar.setOnWeeklyDayClickListener(
            onWeeklyDayClickListener = onWeeklyDayClickListener
        )
    }

    /** 주간 캘린더 아이템 클릭 리스너 **/
    fun setOnWeeklyDayClickListener(block: (view: View, date: LocalDate) -> Unit) {
        weeklyCalendar.setOnWeeklyDayClickListener(
            block = block
        )
    }

    /** 주간 캘린더 스와이프 리스너 **/
    fun setOnWeeklyCalendarSwipeListener(onWeeklyCalendarSwipeListener: OnWeeklyCalendarSwipeListener) {
        weeklyCalendar.setOnWeeklyCalendarSwipeListener(
            onWeeklyCalendarSwipeListener = onWeeklyCalendarSwipeListener
        )
    }

    companion object {
        private const val TAG = "WeeklyCalendarView"
    }
}