package kr.co.nottodo.presentation.onboard.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.co.nottodo.databinding.FragmentOnboardFifthBinding
import kr.co.nottodo.presentation.onboard.OnboardInterface
import kr.co.nottodo.presentation.onboard.adapter.OnboardMissionAdapter
import kr.co.nottodo.presentation.onboard.viewmodel.OnboardViewModel

class OnboardFifthFragment : Fragment() {
    private var _binding: FragmentOnboardFifthBinding? = null
    private val binding: FragmentOnboardFifthBinding
        get() = requireNotNull(_binding)
    lateinit var onboardInterface: OnboardInterface

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onboardInterface = context as OnboardInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOnboardFifthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
    }

    private fun initAdapter() {
        binding.rvOnboardFifth.adapter = OnboardMissionAdapter(
            requireContext(),
            ViewModelProvider(requireActivity())[OnboardViewModel::class.java].missionList
        )
        binding.layoutOnboardFifthNext.setOnClickListener {
            onboardInterface.changeFragment(OnboardSixthFragment())
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}