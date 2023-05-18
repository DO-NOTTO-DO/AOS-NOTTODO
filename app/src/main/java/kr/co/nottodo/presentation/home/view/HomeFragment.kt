package kr.co.nottodo.presentation.home.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kr.co.nottodo.databinding.FragmentHomeBinding
import kr.co.nottodo.listeners.OnFragmentChangedListener
import kr.co.nottodo.presentation.addition.view.AdditionActivity

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
        //todo 더미 바꿔야 됨
        homeViewModel.getHomeDaily("2023-05-17")
        setActivityBackgroundColor()
        observerData()
        clickFloatingBtn()
    }

    private fun observerData() {
        homeViewModel.getHomeDaily.observe(viewLifecycleOwner) { homeDaily ->
            homeAdapter.submitList(homeDaily)
        }
        homeViewModel.patchCheckResult.observe(viewLifecycleOwner) { isCheck ->
            homeViewModel.getHomeDaily("2023-05-17")
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
        val bottomSheetFragment = HomeMenuBottomSheetFragment()
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }

    private fun todoItemClick(id: Long, check: String) {
        homeViewModel.patchTodo(id, check)
    }

    private fun clickFloatingBtn() {
        val intent = Intent(context, AdditionActivity::class.java)
        binding.ftbHomeAdd.setOnClickListener { startActivity(intent) }
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