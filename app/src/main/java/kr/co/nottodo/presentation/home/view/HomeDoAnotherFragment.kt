package kr.co.nottodo.presentation.home.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import kr.co.nottodo.data.model.Home.RequestHomeDoAnotherDay
import kr.co.nottodo.databinding.FragmentHomeDoAnotherBinding
import kr.co.nottodo.presentation.home.view.HomeFragment.Companion.MISSION_ID
import kr.co.nottodo.view.calendar.monthly.util.convertDateToString

class HomeDoAnotherFragment : DialogFragment() {
    private var _binding: FragmentHomeDoAnotherBinding? = null
    private val binding get() = _binding!!
//    private lateinit var missionID: Long
    private val homeDoAnother by viewModels<HomeBottomCalenderViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeDoAnotherBinding.inflate(inflater, container, false)
        val view = binding.root
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // 각 버튼 클릭 시 각각의 함수 호출
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvHomeCalendarSelectDone.setOnClickListener {
            buttonClickListener.onButton1Clicked()
//            val apiDateList = binding.homeDoAnotherCalendar.selectedDays.map {
//                RequestHomeDoAnotherDay(it.convertDateToString()!!)
//            }
//            Log.d("anotherDay", "clickDone: $apiDateList")
//            homeDoAnother.postDoAnotherDay(
//                missionID,
//                apiDateList
//            )
            clickDone(missionID = requireArguments().getLong(MISSION_ID))
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 인터페이스
    interface OnButtonClickListener {
        fun onButton1Clicked()
    }

    // 클릭 이벤트 설정
    fun setButtonClickListener(buttonClickListener: OnButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }

    // 클릭 이벤트 실행
    private lateinit var buttonClickListener: OnButtonClickListener
    private fun clickDone(missionID: Long) {

        binding.tvHomeCalendarSelectDone.setOnClickListener {
            val apiDateList = binding.homeDoAnotherCalendar.selectedDays.map {
                RequestHomeDoAnotherDay(it.convertDateToString()!!)
            }
            Log.d("anotherDay", "clickDone: $apiDateList")
            homeDoAnother.postDoAnotherDay(
                missionID,
                apiDateList
            )
            dismiss()
        }
    }
}
