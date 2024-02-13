package kr.co.nottodo.presentation.common.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.view.View
import androidx.fragment.app.viewModels
import kr.co.nottodo.R
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.databinding.FragmentCommonDialogBinding
import kr.co.nottodo.presentation.base.fragment.DataBindingDialogFragment
import kr.co.nottodo.presentation.common.viewmodel.CommonDialogViewModel
import kr.co.nottodo.presentation.common.viewmodel.CommonDialogViewModel.EventType
import kr.co.nottodo.util.NotTodoAmplitude
import kr.co.nottodo.util.PublicString.STOP_WATCHING_COMMON_DIALOG

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
        setTextSpannable()
    }

    private fun setDialogRounded() {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.rectangle_radius_20)
    }

    private fun setTextSpannable() {
        SpannableStringBuilder(getString(R.string.common_dialog_title)).apply {
            setSpan(
                BackgroundColorSpan(requireContext().getColor(R.color.green_2_4ae59b)),
                7,
                12,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
        }.also { spannedString -> binding.tvCommonDialogTitle.text = spannedString }
    }

    private fun handleClickEvents() {
        viewModel.event.observe(viewLifecycleOwner) {
            checkIsCheckBoxChecked()
            when (it) {
                EventType.DISMISS -> {
                    NotTodoAmplitude.trackEventWithProperty(
                        eventName = getString(R.string.click_ad_modal_close),
                        propertyName = getString(R.string.again),
                        propertyValue = getString(if (viewModel.isCheckBoxChecked.value == true) R.string.yes_eng else R.string.no_eng),
                    )
                }

                EventType.NAVIGATE -> {
                    navigateToOpenChat()
                    NotTodoAmplitude.trackEvent(eventName = getString(R.string.click_ad_modal_cta))
                }
            }
            dismiss()
        }
    }

    private fun checkIsCheckBoxChecked() {
        if (viewModel.isCheckBoxChecked.value == true) {
            SharedPreferences.setBoolean(STOP_WATCHING_COMMON_DIALOG, true)
        }
    }

    private fun navigateToOpenChat() = Intent(
        Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_open_chat))
    ).also { intent -> startActivity(intent) }

    override fun bindViewModelWithBinding() {
        binding.vm = viewModel
    }

    companion object {
        fun newInstance() = CommonDialogFragment()
    }
}