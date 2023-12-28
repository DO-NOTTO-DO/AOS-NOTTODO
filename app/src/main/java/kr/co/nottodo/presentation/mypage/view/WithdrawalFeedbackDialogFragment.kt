package kr.co.nottodo.presentation.mypage.view

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import kr.co.nottodo.R
import kr.co.nottodo.databinding.FragmentWithdrawalFeedbackDialogBinding
import kr.co.nottodo.listeners.OnWithdrawalDialogDismissListener

class WithdrawalFeedbackDialogFragment : DialogFragment() {
    private var _binding: FragmentWithdrawalFeedbackDialogBinding? = null
    private val binding get() = _binding!!
    private var onWithdrawalDialogDismissListener: OnWithdrawalDialogDismissListener? = null
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onWithdrawalDialogDismissListener = context as? OnWithdrawalDialogDismissListener ?: throw TypeCastException(
            getString(
                R.string.context_can_not_cast_as, getString(R.string.on_dialog_dismiss_listener)
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResultLaunchers()
    }

    private fun setResultLaunchers() {
        setFeedBackResultLauncher()
    }

    private fun setFeedBackResultLauncher() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                dismiss()
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWithdrawalFeedbackDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFeedback.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_feedback_withdrawal))
            )
            resultLauncher.launch(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        onWithdrawalDialogDismissListener = null
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onWithdrawalDialogDismissListener?.onWithdrawalDialogDismiss() ?: throw NullPointerException(
            getString(R.string._is_null, getString(R.string.on_dialog_dismiss_listener))
        )
    }

    companion object {
        fun newInstance() = WithdrawalFeedbackDialogFragment()
    }
}