package kr.co.nottodo.presentation.onboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.nottodo.R
import kr.co.nottodo.databinding.FragmentOnboardThirdBinding
import kr.co.nottodo.presentation.onboard.adapter.OnboardPainAdapter
import kr.co.nottodo.presentation.onboard.viewmodel.OnboardViewModel
import kr.co.nottodo.util.Amplitude

class OnboardThirdFragment : Fragment() {
    private var _binding: FragmentOnboardThirdBinding? = null
    private val binding: FragmentOnboardThirdBinding
        get() = requireNotNull(_binding)
    private val viewModel by activityViewModels<OnboardViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOnboardThirdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
        Amplitude.trackEvent(getString(R.string.view_onboarding_2))
    }

    private fun setViews() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.rvOnboardThird.adapter = OnboardPainAdapter(
            requireContext(),
            viewModel.painList,
        )
        binding.rvOnboardThird.layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically() = false
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}