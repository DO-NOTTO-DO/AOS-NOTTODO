package kr.co.nottodo.presentation.onboard.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.nottodo.databinding.FragmentOnboardSixthBinding
import kr.co.nottodo.presentation.login.view.LoginActivity
import kr.co.nottodo.presentation.onboard.adapter.OnboardActionAdapter
import kr.co.nottodo.presentation.onboard.viewmodel.OnboardViewModel

class OnboardSixthFragment : Fragment() {
    private var _binding: FragmentOnboardSixthBinding? = null
    private val binding: FragmentOnboardSixthBinding
        get() = requireNotNull(_binding)
    private val viewModel by activityViewModels<OnboardViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOnboardSixthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initLoginLayoutClickListener()
    }

    private fun initLoginLayoutClickListener() {
        binding.layoutOnboardSixthLogin.setOnClickListener {
            requireActivity().apply {
                startActivity(Intent(context, LoginActivity::class.java))
                if (!isFinishing) {
                    finish()
                }
            }
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
                requireContext(),
                viewModel.actionList
            )
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}