package kr.co.nottodo.presentation.onboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.nottodo.R
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.databinding.FragmentOnboardSixthBinding
import kr.co.nottodo.presentation.base.fragment.ViewBindingFragment
import kr.co.nottodo.presentation.login.view.LoginActivity.Companion.DID_USER_WATCHED_ONBOARD
import kr.co.nottodo.presentation.onboard.adapter.OnboardActionAdapter
import kr.co.nottodo.presentation.onboard.viewmodel.OnboardViewModel
import kr.co.nottodo.util.NotTodoAmplitude.trackEvent

class OnboardSixthFragment : ViewBindingFragment<FragmentOnboardSixthBinding>() {
    private val viewModel by viewModels<OnboardViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trackEvent(getString(R.string.view_onboarding_5))
        initRecyclerView()
        initLoginLayoutClickListener()
    }

    private fun initLoginLayoutClickListener() {
        binding.layoutOnboardSixthLogin.setOnClickListener {
            trackEvent(getString(R.string.click_onboarding_next_5))
            SharedPreferences.setBoolean(DID_USER_WATCHED_ONBOARD, true)
            findNavController().navigate(R.id.action_onboardSixthFragment_to_loginFragment)
        }
    }

    private fun initRecyclerView() {
        with(binding.rvOnboardSixth) {
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            adapter = OnboardActionAdapter(
                requireContext(), viewModel.actionList
            )
        }
    }

    override fun setBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentOnboardSixthBinding = FragmentOnboardSixthBinding.inflate(inflater, container, false)
}