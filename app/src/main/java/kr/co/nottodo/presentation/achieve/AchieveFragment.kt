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

            val todoList =
                listOf<Pair<String?, Int>>(
                    Pair("2023-05-22", 1),
                    Pair("2023-05-15", 1),
                    Pair("2023-05-29", 2),
                    Pair("2023-05-08", 3)
                )
            val returnDate = todoList.map {
                it.first?.convertStringToDate() to it.second
            }
            binding.achieveCalender.setNotToDoCountList(returnDate)
        }
    }

    private fun initDay() {
        achieveViewModel.getCalenderRate(todayData)
    }

    private fun createDialog() {
//            binding.achieveCalender.setNotToDoCountList()
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