package kr.co.nottodo.presentation.mypage.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import kr.co.nottodo.databinding.FragmentWithdrawalDialogBinding
import kr.co.nottodo.presentation.mypage.viewmodel.MyPageInformationViewModel

class WithdrawalDialogFragment : DialogFragment() {
    private var _binding: FragmentWithdrawalDialogBinding? = null
    private val binding get() = _binding!!
    private val activityViewModel by activityViewModels<MyPageInformationViewModel>()

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
            activityViewModel.withdrawal()
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