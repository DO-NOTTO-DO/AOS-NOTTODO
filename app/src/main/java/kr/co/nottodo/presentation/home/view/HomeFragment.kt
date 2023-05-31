package kr.co.nottodo.presentation.home.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kr.co.nottodo.databinding.FragmentHomeBinding
import kr.co.nottodo.listeners.OnFragmentChangedListener
import kr.co.nottodo.presentation.addition.view.AdditionActivity
import kr.co.nottodo.view.calendar.monthly.util.convertToLocalDate
import kr.co.nottodo.view.calendar.weekly.listener.OnWeeklyCalendarSwipeListener
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = requireNotNull(_binding)
    private lateinit var homeAdapter: HomeAdpater
    private var onFragmentChangedListener: OnFragmentChangedListener? = null
    private val homeViewModel by viewModels<HomeViewModel>()
    private var todayData = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    private var weeklyData = todayData

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
        homeViewModel.getHomeWeekly(todayData)
        initAdapter()
        //todo 더미 바꿔야 됨, weeklyTodo로..?
        homeViewModel.getHomeDaily(weeklyData)
        setActivityBackgroundColor()
        observerData()
        clickFloatingBtn()
        setWeeklyDate()
        initMonth()
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
        homeViewModel.deleteTodo.observe(viewLifecycleOwner) {
            //todo 왜 observer안돼 끄흡이다
            Timber.tag("deleteTodo").e("$it")
            homeViewModel.getHomeDaily(weeklyData)
            homeAdapter.submitList(homeViewModel.getHomeDaily.value)
        }
        homeViewModel.patchCheckResult.observe(viewLifecycleOwner) {
            Timber.d("homefragment todo성공", "observerData: ")
            homeViewModel.getHomeDaily(weeklyData)
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
        val bundle = Bundle()
        bundle.putLong(MISSION_ID, index)
        val bottomSheetFragment = HomeMenuBottomSheetFragment()
        bottomSheetFragment.arguments = bundle
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }

    private fun todoItemClick(id: Long, check: String) {
        homeViewModel.patchTodo(id, check)
    }

    private fun clickFloatingBtn() {
        val intent = Intent(context, AdditionActivity::class.java)
        binding.ftbHomeAdd.setOnClickListener { startActivity(intent) }
    }

    private fun setWeeklyDate() {
        binding.weeklyCalendar.setOnWeeklyCalendarSwipeListener(object :
            OnWeeklyCalendarSwipeListener {
            override fun onSwipe(mondayDate: LocalDate?) {
                if (mondayDate != null) {
                    // Monday 에 따라서 주간 캘린더에 보여줄 낫투두 리스트 값 갱신
                    weeklyData = mondayDate.toString()
                    homeViewModel.getHomeWeekly(weeklyData)
                }
            }
        })
    }

    private fun initMonth() {
        binding.weeklyCalendar.setOnWeeklyDayClickListener { view, date ->
            Timber.d("calender", "initMonth: $date")
            weeklyData = date.toString()
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
    }
}