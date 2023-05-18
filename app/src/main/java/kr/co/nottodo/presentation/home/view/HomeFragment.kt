package kr.co.nottodo.presentation.home.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kr.co.nottodo.databinding.FragmentHomeBinding
import kr.co.nottodo.listeners.OnFragmentChangedListener

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = requireNotNull(_binding)
    private lateinit var homeAdapter: HomeAdpater
    private var onFragmentChangedListener: OnFragmentChangedListener? = null
    private val homeViewModel by viewModels<HomeViewModel>()

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
        initAdapter()
        homeViewModel.initHome("2023-05-17")
        setActivityBackgroundColor()
        observerData()
    }

    private fun observerData() {
        homeViewModel.responseHomeDaily.observe(viewLifecycleOwner) { homeDaily ->
            homeAdapter.submitList(homeDaily)
        }
    }

    private fun initAdapter() {
        homeAdapter = HomeAdpater(::menuItemClick, ::todoItemClick)
        binding.rvHomeTodoList.adapter = homeAdapter
//        val todoList = listOf(
//            HomeDailyResponse.HomeDaily(
//                id = 1,
//                title = "어쩌구",
//                completionStatus = "CHECKED",
//                situationName = "잉"
//            ),
//            HomeDailyResponse.HomeDaily(
//                id = 2,
//                title = "어쩌구2",
//                completionStatus = "저쩌구2",
//                situationName = "잉"
//            ),
//            HomeDailyResponse.HomeDaily(
//                id = 3,
//                title = "어쩌구3",
//                completionStatus = "저쩌구3",
//                situationName = "잉"
//            ),
//        )
//        homeAdapter.submitList(todoList)
    }

    private fun setActivityBackgroundColor() {
        onFragmentChangedListener?.setActivityBackgroundColorBasedOnFragment(this@HomeFragment)
            ?: throw NullPointerException("onFragmentChangedListener is null")
    }

    private fun menuItemClick(index: Long) {
        val bottomSheetFragment = HomeMenuBottomSheetFragment()
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }

    private fun todoItemClick(id: Long, check: Boolean) {

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