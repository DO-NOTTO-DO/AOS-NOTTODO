package kr.co.nottodo.presentation.achieve

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kr.co.nottodo.databinding.FragmentAchieveBinding
import kr.co.nottodo.listeners.OnFragmentChangedListener
import kr.co.nottodo.view.calendar.monthly.util.convertStringToDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class AchieveFragment : Fragment() {
    private var _binding: FragmentAchieveBinding? = null
    private val binding: FragmentAchieveBinding get() = requireNotNull(_binding)
    private var onFragmentChangedListener: OnFragmentChangedListener? = null
    private val achieveViewModel by viewModels<AchieveFragmentViewModel>()
    private var todayData = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"))

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onFragmentChangedListener = context as? OnFragmentChangedListener
            ?: throw TypeCastException("context can not cast as OnFragmentChangedListener")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        setActivityBackgroundColor()
        _binding = FragmentAchieveBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActivityBackgroundColor()
        initDay()
        observeData()
        createDialog()
    }

    private fun setActivityBackgroundColor() {
        onFragmentChangedListener?.setActivityBackgroundColorBasedOnFragment(this@AchieveFragment)
            ?: throw NullPointerException("onFragmentChangedListener is null")
    }

    private fun observeData() {
        achieveViewModel.calenderCount.observe(viewLifecycleOwner) {
            val notTodoRate = it?.map {
                it.actionDate.convertStringToDate() to it.percentage
            } ?: emptyList()
            binding.achieveCalender.setNotToDoPercentages(notTodoRate)
        }
    }

    private fun initDay() {
        achieveViewModel.getCalenderRate(todayData)
    }

    private fun createDialog() {
        binding.tvAchieveTitle.setOnClickListener {
            val customDialog = CustomDialogAchieveFragment()
            customDialog.show(parentFragmentManager, "CustomDialogFragment")
            achieveViewModel.getAchieveDialogDaily("2023-06-01")
        }

    }

    private fun clickMonth() {
        binding.achieveCalender.setOnMonthlyCalendarNextMonthListener { view, dateString ->
            achieveViewModel.getCalenderRate(dateString)
        }
        binding.achieveCalender.setOnMonthlyCalendarPrevMonthListener { view, dateString ->
            achieveViewModel.getCalenderRate(dateString)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onDetach() {
        onFragmentChangedListener = null
        super.onDetach()
    }
}