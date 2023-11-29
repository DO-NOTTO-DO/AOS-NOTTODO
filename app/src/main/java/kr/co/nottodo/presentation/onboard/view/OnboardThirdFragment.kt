package kr.co.nottodo.presentation.onboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.nottodo.R
import kr.co.nottodo.databinding.FragmentOnboardThirdBinding
import kr.co.nottodo.presentation.base.fragment.ViewBindingFragment
import kr.co.nottodo.presentation.onboard.adapter.OnboardPainAdapter
import kr.co.nottodo.presentation.onboard.viewmodel.OnboardViewModel
import kr.co.nottodo.util.NotTodoAmplitude.trackEvent

class OnboardThirdFragment : ViewBindingFragment<FragmentOnboardThirdBinding>() {
    private val viewModel by viewModels<OnboardViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
        trackEvent(getString(R.string.view_onboarding_2))
    }

    private fun setViews() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.rvOnboardThird.adapter = OnboardPainAdapter(
            viewModel.painList, navigateToOnboardFourthFragment
        )
        binding.rvOnboardThird.layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically() = false
        }
    }

    private val navigateToOnboardFourthFragment =
        { findNavController().navigate(R.id.action_onboardThirdFragment_to_onboardFourthFragment) }

    override fun setBinding(
        inflater: LayoutInflater, container: ViewGroup?,
    ): FragmentOnboardThirdBinding = FragmentOnboardThirdBinding.inflate(inflater, container, false)
}