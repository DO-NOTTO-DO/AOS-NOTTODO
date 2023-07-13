package kr.co.nottodo.presentation.mypage.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import kr.co.nottodo.R
import kr.co.nottodo.databinding.FragmentWithdrawalDialogBinding
import kr.co.nottodo.presentation.mypage.viewmodel.MyPageInformationViewModel
import kr.co.nottodo.util.NotTodoAmplitude.trackEvent

class WithdrawalDialogFragment : DialogFragment() {
    private var _binding: FragmentWithdrawalDialogBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val activityViewModel by activityViewModels<MyPageInformationViewModel>()

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
        setClickEvents()
        trackEvent(getString(R.string.appear_withdrawal_modal))
    }

    private fun setClickEvents() {
        setCancelTvClickEvent()
        setWithdrawalTvClickEvent()
    }

    private fun setWithdrawalTvClickEvent() {
        binding.tvWithdrawal.setOnClickListener {
            activityViewModel.withdrawal()
        }
    }

    private fun setCancelTvClickEvent() {
        binding.tvCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}