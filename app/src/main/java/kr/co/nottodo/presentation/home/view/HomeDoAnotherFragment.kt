package kr.co.nottodo.presentation.home.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kr.co.nottodo.data.model.Home.RequestHomeDoAnotherDay
import kr.co.nottodo.databinding.FragmentHomeDoAnotherBinding
import kr.co.nottodo.presentation.home.view.HomeFragment.Companion.MISSION_ID
import kr.co.nottodo.view.calendar.monthly.util.convertDateToString


class HomeDoAnotherFragment : DialogFragment() {
    private var _binding: FragmentHomeDoAnotherBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeDoAnotherBinding.inflate(inflater, container, false)
        val view = binding.root
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        clickDone(requireArguments().getLong(MISSION_ID))
        // 각 버튼 클릭 시 각각의 함수 호출
        binding.tvHomeCalendarSelectDone.setOnClickListener {
            buttonClickListener.onButton1Clicked()
            dismiss()
        }
        return view
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
//            calenderViewModel.postDoAnotherDay(
//                missionID,
//                apiDateList
//            )
            dismiss()
        }
    }
}
