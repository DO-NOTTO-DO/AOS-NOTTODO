package kr.co.nottodo.view.calendar.weekly

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import kr.co.nottodo.R
import kr.co.nottodo.view.calendar.monthly.model.TOTAL_COLUMN_COUNT
import kr.co.nottodo.view.calendar.weekly.adapter.WeeklyAdapter
import kr.co.nottodo.view.calendar.weekly.listener.OnWeeklyCalendarSwipeListener
import kr.co.nottodo.view.calendar.weekly.listener.OnWeeklyCalendarViewChangeYearMonthTextListener
import kr.co.nottodo.view.calendar.weekly.listener.OnWeeklyDayClickListener
import java.time.DayOfWeek
import java.time.LocalDate

class WeeklyCalendar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : RecyclerView(context, attrs, defStyleAttr), OnWeeklyDayClickListener {

    companion object {
        private const val SWIPE_THRESHOLD = 100
        private const val SWIPE_VELOCITY_THRESHOLD = 100
    }

    /** 제스처 디텍터 **/
    private lateinit var gestureDetector: GestureDetector

    /** 주간 캘린더 어댑터 **/
    private val weeklyAdapter = WeeklyAdapter(this)

    /** 오늘 날짜를 알려주는 LocalDate **/
    var currentDate = LocalDate.now()

    /** 선택한 날짜를 알려주는 LocalDate **/
    var selectedDate = LocalDate.now()

    /** 일요일 알려주는 LocalDate **/
    var sundayDate: LocalDate? = null

    /** weekly calendar day item click listener **/
    private var onWeeklyDayClickListener: OnWeeklyDayClickListener? = null

    /** weekly calendar swipe listener **/
    private var onWeeklyCalendarSwipeListener: OnWeeklyCalendarSwipeListener? = null

    private var onWeeklyCalendarViewChangeYearMonthTextListener: OnWeeklyCalendarViewChangeYearMonthTextListener? =
        null

    init {
        initWeeklyCalendar()
    }

    override fun onWeeklyDayClick(view: View, date: LocalDate) {
        selectedDate = date
        weeklyAdapter.setSelectedDay(date)
        // TODO 여기서 뭔가 CalendarView와 인터랙션이 일어날 수 있도록 해야할 것만 같은디....
        onWeeklyCalendarViewChangeYearMonthTextListener?.changeYearMonthText()
        onWeeklyDayClickListener?.onWeeklyDayClick(view, date)
    }

    /** 새로고침 **/
    fun refresh() {
        currentDate = LocalDate.now()
        selectedDate = LocalDate.now()
        weeklyAdapter.setSelectedDay(selectedDate)
        this@WeeklyCalendar.scheduleLayoutAnimation()
        weeklyAdapter.submitList(getDaysInWeek(currentDate))
        onWeeklyCalendarViewChangeYearMonthTextListener?.changeYearMonthText()
        onWeeklyDayClickListener?.onWeeklyDayClick(this, selectedDate)
    }

    /** 가장 최근 낫투두 등록한 날짜로 이동하는 함수 **/
    fun moveToDate(date: LocalDate) {
        selectedDate = date
        weeklyAdapter.setSelectedDay(selectedDate)
        this@WeeklyCalendar.scheduleLayoutAnimation()
        weeklyAdapter.submitList(getDaysInWeek(date))
        onWeeklyCalendarViewChangeYearMonthTextListener?.changeYearMonthText()
        onWeeklyDayClickListener?.onWeeklyDayClick(this, selectedDate)
    }

    /** 서버 통신된 NotToDo 갯수 갱신 **/
    fun setNotToDoCount(list: List<Pair<LocalDate?, Float>>) {
        weeklyAdapter.submitNotTodoCountList(list)
    }

    /** 주간 캘린더 아이템 클릭 리스너 **/
    fun setOnWeeklyDayClickListener(onWeeklyDayClickListener: OnWeeklyDayClickListener) {
        this.onWeeklyDayClickListener = onWeeklyDayClickListener
    }

    /** 주간 캘린더 아이템 클릭 리스너 **/
    fun setOnWeeklyDayClickListener(block: (view: View, date: LocalDate) -> Unit) {
        this.onWeeklyDayClickListener = OnWeeklyDayClickListener(block)
    }

    /** 주간 캘린더 스와이프 리스너 **/
    fun setOnWeeklyCalendarSwipeListener(onWeeklyCalendarSwipeListener: OnWeeklyCalendarSwipeListener) {
        this.onWeeklyCalendarSwipeListener = onWeeklyCalendarSwipeListener
    }

