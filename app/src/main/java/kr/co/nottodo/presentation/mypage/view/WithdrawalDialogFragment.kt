package kr.co.nottodo.presentation.mypage.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import kr.co.nottodo.R
import kr.co.nottodo.databinding.FragmentWithdrawalDialogBinding
import kr.co.nottodo.presentation.mypage.viewmodel.WithdrawalDialogViewModel
import kr.co.nottodo.util.NotTodoAmplitude.trackEvent
import kr.co.nottodo.util.PublicString
import kr.co.nottodo.util.showNotTodoSnackBar
import kr.co.nottodo.util.showToast

class WithdrawalDialogFragment : DialogFragment() {
    private var _binding: FragmentWithdrawalDialogBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: WithdrawalDialogViewModel by viewModels()
    private val withdrawalFeedbackDialogFragment by lazy { WithdrawalFeedbackDialogFragment.newInstance() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWithdrawalDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trackEvent(getString(R.string.appear_withdrawal_modal))
        setClickEvents()
        setObservers()
    }

    private fun setClickEvents() {
        setCancelTvClickEvent()
        setWithdrawalTvClickEvent()
    }

    private fun setWithdrawalTvClickEvent() {
        binding.tvWithdrawal.setOnClickListener {
            viewModel.withdrawal()
        }
    }

    private fun setCancelTvClickEvent() {
        binding.tvCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun setObservers() {
        setWithdrawalObserver()
    }

    private fun setWithdrawalObserver() {
        setWithdrawalSuccessObserver()
        setWithdrawalErrorObserver()
    }

    private fun setWithdrawalErrorObserver() {
        viewModel.withdrawalErrorResponse.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage == PublicString.NO_INTERNET_CONDITION_ERROR) requireContext().showNotTodoSnackBar(
                binding.root, PublicString.NO_INTERNET_CONDITION_ERROR
            )
            else requireContext().showToast(errorMessage)
        }
    }

    private fun setWithdrawalSuccessObserver() {
        viewModel.withdrawalSuccessResponse.observe(viewLifecycleOwner) {
            withdrawalFeedbackDialogFragment.show(
                requireActivity().supportFragmentManager, withdrawalFeedbackDialogFragment.tag
            )
            trackEvent(getString(R.string.complete_withdrawal))
            dismiss()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = WithdrawalDialogFragment()
    }
}