package kr.co.nottodo.presentation.mypage.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kr.co.nottodo.R
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.databinding.FragmentMyPageInformationBinding
import kr.co.nottodo.listeners.OnDialogDismissListener
import kr.co.nottodo.presentation.base.fragment.DataBindingFragment
import kr.co.nottodo.presentation.login.view.LoginActivity
import kr.co.nottodo.presentation.login.view.LoginActivity.Companion.DID_USER_CHOOSE_TO_BE_NOTIFIED
import kr.co.nottodo.presentation.mypage.viewmodel.MyPageInformationViewModel
import kr.co.nottodo.util.NotTodoAmplitude.trackEvent
import kr.co.nottodo.util.PublicString.NO_INTERNET_CONDITION_ERROR
import kr.co.nottodo.util.showNotTodoSnackBar
import kr.co.nottodo.util.showToast


class MyPageInformationFragment :
    DataBindingFragment<FragmentMyPageInformationBinding>(R.layout.fragment_my_page_information),
    OnDialogDismissListener {

    private val viewModel: MyPageInformationViewModel by viewModels()
    private val withdrawalDialogFragment by lazy { WithdrawalDialogFragment.newInstance() }
    private val withdrawalFeedbackDialogFragment by lazy { WithdrawalFeedbackDialogFragment.newInstance() }
    private val myPageLogoutDialogFragment by lazy { MyPageLogoutDialogFragment.newInstance() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackEnterMyPageInformation()
        setViews()
        setClickEvents()
        setObservers()
    }

    private fun trackEnterMyPageInformation() {
        trackEvent(getString(R.string.view_account_info))
    }

    private fun setViews() {
        setUserName()
        setUserEmail()
    }

    private fun setUserEmail() {
        binding.tvMyPageInformationEmail.text =
            SharedPreferences.getString(LoginActivity.USER_EMAIL) ?: getString(R.string.no_email)
    }

    private fun setUserName() {
        binding.tvMyPageInformationName.text =
            SharedPreferences.getString(LoginActivity.USER_NAME) ?: getString(R.string.no_name)
    }

    override fun onResume() {
        super.onResume()
        setData()
        setNotificationPermissionSwitchChecked()
        setCheckedChangeEvents()
    }

    private fun setData() {
        setIsNotificationPermissionValid()
    }

    private fun setIsNotificationPermissionValid() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            viewModel.setIsNotificationPermissionValid(
                isNotificationPermissionValid = ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.POST_NOTIFICATIONS
                ) == PERMISSION_GRANTED
            )
        } else {
            viewModel.setIsNotificationPermissionValid(isNotificationPermissionValid = true)
        }
    }

    private fun setNotificationPermissionSwitchChecked() {
        binding.switchMyPageInformationNotificationPermission.isChecked =
            SharedPreferences.getBoolean(
                DID_USER_CHOOSE_TO_BE_NOTIFIED
            )
    }

    private fun setCheckedChangeEvents() {
        setNotificationPermissionSwitchCheckedChangeEvent()
    }

    private fun setNotificationPermissionSwitchCheckedChangeEvent() {
        binding.switchMyPageInformationNotificationPermission.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                trackEvent(getString(R.string.complete_push_on))
            } else {
                trackEvent(getString(R.string.complete_push_off))
            }
        }
    }

    override fun onPause() {
        super.onPause()
        setDidUserChooseToBeNotified()
    }

    private fun setDidUserChooseToBeNotified() {
        SharedPreferences.setBoolean(
            DID_USER_CHOOSE_TO_BE_NOTIFIED,
            binding.switchMyPageInformationNotificationPermission.isChecked
        )
    }


    private fun setClickEvents() {
        setAlarmLayoutClickEvent()
        setMemberWithdrawalTvClickEvent()
        setLogoutTvClickEvent()
        setBackIvClickEvent()
    }

    private fun setAlarmLayoutClickEvent() {
        binding.layoutMyPageInformationAlarm.setOnClickListener {
            startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse(
                        getString(
                            R.string.package_package_name, requireContext().packageName
                        )
                    )
                )
            )
        }
    }

    private fun setMemberWithdrawalTvClickEvent() {
        binding.tvMyPageInformationMemberWithdrawal.setOnClickListener {
            withdrawalDialogFragment.show(
                requireActivity().supportFragmentManager, withdrawalDialogFragment.tag
            )
        }
    }

    private fun setLogoutTvClickEvent() {
        binding.tvMyPageInformationLogout.setOnClickListener {
            startMyPageLogoutDialog()
        }
    }

    private fun startMyPageLogoutDialog() {
        myPageLogoutDialogFragment.show(
            requireActivity().supportFragmentManager, myPageLogoutDialogFragment.tag
        )
    }

    private fun setBackIvClickEvent() {
        binding.ivMyPageInformationBackArrow.setOnClickListener {
            findNavController().popBackStack()
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
            if (errorMessage == NO_INTERNET_CONDITION_ERROR) requireContext().showNotTodoSnackBar(
                binding.root, NO_INTERNET_CONDITION_ERROR
            )
            else requireContext().showToast(errorMessage)
        }
    }

    private fun setWithdrawalSuccessObserver() {
        viewModel.withdrawalSuccessResponse.observe(viewLifecycleOwner) {
            withdrawalDialogFragment.dismiss()
            withdrawalFeedbackDialogFragment.show(
                requireActivity().supportFragmentManager, withdrawalFeedbackDialogFragment.tag
            )
            trackEvent(getString(R.string.complete_withdrawal))
        }
    }


    override fun onDialogDismiss() {
        logout()
    }

    private fun logout() {
        SharedPreferences.clearForLogout()
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        if (!requireActivity().isFinishing) requireActivity().finish()
    }

    override fun bindViewModelWithBinding() {
        binding.vm = viewModel
    }
}