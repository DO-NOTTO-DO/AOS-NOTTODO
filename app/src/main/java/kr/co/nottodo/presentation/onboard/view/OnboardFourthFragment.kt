package kr.co.nottodo.presentation.onboard.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import kr.co.nottodo.R
import kr.co.nottodo.databinding.FragmentOnboardFourthBinding
import kr.co.nottodo.presentation.onboard.OnboardInterface
import kr.co.nottodo.presentation.onboard.adapter.OnboardSituationAdapter
import kr.co.nottodo.presentation.onboard.viewmodel.OnboardViewModel
import kr.co.nottodo.util.NotTodoAmplitude
import kr.co.nottodo.util.NotTodoAmplitude.trackEventWithProperty

class OnboardFourthFragment : Fragment() {
    private var _binding: FragmentOnboardFourthBinding? = null
    private val binding: FragmentOnboardFourthBinding
        get() = requireNotNull(_binding)
    private lateinit var onboardInterface: OnboardInterface
    private val viewModel by activityViewModels<OnboardViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initInterface()
    }

    private fun initInterface() {
        onboardInterface = context as OnboardInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOnboardFourthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        NotTodoAmplitude.trackEvent(getString(R.string.view_onboarding_3))
        initRecyclerView()
        initCompleteBtnClickListener()
        observeIsBtnClickable()
    }

    private fun initCompleteBtnClickListener() {
        binding.btnOnboardFourthComplete.setOnClickListener {
            if (isCompleteBtnClickable()) {
                onboardInterface.changeFragment(OnboardFifthFragment())
                onboardInterface.setIndicatorNext()
                trackEventWithProperty(
                    getString(R.string.click_onboarding_next_3),
                    binding.root.context.getString(R.string.onboard_select),
                    viewModel.getSelectedSituations()?.toList()
                )
            }
        }
    }

    private fun isCompleteBtnClickable(): Boolean {
        return binding.btnOnboardFourthComplete.currentTextColor == getColor(
            requireContext(), R.color.black
        )
    }

    private fun observeIsBtnClickable() {
        viewModel.selectedSituationsCount.observe(
            viewLifecycleOwner
        ) { selectedSituationsCount ->
            if (selectedSituationsCount > 0) {
                setBtnClickable()
            } else {
                setBtnNonClickable()
            }
        }
    }

    private fun setBtnClickable() {
        with(binding.btnOnboardFourthComplete) {
            setTextColor(
                getColor(requireContext(), R.color.black)
            )
            setBackgroundResource(R.drawable.rectangle_solid_white_radius_50)
        }
    }

    private fun setBtnNonClickable() {
        with(binding.btnOnboardFourthComplete) {
            setTextColor(
                getColor(
                    requireContext(), R.color.gray_4_9398aa
                )
            )
            setBackgroundResource(R.drawable.rectangle_solid_gray_2_radius_50)
        }
    }

    private fun initRecyclerView() {
        binding.rvOnboardFourth.adapter = OnboardSituationAdapter(
            requireContext(),
            viewModel.situationList,
            viewModel.addSituation,
            viewModel.removeSituation
        )
        binding.rvOnboardFourth.layoutManager = object : GridLayoutManager(context, 2) {
            override fun canScrollVertically(): Boolean = false
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}