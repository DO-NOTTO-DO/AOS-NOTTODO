package kr.co.nottodo.presentation.mypage.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import kr.co.nottodo.R
import kr.co.nottodo.databinding.FragmentWithdrawalDialogBinding

class WithdrawalDialogFragment : DialogFragment() {
    private var _binding: FragmentWithdrawalDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWithdrawalDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 취소 버튼 클릭 시
        binding.tvCancel.setOnClickListener {
            dismiss() // 다이얼로그 닫기
        }

        // 회원탈퇴 버튼 클릭 시
        binding.tvWithdrawal.setOnClickListener {
            val withdrawalFeedbackDialog = WithdrawalFeedbackDialogFragment()
            withdrawalFeedbackDialog.show(requireFragmentManager(), "WithdrawalFeedbackDialog")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun performWithdrawal() {

        dismiss() // 다이얼로그 닫기
    }
}