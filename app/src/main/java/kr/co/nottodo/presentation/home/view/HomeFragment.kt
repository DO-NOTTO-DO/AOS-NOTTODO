package kr.co.nottodo.presentation.home.view

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kr.co.nottodo.databinding.FragmentHomeBinding
import kr.co.nottodo.listeners.OnFragmentChangedListener
import kr.co.nottodo.presentation.recommendation.mission.view.RecommendMissionActivity
import kr.co.nottodo.util.DialogCloseListener
import kr.co.nottodo.view.calendar.monthly.util.convertToLocalDate
import kr.co.nottodo.view.calendar.weekly.listener.OnWeeklyCalendarSwipeListener
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class HomeFragment : Fragment(), DialogCloseListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = requireNotNull(_binding)
    private lateinit var homeAdapter: HomeAdpater
    private var onFragmentChangedListener: OnFragmentChangedListener? = null
    private val homeViewModel by viewModels<HomeViewModel>()
    private var todayData = LocalDate.now().format(DateTimeFormatter.ofPattern(YEAR_PATTERN))
    private var weeklyData = todayData
    val bundle = Bundle()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        onFragmentChangedListener = context as? OnFragmentChangedListener
            ?: throw TypeCastException("context can not cast as OnFragmentChangedListener")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getHomeWeekly(binding.weeklyCalendar.getCurrentSundayDate().toString())
        bundle.putString(CLICK_DAY, todayData)
        initAdapter()
        homeViewModel.getHomeDaily(weeklyData)
        setActivityBackgroundColor()
        observerData()
        clickFloatingBtn()
        setWeeklyDate()
        weeklyDayClick()
    }

    private fun observerData() {
        homeViewModel.getHomeDaily.observe(viewLifecycleOwner) { homeDaily ->
            if (homeDaily.isEmpty()) {
                binding.clHomeMain.visibility = View.VISIBLE
                binding.rvHomeTodoList.visibility = View.INVISIBLE
                return@observe
            }
            binding.rvHomeTodoList.visibility = View.VISIBLE
            binding.clHomeMain.visibility = View.INVISIBLE
            homeAdapter.submitList(homeDaily.toList())
        }

        homeViewModel.getHomeWeeklyResult.observe(viewLifecycleOwner) { weeklyCount ->
            val notToDoCountList = weeklyCount.map {
                it.actionDate.convertToLocalDate() to it.percentage
            }
            binding.weeklyCalendar.setNotToDoCount(notToDoCountList)
        }
        homeViewModel.patchCheckResult.observe(viewLifecycleOwner) {
            homeViewModel.getHomeDaily(weeklyData)
        }
        homeViewModel.clickDay.observe(viewLifecycleOwner) { clickDay ->
            bundle.putString(CLICK_DAY, clickDay)
        }
    }

    private fun initAdapter() {
        homeAdapter = HomeAdpater(::menuItemClick, ::todoItemClick)
        binding.rvHomeTodoList.adapter = homeAdapter
    }

    private fun setActivityBackgroundColor() {
        onFragmentChangedListener?.setActivityBackgroundColorBasedOnFragment(this@HomeFragment)
            ?: throw NullPointerException("onFragmentChangedListener is null")
    }

    private fun menuItemClick(index: Long) {
        bundle.putLong(MISSION_ID, index)
        val bottomSheetFragment = HomeMenuBottomSheetFragment()
        bottomSheetFragment.arguments = bundle
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }

    private fun todoItemClick(id: Long, check: String) {
        homeViewModel.patchTodo(id, check)
    }

    private fun clickFloatingBtn() {
        val intent = Intent(context, RecommendMissionActivity::class.java)
        binding.ftbHomeAdd.setOnClickListener { startActivity(intent) }
    }

    private fun setWeeklyDate() {
        binding.weeklyCalendar.setOnWeeklyCalendarSwipeListener(object :
            OnWeeklyCalendarSwipeListener {
            override fun onSwipe(mondayDate: LocalDate?) {
                if (mondayDate != null) {
                    // Monday 에 따라서 주간 캘린더에 보여줄 낫투두 리스트 값 갱신
                    homeViewModel.getHomeWeekly(mondayDate.toString())
                }
            }
        })
    }

//    private fun setWeeklyDayClick(){
//        binding.weeklyCalendar.setOnWeeklyDayClickListener()
//    }

    private fun weeklyDayClick() {
        binding.weeklyCalendar.setOnWeeklyDayClickListener { view, date ->
            Timber.d("calender", "initMonth: $date")
            weeklyData = date.toString()
            homeViewModel.clickDay.value = weeklyData
            homeViewModel.getHomeDaily(weeklyData)
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

    companion object {
        const val MONTH_PATTERN = "yyyy.MM"
        const val YEAR_PATTERN = "yyyy-MM-dd"
        const val MISSION_ID = "MISSION_ID"
        const val CLICK_DAY = "CLICK_DAY"
    }

    override fun handleDialogClose(dialog: DialogInterface) {
        homeViewModel.getHomeDaily(weeklyData)
    }
}