package kr.co.nottodo.presentation.onboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.co.nottodo.databinding.FragmentOnboardThirdBinding
import kr.co.nottodo.presentation.onboard.adapter.OnboardPainAdapter
import kr.co.nottodo.presentation.onboard.viewmodel.OnboardViewModel

class OnboardThirdFragment : Fragment() {
    private var _binding: FragmentOnboardThirdBinding? = null
    private val binding: FragmentOnboardThirdBinding
        get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOnboardThirdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
    }

    private fun initAdapter() {
        binding.rvOnboardThird.adapter = OnboardPainAdapter(
            requireContext(),
            ViewModelProvider(requireActivity())[OnboardViewModel::class.java].painList,
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}