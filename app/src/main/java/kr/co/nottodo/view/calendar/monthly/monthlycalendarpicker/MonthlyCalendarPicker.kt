package kr.co.nottodo.view.calendar.monthly.monthlycalendarpicker

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import kr.co.nottodo.R
import kr.co.nottodo.databinding.ViewCalendarWeekDescriptionBinding
import kr.co.nottodo.view.NoRippleRecyclerView
import kr.co.nottodo.view.calendar.monthly.model.DAY_COLUMN_COUNT
import kr.co.nottodo.view.calendar.monthly.model.DateType
import kr.co.nottodo.view.calendar.monthly.model.MonthlyCalendarDay
import kr.co.nottodo.view.calendar.monthly.model.TOTAL_COLUMN_COUNT
import kr.co.nottodo.view.calendar.monthly.monthlycalendarpicker.adapter.MonthlyCalendarPickerDayAdapter
import kr.co.nottodo.view.calendar.monthly.monthlycalendarpicker.listener.MonthlyCalendarPickerClickListener
import kr.co.nottodo.view.calendar.monthly.util.addCircleRipple
import kr.co.nottodo.view.calendar.monthly.util.dpToPx
import kr.co.nottodo.view.calendar.monthly.util.isTheSameDay
import kr.co.nottodo.view.calendar.monthly.util.isWeekend
import kr.co.nottodo.view.calendar.monthly.util.toPrettyDateString
import kr.co.nottodo.view.calendar.monthly.util.toPrettyMonthString
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * created by ssong-develop on 2022.12.31
 *
 * No Swipe Effection MonthlyCalendar
 *
 * 생성 뷰에서 사용합니다.
 *
 * 1번 작업물
 *
 * // TODO 같은 일자 2번 터치시 선택 해제, 날짜 다중 선택가능
 *
 */
class MonthlyCalendarPicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle), MonthlyCalendarPickerClickListener {
    private val timeZone = TimeZone.getDefault()
    private val locale = Locale.KOREA
    var selectedDate: Date = Calendar.getInstance().time
    private var monthlyCalendarPickerClickListener: MonthlyCalendarPickerClickListener? = null
    private val monthlyCalendarPickerDayAdapter = MonthlyCalendarPickerDayAdapter(this)
    private val calendar = Calendar.getInstance(timeZone, locale)
    private var calendarDataList: List<MonthlyCalendarDay> = listOf()
    private var currentDate = calendar.toPrettyMonthString(locale = locale)
        set(value) {
            field = value
            updateCurrentDateTextView()
        }

    private val currentDateTextView = TextView(context, null, R.style.M18).apply {
        id = ViewCompat.generateViewId()
        text = currentDate
        layoutParams =
            LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        typeface = ResourcesCompat.getFont(context, R.font.pretendard)
        setTextColor(ContextCompat.getColor(context, R.color.white))
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
    }

