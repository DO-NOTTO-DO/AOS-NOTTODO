package kr.co.nottodo.presentation.achieve

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kr.co.nottodo.R
import kr.co.nottodo.databinding.FragmentAchieveBinding
import kr.co.nottodo.listeners.OnFragmentChangedListener
import kr.co.nottodo.util.NotTodoAmplitude
import kr.co.nottodo.view.calendar.monthly.util.convertStringToDate
import kr.co.nottodo.view.snackbar.NotTodoSnackbar
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AchieveFragment : Fragment() {
    private var _binding: FragmentAchieveBinding? = null
    private val binding: FragmentAchieveBinding get() = requireNotNull(_binding)
    private var onFragmentChangedListener: OnFragmentChangedListener? = null
    private val achieveViewModel by viewModels<AchieveFragmentViewModel>()
    private var todayData = LocalDate.now().format(DateTimeFormatter.ofPattern(MONTH_PATTERN))
    val bundle = Bundle()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onFragmentChangedListener = context as? OnFragmentChangedListener
            ?: throw TypeCastException("context can not cast as OnFragmentChangedListener")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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
        networkError()
        clickMonth()
        observeDailyData()
        NotTodoAmplitude.trackEvent(getString(R.string.view_accomplish))
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

    private fun networkError() {
        achieveViewModel.errorMessage.observe(viewLifecycleOwner) {
            NotTodoSnackbar(binding.root, getString(R.string.net_work_error_message)).show()
        }
    }

    private fun initDay() {
        achieveViewModel.getCalenderRate(todayData)
    }

    private fun clickMonth() {
        binding.achieveCalender.setOnMonthlyCalendarNextMonthListener { view, dateString ->
            achieveViewModel.getCalenderRate(dateString)
        }
        binding.achieveCalender.setOnMonthlyCalendarPrevMonthListener { view, dateString ->
            achieveViewModel.getCalenderRate(dateString)
        }
        binding.achieveCalender.setOnMonthlyCalendarDayClickListener { date ->
            val formatDate = achieveViewModel.formatDateToLocal(date)
            bundle.putString(CLICK_DATE, formatDate)
            achieveViewModel.getAchieveDialogDaily(formatDate)
        }
    }

    private fun observeDailyData() {
        achieveViewModel.getAchieveDialog.observe(viewLifecycleOwner) {
            createDialog()
        }
    }

    private fun createDialog() {
        val customDialog = CustomDialogAchieveFragment()
        customDialog.show(childFragmentManager, "CustomDialogFragment")
        customDialog.arguments = bundle
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onDetach() {
        onFragmentChangedListener = null
        super.onDetach()
    }

    companion object {
        const val MONTH_PATTERN = "yyyy-MM"
        const val YEAR_PATTERN = "yyyy-MM-dd"
        const val CLICK_DATE = "CLICK_DATE"
    }
}
