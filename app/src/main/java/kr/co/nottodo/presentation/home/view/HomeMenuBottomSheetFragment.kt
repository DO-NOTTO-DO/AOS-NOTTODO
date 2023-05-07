package kr.co.nottodo.presentation.home.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.co.nottodo.databinding.FragmentHomeMenuBottomSheetBinding

class HomeMenuBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentHomeMenuBottomSheetBinding? = null
    private val binding: FragmentHomeMenuBottomSheetBinding
        get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeMenuBottomSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDialog()
        clickBack()
    }

    private fun initDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())

        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.behavior.saveFlags = BottomSheetBehavior.SAVE_FIT_TO_CONTENTS
    }

    private fun clickBack() {
        binding.ivHomeDialogCancle.setOnClickListener { dismiss() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}