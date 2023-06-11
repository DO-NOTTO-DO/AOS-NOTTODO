package kr.co.nottodo.presentation.home.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import kr.co.nottodo.data.model.Home.RequestHomeDoAnotherDay
import kr.co.nottodo.databinding.FragmentHomeDoAnotherBinding
import kr.co.nottodo.presentation.home.view.HomeFragment.Companion.MISSION_ID
import kr.co.nottodo.view.calendar.monthly.util.convertDateToString
import java.util.Date


class HomeDoAnotherFragment : DialogFragment() {

    private var _binding: FragmentHomeDoAnotherBinding? = null
    private val binding get() = _binding!!
    private val calenderViewModel by activityViewModels<HomeBottomCalenderViewModel>()
    private lateinit var dates: MutableList<Date>
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentHomeDoAnotherBinding.inflate(layoutInflater)
        val view = binding.root
        val builder = AlertDialog.Builder(requireActivity())
            .setView(view)
        return builder.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickDone(requireArguments().getLong(MISSION_ID))
        addDate()
    }

    private fun clickDone(missionID: Long) {
        val apiDateList = binding.homeDoAnotherCalendar.selectedDays.map {
            RequestHomeDoAnotherDay(it.convertDateToString()!!)
        }
        binding.tvHomeCalendarSelectDone.setOnClickListener {
            calenderViewModel.postDoAnotherDay(
                missionID,
                apiDateList
            )
        }
    }

    private fun addDate() {

        binding.homeDoAnotherCalendar.setOnMonthlyCalendarPickerClickListener { view, date ->
            val dates = binding.homeDoAnotherCalendar.selectedDays
            Log.d("addDates1", "addDate:$date")
        }
        Log.d("addDates", "addDate: $dates")

//        print(apiDateList)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}