    private val calendarPickerHeaderLinearLayout = LinearLayout(context).apply {
        id = ViewCompat.generateViewId()
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        layoutParams =
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        setBackgroundColor(ContextCompat.getColor(context, R.color.gray_1_2a2a2e))
        setPadding(
            context.dpToPx(20f),
            context.dpToPx(8f),
            context.dpToPx(20f),
            context.dpToPx(8f)
        )

        addView(
            View(this.context).apply {
                layoutParams = LayoutParams(context.dpToPx(80), 0, 0f)
            }
        )

        addView(
            ImageView(this.context).apply {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        this.context,
                        R.drawable.ic_left_arrow_monthly_calendar
                    )
                )
                setOnClickListener {
                    calendar.add(Calendar.MONTH, -1)
                    currentDate = calendar.toPrettyMonthString(locale = locale)
                    initCalendarData()
                }
                setPadding(
                    context.dpToPx(6),
                    context.dpToPx(6),
                    context.dpToPx(6),
                    context.dpToPx(6)
                )
                addCircleRipple()
            }
        )

        addView(
            View(this.context).apply {
                layoutParams = LayoutParams(context.dpToPx(8), 0, 0f)
            }
        )

        addView(currentDateTextView)

        addView(
            View(this.context).apply {
                layoutParams = LayoutParams(context.dpToPx(8), 0, 0f)
            }
        )

        addView(
            ImageView(this.context).apply {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        this.context,
                        R.drawable.ic_right_arrow_monthly_calendar
                    )
                )
                setOnClickListener {
                    calendar.add(Calendar.MONTH, 1)
                    currentDate = calendar.toPrettyMonthString(locale = locale)
                    initCalendarData()
                }
                setPadding(
                    context.dpToPx(6),
                    context.dpToPx(6),
                    context.dpToPx(6),
                    context.dpToPx(6)
                )
                addCircleRipple()
            }
        )

        addView(
            View(this.context).apply {
                layoutParams = LayoutParams(context.dpToPx(80), 0, 0f)
            }
        )
    }

    private val calendarWeekDescriptionView = ViewCalendarWeekDescriptionBinding.inflate(
        LayoutInflater.from(context), this, false
    )

    private val monthRecyclerView = NoRippleRecyclerView(context).apply {
        id = ViewCompat.generateViewId()
        adapter = monthlyCalendarPickerDayAdapter
        layoutManager = GridLayoutManager(context, TOTAL_COLUMN_COUNT).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return DAY_COLUMN_COUNT
                }
            }
        }
        layoutParams =
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        overScrollMode = OVER_SCROLL_NEVER
    }

    init {
        if (attrs != null) {
            getStyleableAttrs(attrs)
        }
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        orientation = LinearLayout.VERTICAL

        addView(calendarPickerHeaderLinearLayout)
        addView(calendarWeekDescriptionView.root)
        addView(monthRecyclerView)
        initBackgroundColor()
        initializeNotToDoMonthCalendar()
    }

    private fun updateCurrentDateTextView() {
        currentDateTextView.text = currentDate
    }

    private fun initializeNotToDoMonthCalendar() {
        initCalendarData()
    }

    private fun initCalendarData() {
        calendarDataList = buildCalendarData()
        monthlyCalendarPickerDayAdapter.submitList(calendarDataList)
    }

    private fun buildCalendarData(): List<MonthlyCalendarDay> {
        // 현재 달력이 보여주고 있는 Calendar instance의 복사본
        val proxyCalendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, calendar.get(Calendar.YEAR))
            set(Calendar.MONTH, calendar.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, 1)
        }
        // 이번 주의 일~토가 담긴 인스턴스
        val currentWeekDays = getCurrentWeekDays()

        val totalDayInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val monthlyCalendarDayList = mutableListOf<MonthlyCalendarDay>()
        (1..totalDayInMonth).forEach { day ->
            proxyCalendar.set(Calendar.DAY_OF_MONTH, day)
            val dayOfWeek = proxyCalendar.get(Calendar.DAY_OF_WEEK)
            val dateType = if (proxyCalendar.isWeekend()) {
                DateType.WEEKEND
            } else {
                // 이번주 한주에 걸리는지 안걸리는 체크하는 조건문
                if (currentWeekDays.any { it.isTheSameDay(proxyCalendar.time) }) {
                    DateType.WEEKDAY
                } else {
                    DateType.DISABLED
                }
            }

            when (day) {
                1 -> {
                    monthlyCalendarDayList.addAll(
                        createStartEmptyView(
                            dayOfWeek,
                            proxyCalendar
                        )
                    )
                    monthlyCalendarDayList.add(
                        MonthlyCalendarDay.DayMonthly(
                            day.toString(),
                            proxyCalendar.toPrettyDateString(),
                            proxyCalendar.time,
                            state = dateType
                        )
                    )
                }

                totalDayInMonth -> {
                    monthlyCalendarDayList.add(
                        MonthlyCalendarDay.DayMonthly(
                            day.toString(),
                            proxyCalendar.toPrettyDateString(),
                            proxyCalendar.time,
                            state = dateType
                        )
                    )
                    monthlyCalendarDayList.addAll(
                        createEndEmptyView(
                            dayOfWeek,
                            proxyCalendar
                        )
                    )
                }

                else -> {
                    monthlyCalendarDayList.add(
                        MonthlyCalendarDay.DayMonthly(
                            day.toString(),
                            proxyCalendar.toPrettyDateString(),
                            proxyCalendar.time,
                            state = dateType
                        )
                    )
                }
            }
        }
        return monthlyCalendarDayList
    }

    private fun createEndEmptyView(
        dayOfWeek: Int,
        proxyCalendar: Calendar
    ): List<MonthlyCalendarDay.Empty> {
        val nextCalendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, proxyCalendar.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, proxyCalendar.get(Calendar.DAY_OF_MONTH))
            set(Calendar.YEAR, proxyCalendar.get(Calendar.YEAR))
        }.also {
            it.add(Calendar.MONTH, 1)
        }

        var totalDayInNextMonth = nextCalendar.getActualMinimum(Calendar.DAY_OF_MONTH)
        val numberOfEmptyView = when (dayOfWeek) {
            Calendar.SUNDAY -> 6
            Calendar.MONDAY -> 5
            Calendar.TUESDAY -> 4
            Calendar.WEDNESDAY -> 3
            Calendar.THURSDAY -> 2
            Calendar.FRIDAY -> 1
            else -> 0
        }

        val listEmpty = mutableListOf<MonthlyCalendarDay.Empty>()
        repeat((0 until numberOfEmptyView).count()) {
            listEmpty.add(
                MonthlyCalendarDay.Empty(
                    totalDayInNextMonth++.toString()
                )
            )
        }
        return listEmpty
    }

    private fun createStartEmptyView(
        dayOfWeek: Int,
        proxyCalendar: Calendar
    ): List<MonthlyCalendarDay.Empty> {
        val previousCalendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, proxyCalendar.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, proxyCalendar.get(Calendar.DAY_OF_MONTH))
            set(Calendar.YEAR, proxyCalendar.get(Calendar.YEAR))
        }.also {
            it.add(Calendar.MONTH, -1)
        }

        val numberOfEmptyView = when (dayOfWeek) {
            Calendar.MONDAY -> 1
            Calendar.TUESDAY -> 2
            Calendar.WEDNESDAY -> 3
            Calendar.THURSDAY -> 4
            Calendar.FRIDAY -> 5
            Calendar.SATURDAY -> 6
            else -> 0
        }

        var startDayInPreviousMonth =
            previousCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) - numberOfEmptyView + 1

        val listEmpty = mutableListOf<MonthlyCalendarDay.Empty>()
        repeat((0 until numberOfEmptyView).count()) {
            listEmpty.add(
                MonthlyCalendarDay.Empty(
                    startDayInPreviousMonth++.toString()
                )
            )
        }
        return listEmpty
    }

    private fun initBackgroundColor() {
        setBackgroundColor(Color.parseColor("#ffffff"))
    }

    private fun getStyleableAttrs(attrs: AttributeSet) {}

    /**
     * 이번주의 시작, 끝을 반환하는 함수
     */
    private fun getCurrentWeekDays(): MutableList<Date> {
        val currentWeekDays = mutableListOf<Date>()
        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_WEEK) // 오늘 요일
        val firstDay = calendar.firstDayOfWeek // 이번주 첫번째 요일
        var offset = firstDay - today
        if (offset > 0) {
            offset -= 7
        }
        calendar.add(Calendar.DAY_OF_MONTH, offset)

        repeat(7) {
            val date = calendar.time
            currentWeekDays.add(date)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return currentWeekDays
    }

    fun setOnMonthlyCalendarPickerClickListener(monthlyCalendarPickerClickListener: MonthlyCalendarPickerClickListener) {
        this.monthlyCalendarPickerClickListener = monthlyCalendarPickerClickListener
    }

    fun setOnMonthlyCalendarPickerClickListener(block: (view: View, date: Date) -> Unit) {
        this.monthlyCalendarPickerClickListener = MonthlyCalendarPickerClickListener(block)
    }

    override fun onDayClick(view: View, date: Date) {
        selectedDate = date
        monthlyCalendarPickerDayAdapter.selectedDate = date
        monthlyCalendarPickerClickListener?.onDayClick(view, date)
    }
}