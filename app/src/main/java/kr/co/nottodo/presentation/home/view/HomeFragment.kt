package kr.co.nottodo.presentation.home.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kr.co.nottodo.R
import kr.co.nottodo.databinding.FragmentHomeBinding
import kr.co.nottodo.listeners.OnFragmentChangedListener
import kr.co.nottodo.presentation.addition.view.AdditionActivity.Companion.FIRST_DATE
import kr.co.nottodo.presentation.recommend.mission.view.RecommendMissionActivity
import kr.co.nottodo.util.NotTodoAmplitude.trackEvent
import kr.co.nottodo.util.NotTodoAmplitude.trackEventWithProperty
import kr.co.nottodo.view.calendar.monthly.util.convertToLocalDate
import kr.co.nottodo.view.calendar.weekly.listener.OnWeeklyCalendarSwipeListener
import kr.co.nottodo.view.snackbar.NotTodoSnackbar
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment(), DialogCloseListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = requireNotNull(_binding)
    private lateinit var homeAdapter: HomeAdpater
    private var onFragmentChangedListener: OnFragmentChangedListener? = null
    private val homeViewModel by activityViewModels<HomeViewModel>()
    private var todayData = LocalDate.now().format(DateTimeFormatter.ofPattern(YEAR_PATTERN))
    private var weeklyData = todayData
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
        ObserverDailyTodo()
        observerData()
        clickFloatingBtn()
        showErrorToast()
        setWeeklyDate()
        weeklyDayClick()
        firsetDayGet()
        trackEvent(getString(R.string.view_home))
    }

    private fun observerData() {
        homeViewModel.getHomeWeeklyResult.observe(viewLifecycleOwner) { weeklyCount ->
            val notToDoCountList = weeklyCount.map {
                it.actionDate.convertToLocalDate() to it.percentage
            }
            binding.weeklyCalendar.setNotToDoCount(notToDoCountList)
        }
        homeViewModel.patchCheckResult.observe(viewLifecycleOwner) {
            homeViewModel.getHomeWeekly(binding.weeklyCalendar.getCurrentSundayDate().toString())
            homeViewModel.getHomeDaily(weeklyData)
        }
        homeViewModel.clickDay.observe(viewLifecycleOwner) { clickDay ->
            bundle.putString(CLICK_DAY, clickDay)
        }
    }

    private fun showErrorToast() {
        homeViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            NotTodoSnackbar(binding.root, getString(R.string.net_work_error_message)).show()
        }
    }

    private fun ObserverDailyTodo() {
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
        bottomSheetFragment.setDialogDismissListener(this)
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }

    private fun todoItemClick(id: Long, check: String) {
        homeViewModel.patchTodo(id, check)
    }

    private fun clickFloatingBtn() {
        val intent = Intent(context, RecommendMissionActivity::class.java)
        binding.ftbHomeAdd.setOnClickListener {
            startActivity(intent)
        }
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

    private fun weeklyDayClick() {
        binding.weeklyCalendar.setOnWeeklyDayClickListener { view, date ->
            trackClickDay(date.toString())
            Timber.d("calender", "initMonth: $date")
            weeklyData = date.toString()
            homeViewModel.clickDay.value = weeklyData
            homeViewModel.getHomeDaily(weeklyData)
        }
    }

    private fun trackClickDay(weeklyList: String) {
        val formatDay = weeklyList.filter { char -> char != '-' }.toInt()
        trackEventWithProperty(
            getString(R.string.click_weekly_date),
            getString(R.string.date),
            formatDay,
        )
    }

    private fun firsetDayGet() {
        binding.weeklyCalendar.moveToDate(
            arguments?.getString(FIRST_DATE)?.replace('.', '-')?.convertToLocalDate()
                ?: LocalDate.now(),
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onDetach() {
        onFragmentChangedListener = null
        super.onDetach()
    }

    override fun onDismissAndDataPass(selectFirstDay: String?) {
        val formatSelectDay = selectFirstDay?.replace(".", "-")
        if (formatSelectDay.isNullOrEmpty()) {
            Timber.tag("interface1").d("$weeklyData")
            homeViewModel.getHomeDaily(weeklyData)
        } else {
            Timber.tag("interface").d("$formatSelectDay")
            weeklyData = formatSelectDay
            val weeklyFormatDay = formatSelectDay.convertToLocalDate()
            Timber.tag("interface6").d("$weeklyFormatDay")
            binding.weeklyCalendar.moveToDate(weeklyFormatDay!!)
        }
    }

    override fun onDeleteButtonClicked() {
        Timber.tag("interface3").d("$weeklyData")
//        homeViewModel.getHomeDaily(weeklyData)
        homeViewModel.getHomeWeekly(binding.weeklyCalendar.getCurrentSundayDate().toString())
    }

    companion object {
        const val YEAR_PATTERN = "yyyy-MM-dd"
        const val MISSION_ID = "MISSION_ID"
        const val CLICK_DAY = "CLICK_DAY"
    }
}