    fun setOnWeeklyCalendarViewChangeYearMonthTextListener(block: () -> Unit) {
        this.onWeeklyCalendarViewChangeYearMonthTextListener =
            OnWeeklyCalendarViewChangeYearMonthTextListener(block)
    }

    fun setOnWeeklyCalendarViewChangeYearMonthTextListener(
        onWeeklyCalendarViewChangeYearMonthTextListener: OnWeeklyCalendarViewChangeYearMonthTextListener,
    ) {
        this.onWeeklyCalendarViewChangeYearMonthTextListener =
            onWeeklyCalendarViewChangeYearMonthTextListener
    }

    private fun initWeeklyCalendar() {
        initBackgroundColor()
        initDefaultItemAnimator()
        initScrollRippleEffect()
        initLayoutManager()
        initGestureDetector()
        initItemTouchListener()
        initLayoutAnimation()
        initRecyclerView()
    }

    private fun initBackgroundColor() {
        setBackgroundColor(ContextCompat.getColor(context, R.color.gray_1_2a2a2e))
    }

    private fun initDefaultItemAnimator() {
        (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    private fun initScrollRippleEffect() {
        overScrollMode = OVER_SCROLL_NEVER
    }

    private fun initLayoutManager() {
        layoutManager = GridLayoutManager(context, TOTAL_COLUMN_COUNT)
    }

    private fun initLayoutAnimation() {
        val scaleUpAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)
        layoutAnimation = scaleUpAnimation
    }

    private fun initRecyclerView() {
        adapter = weeklyAdapter
        weeklyAdapter.submitList(getDaysInWeek(LocalDate.now()))
    }

    private fun initGestureDetector() {
        gestureDetector = GestureDetector(context, object : GestureDetector.OnGestureListener {
            override fun onDown(e: MotionEvent): Boolean = false

            override fun onShowPress(e: MotionEvent) {
                /** no - op **/
            }

            override fun onSingleTapUp(e: MotionEvent): Boolean = false

            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float,
            ): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                /** no - op **/
            }

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float,
            ): Boolean {
                val result = false
                try {
                    if (e1 == null) {
                        return result
                    }
                    val diffY = e2.y - e1.y
                    val diffX = e2.x - e1.x
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                this@WeeklyCalendar.scheduleLayoutAnimation()
                                swipeLeftToRight()
                            } else {
                                this@WeeklyCalendar.scheduleLayoutAnimation()
                                swipeRightToLeft()
                            }
                            onWeeklyCalendarSwipeListener?.onSwipe(sundayDate)
                        }
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
                return result
            }
        })
    }

    private fun initItemTouchListener() {
        addOnItemTouchListener(object : OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                gestureDetector.onTouchEvent(e)
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }

    private fun swipeLeftToRight() {
        weeklyAdapter.submitList(getDaysInWeek(currentDate.minusWeeks(1)))
        currentDate = currentDate.minusWeeks(1)
        sundayDate = getSundayInWeek(currentDate)
    }

    private fun swipeRightToLeft() {
        weeklyAdapter.submitList(getDaysInWeek(currentDate.plusWeeks(1)))
        currentDate = currentDate.plusWeeks(1)
        sundayDate = getSundayInWeek(currentDate)
    }

    /** 한 주에 있는 일 ~ 토 까지의 날짜 정보를 가져오는 함수 **/
    private fun getDaysInWeek(date: LocalDate): List<LocalDate> {
        val days = mutableListOf<LocalDate>()
        var sunDayInWeek = getSundayInWeek(date)
        return if (sunDayInWeek == null) {
            emptyList()
        } else {
            val end = sunDayInWeek.plusWeeks(1)

            while (sunDayInWeek!!.isBefore(end)) {
                days.add(sunDayInWeek)
                sunDayInWeek = sunDayInWeek.plusDays(1)
            }
            days
        }
    }

    /** 한 주에 일요일을 가져오는 함수 **/
    private fun getSundayInWeek(currentDate: LocalDate): LocalDate? {
        var copy = LocalDate.from(currentDate)
        val oneWeekAgo = copy.minusWeeks(1)

        while (copy.isAfter(oneWeekAgo)) {
            if (copy.dayOfWeek == DayOfWeek.SUNDAY) {
                sundayDate = copy
                return copy
            }
            copy = copy.minusDays(1)
        }
        return null
    }
}