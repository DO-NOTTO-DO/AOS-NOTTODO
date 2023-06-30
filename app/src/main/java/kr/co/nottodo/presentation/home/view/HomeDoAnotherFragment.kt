package kr.co.nottodo.presentation.home.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import kr.co.nottodo.R
import kr.co.nottodo.databinding.FragmentHomeDoAnotherBinding
import kr.co.nottodo.presentation.home.view.HomeFragment.Companion.MISSION_ID
import kr.co.nottodo.view.calendar.monthly.util.convertDateToString
import timber.log.Timber

class HomeDoAnotherFragment : DialogFragment() {
    private var _binding: FragmentHomeDoAnotherBinding? = null
    private val binding get() = _binding!!

    //    private lateinit var missionID: Long
    private val homeDoAnotherViemodel by viewModels<HomeBottomCalenderViewModel>()
    private lateinit var buttonClickListener: OnButtonClickListener
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
            postSelectedDay(requireArguments().getLong(MISSION_ID))
            observeDate()
        }
    }

    // 인터페이스
    interface OnButtonClickListener {
        fun onButton1Clicked() {
        }
    }

    // 클릭 이벤트 설정
    fun setButtonClickListener(buttonClickListener: OnButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }

    // 클릭 이벤트 실행

    private fun clickDone(): List<String> {
        val apiDateList = binding.homeDoAnotherCalendar.selectedDays.map {
            it.convertDateToString()!!
        }
        Log.d("homeDoAnotherViemodel", "clickDone: 여기인가?1")
        return apiDateList
    }

    private fun postSelectedDay(missionID: Long) {
        homeDoAnotherViemodel.postDoAnotherDay(
            missionID,
            clickDone()
        )
        Log.d("homeDoAnotherViemodel", "postSelectedDay: 아니면 여기?2")
    }

    private fun deleteFragmentStack() {
        buttonClickListener.onButton1Clicked()
    }

    private fun observeDate() {
        homeDoAnotherViemodel.postDoAnotherDay.observe(viewLifecycleOwner) {
            showResponseToast(it)
        }
    }

    private fun showResponseToast(responseResult: String) {
        if (responseResult == "201") {
            Timber.d("homeDoAnotherViemodel1", "$responseResult")
            Toast.makeText(context, R.string.success_save_not_todo, Toast.LENGTH_SHORT).show()
            deleteFragmentStack()
            dismiss()
            return
        } else {
            Timber.d("homeDoAnotherViemodel2", "$responseResult")
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
