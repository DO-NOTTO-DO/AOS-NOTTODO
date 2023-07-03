package kr.co.nottodo.presentation.home.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kr.co.nottodo.databinding.FragmentNotTodoDeleteDialogBinding

class HomeNottodoDeleteFragment : DialogFragment() {
    private var _binding: FragmentNotTodoDeleteDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var buttonClickListener: DialogCloseListener
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentNotTodoDeleteDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // 각 버튼 클릭 시 각각의 함수 호출
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickDelete()
        clickCancle()
    }

    // 클릭 이벤트 설정
    fun setDeleteButtonClickListener(buttonClickListener: DialogCloseListener) {
        this.buttonClickListener = buttonClickListener
    }

    // 클릭 이벤트 실행

    private fun clickDelete() {
        binding.btnDeleteDialogDelete.setOnClickListener {
            buttonClickListener.onDeleteButtonClicked()
            dismiss()
        }
    }

    private fun clickCancle() {
        binding.btnDeleteDialogCancle.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
