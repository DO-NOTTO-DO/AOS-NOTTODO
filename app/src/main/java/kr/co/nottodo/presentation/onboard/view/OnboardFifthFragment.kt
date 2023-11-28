package kr.co.nottodo.presentation.onboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.nottodo.R
import kr.co.nottodo.databinding.FragmentOnboardFifthBinding
import kr.co.nottodo.presentation.base.fragment.ViewBindingFragment
import kr.co.nottodo.presentation.onboard.adapter.OnboardMissionAdapter
import kr.co.nottodo.presentation.onboard.viewmodel.OnboardViewModel
import kr.co.nottodo.util.NotTodoAmplitude.trackEvent

class OnboardFifthFragment : ViewBindingFragment<FragmentOnboardFifthBinding>() {
    private val viewModel by viewModels<OnboardViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trackEvent(getString(R.string.view_onboarding_4))
        initRecyclerView()
        initNextLayoutClickListener()
    }

    private fun initNextLayoutClickListener() {
        binding.tvOnboardFifthNext.setOnClickListener {
            findNavController().navigate(R.id.action_onboardFifthFragment_to_onboardSixthFragment)
            trackEvent(getString(R.string.click_onboarding_next_4))
        }
    }

    private fun initRecyclerView() {
        binding.rvOnboardFifth.adapter = OnboardMissionAdapter(
            requireContext(), viewModel.missionList
        )
        binding.rvOnboardFifth.layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
    }

    override fun setBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentOnboardFifthBinding = FragmentOnboardFifthBinding.inflate(inflater, container, false)
}