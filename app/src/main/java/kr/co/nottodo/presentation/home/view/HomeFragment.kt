package kr.co.nottodo.presentation.home.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kr.co.nottodo.MainActivityViewModel
import kr.co.nottodo.R
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.databinding.FragmentHomeBinding
import kr.co.nottodo.listeners.OnFragmentChangedListener
import kr.co.nottodo.presentation.home.viewmodel.HomeViewModel
import kr.co.nottodo.util.NotTodoAmplitude.trackEvent
import kr.co.nottodo.util.NotTodoAmplitude.trackEventWithProperty
import kr.co.nottodo.util.PublicString.DID_USER_WATCHED_NOTIFICATION_PERMISSION_FRAGMENT
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
    private lateinit var homeAdapter: HomeAdapter
    private var onFragmentChangedListener: OnFragmentChangedListener? = null
    private val homeViewModel by activityViewModels<HomeViewModel>()
    private val mainViewModel by activityViewModels<MainActivityViewModel>()
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
        firstDayGet()
        initAdapter()
        setActivityBackgroundColor()
        observeDailyTodo()
        observerData()
        clickFloatingBtn()
        showErrorToast()
        setWeeklyDate()
        weeklyDayClick()
        navigateToNotificationPermissionRequestFragment()
        trackEvent(getString(R.string.view_home))
    }

    private fun firstDayGet() {
        mainViewModel.getFirstDateOnAdd.observe(viewLifecycleOwner) { date ->
            weeklyData = date.replace('.', '-')?.convertToLocalDate().toString()
            Timber.d("calender firstday를 home에서 observe $date")
            binding.weeklyCalendar.moveToDate(
                date.replace('.', '-')?.convertToLocalDate()
                    ?: LocalDate.now(),
            )
        }
        // LiveData 값이 도착한 후에 실행되도록 이 부분을 observe 콜백 내부로 이동
        Timber.d("calender 여기자나 처음 불리는 곳이")
        getHomeDaily(weeklyData)
    }

    private fun getHomeDaily(day: String) {
        homeViewModel.getHomeDaily(day)
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
            getHomeDaily(weeklyData)
        }
        homeViewModel.clickDay.observe(viewLifecycleOwner) { clickDay ->
            bundle.putString(CLICK_DAY, clickDay)
            Timber.d("calender clickday $clickDay")
        }
    }

    private fun showErrorToast() {
        homeViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            NotTodoSnackbar(binding.root, getString(R.string.net_work_error_message)).show()
        }
    }

    private fun observeDailyTodo() {
        homeViewModel.getHomeDaily.observe(viewLifecycleOwner) { homeDaily ->
            Timber.d("calender moveTo가 불렸을 때 여기 값도 다시 불려야 될텐데")
            if (homeDaily.isEmpty()) {
                binding.clHomeMain.visibility = View.VISIBLE
                binding.rvHomeTodoList.visibility = View.INVISIBLE
                Timber.d("calender moveTo가 empty로 들어가나?")
                return@observe
            }
            binding.rvHomeTodoList.visibility = View.VISIBLE
            binding.clHomeMain.visibility = View.INVISIBLE
            homeAdapter.submitList(homeDaily.toList())
            Timber.d("calender 여기가 또 불릴까요?")
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
            Timber.d("calender initMonth: $date")
            weeklyData = date.toString()
            homeViewModel.clickDay.value = weeklyData
            getHomeDaily(weeklyData)
        }
    }

    private fun initAdapter() {
        homeAdapter = HomeAdapter(::menuItemClick, ::todoItemClick)
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
        binding.ftbHomeAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_recommendMissionFragment)
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

    private fun navigateToNotificationPermissionRequestFragment() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS,
            ) != PackageManager.PERMISSION_GRANTED && !SharedPreferences.getBoolean(
                DID_USER_WATCHED_NOTIFICATION_PERMISSION_FRAGMENT,
            )
        ) {
            findNavController().navigate(R.id.action_homeFragment_to_notificationPermissionRequestDialogFragment)
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

    override fun onDismissAndDataPass(selectFirstDay: String?) {
        val formatSelectDay = selectFirstDay?.replace(".", "-")
        if (formatSelectDay.isNullOrEmpty()) {
            Timber.tag("interface1").d("$weeklyData")
            getHomeDaily(weeklyData)
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
        homeViewModel.getHomeWeekly(binding.weeklyCalendar.getCurrentSundayDate().toString())
    }

    companion object {
        const val YEAR_PATTERN = "yyyy-MM-dd"
        const val MISSION_ID = "MISSION_ID"
        const val CLICK_DAY = "CLICK_DAY"
    }
}
