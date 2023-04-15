package kr.co.nottodo.presentation.onboard.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import kr.co.nottodo.R
import kr.co.nottodo.databinding.FragmentOnboardFourthBinding
import kr.co.nottodo.presentation.onboard.OnboardInterface
import kr.co.nottodo.presentation.onboard.adapter.OnboardSituationAdapter
import kr.co.nottodo.presentation.onboard.viewmodel.OnboardViewModel

class OnboardFourthFragment : Fragment() {
    private var _binding: FragmentOnboardFourthBinding? = null
    private val binding: FragmentOnboardFourthBinding
        get() = requireNotNull(_binding)
    private lateinit var onboardInterface: OnboardInterface

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
        initRecyclerView()
        initCompleteBtnClickListener()
        observeIsBtnClickable()
    }

    private fun initCompleteBtnClickListener() {
        binding.btnOnboardFourthComplete.setOnClickListener {
            if (isCompleteBtnClickable()) {
                onboardInterface.changeFragment(OnboardFifthFragment())
                onboardInterface.setIndicatorNext()
            }
        }
    }

    private fun isCompleteBtnClickable(): Boolean {
        return binding.btnOnboardFourthComplete.currentTextColor == getColor(
            requireContext(), R.color.black
        )
    }

    private fun observeIsBtnClickable() {
        ViewModelProvider(requireActivity())[OnboardViewModel::class.java].isBtnClickable.observe(
            viewLifecycleOwner
        ) { isBtnClickable ->
            if (isBtnClickable) {
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
            ViewModelProvider(requireActivity())[OnboardViewModel::class.java].situationList,
            ViewModelProvider(requireActivity())[OnboardViewModel::class.java].plusOneInSituationCount,
            ViewModelProvider(requireActivity())[OnboardViewModel::class.java].minusOneInSituationCount
        )
        binding.rvOnboardFourth.layoutManager = object : GridLayoutManager(context, 2) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}