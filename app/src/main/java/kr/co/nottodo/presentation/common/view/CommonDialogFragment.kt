package kr.co.nottodo.presentation.common.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import kr.co.nottodo.R
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.databinding.FragmentCommonDialogBinding
import kr.co.nottodo.presentation.base.fragment.DataBindingDialogFragment
import kr.co.nottodo.presentation.common.viewmodel.CommonDialogViewModel
import kr.co.nottodo.util.PublicString.STOP_WATCHING_COMMON_DIALOG
import kr.co.nottodo.util.observeEvent

class CommonDialogFragment :
    DataBindingDialogFragment<FragmentCommonDialogBinding>(R.layout.fragment_common_dialog) {
    private val viewModel: CommonDialogViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setView()
        handleClickEvents()
    }

    private fun setView() {
        setDialogRounded()
    }

    private fun setDialogRounded() {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.rectangle_radius_20)
    }

    private fun handleClickEvents() {
        handleClickBtnEvent()
        handleClickDismissTvEvent()
    }

    private fun handleClickDismissTvEvent() {
        viewModel.dismissTvClickEvent.observeEvent(viewLifecycleOwner) {
            checkIsCheckBoxChecked()
            dismiss()
        }
    }

    private fun handleClickBtnEvent() {
        viewModel.clickBtnEvent.observeEvent(viewLifecycleOwner) {
            startFeedbackPage()
            checkIsCheckBoxChecked()
            dismiss()
        }
    }

    private fun checkIsCheckBoxChecked() {
        if (viewModel.isCheckBoxChecked.value == true) {
            SharedPreferences.setBoolean(STOP_WATCHING_COMMON_DIALOG, true)
        }
    }

    private fun startFeedbackPage() = Intent(
        Intent.ACTION_VIEW, Uri.parse("https://www.naver.com")
    ).also { intent -> startActivity(intent) }

    override fun bindViewModelWithBinding() {
        binding.vm = viewModel
    }

    companion object {
        fun newInstance() = CommonDialogFragment()
    }
}