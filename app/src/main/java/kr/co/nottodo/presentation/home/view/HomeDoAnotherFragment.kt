package kr.co.nottodo.presentation.home.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import kr.co.nottodo.R
import kr.co.nottodo.databinding.FragmentHomeDoAnotherBinding
import kr.co.nottodo.presentation.home.view.HomeFragment.Companion.MISSION_ID
import kr.co.nottodo.view.calendar.monthly.util.convertDateToString
import kr.co.nottodo.view.calendar.monthly.util.convertStringToDate
import timber.log.Timber
import java.util.Date

class HomeDoAnotherFragment : DialogFragment() {
    private var _binding: FragmentHomeDoAnotherBinding? = null
    private val binding get() = _binding!!

    private val homeDoAnotherViemodel by viewModels<HomeBottomCalenderViewModel>()
    private val homeViemodel by activityViewModels<HomeViewModel>()
    private lateinit var buttonClickListener: OnButtonClickListener
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeDoAnotherBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // 각 버튼 클릭 시 각각의 함수 호출
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val missionID = requireArguments().getLong(MISSION_ID)
        setNotTodoDateList(missionID.toInt())
        binding.tvHomeCalendarSelectDone.setOnClickListener {
            postSelectedDay(missionID)
        }
        observeDate()
    }

    // 인터페이스
    interface OnButtonClickListener {
        fun onButton1Clicked() {
        }

        fun onPassSelectDay(value: String) {
            Timber.tag("onPassInterface").d("$value")
        }
    }

    // 클릭 이벤트 설정
    fun setButtonClickListener(buttonClickListener: OnButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }

    // 클릭 이벤트 실행

    private fun formatSelectDays(): List<String> {
        val apiDateList = binding.homeDoAnotherCalendar.selectedDays.map {
            it.convertDateToString()!!
        }
        Timber.tag("homeDoAnotherViemodel").d("clickDone: 여기인가?1 $apiDateList")
        return apiDateList
    }

    private fun postSelectedDay(missionID: Long) {
        homeDoAnotherViemodel.postDoAnotherDay(
            missionID,
            formatSelectDays(),
        )
        Timber.tag("homeDoAnotherViemodel").d("postSelectedDay: 아니면 여기?2")
    }

    private fun setNotTodoDateList(missionID: Int) {
        homeDoAnotherViemodel.getDoAnotherDay(missionID)
    }

    private fun deleteFragmentStack() {
        buttonClickListener.onButton1Clicked()
    }

    private fun observeDate() {
        homeDoAnotherViemodel.postDoAnotherDay.observe(viewLifecycleOwner) {
            showResponseToast(it)
        }
        homeDoAnotherViemodel.seletedDays.observe(viewLifecycleOwner) {
            Timber.tag("homeDoAnotherViemodel0").d("$it")
            val selectFirstDay = it[0]
            Timber.d("homeDoAnotherViemodel3", "$selectFirstDay")
            homeViemodel.selectedDay.value = selectFirstDay
            buttonClickListener.onPassSelectDay(selectFirstDay)
        }
        homeDoAnotherViemodel.getDoAnotherDay.observe(viewLifecycleOwner) {
            Timber.tag("homeDoAnotherViemodelca3e").d("$it")
            val dayList = formatDay(it).map {
                it.convertStringToDate()
            }
            binding.homeDoAnotherCalendar.setScheduledNotTodoDateList(dayList as List<Date>)
        }
    }

    private fun formatDay(dayList: List<String>): List<String> {
        val formattedList = dayList.map { day ->
            val parts = day.split(".")
            val year = parts[0]
            val month = parts[1]
            val dayOfMonth = parts[2]
            "$year-$month-$dayOfMonth"
        }
        Timber.tag("homeDoAnotherViemodelca3d").d("$formattedList")
        return formattedList
    }

    private fun showResponseToast(responseResult: String) {
        if (responseResult == "201") {
            Toast.makeText(context, R.string.success_save_not_todo, Toast.LENGTH_SHORT).show()
            deleteFragmentStack()
            dismiss()
            return
        } else {
            Toast.makeText(context, R.string.Duplicate_nottodo, Toast.LENGTH_SHORT).show()
            return
        }
        return
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
