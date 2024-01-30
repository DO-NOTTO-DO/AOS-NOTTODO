package kr.co.nottodo.presentation.mypage.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kr.co.nottodo.MainActivity
import kr.co.nottodo.R
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.databinding.ActivityMyPageInformationBinding
import kr.co.nottodo.listeners.OnWithdrawalDialogDismissListener
import kr.co.nottodo.presentation.login.view.LoginFragment.Companion.DID_USER_CHOOSE_TO_BE_NOTIFIED
import kr.co.nottodo.presentation.login.view.LoginFragment.Companion.USER_EMAIL
import kr.co.nottodo.presentation.login.view.LoginFragment.Companion.USER_NAME
import kr.co.nottodo.presentation.mypage.viewmodel.MyPageInformationViewModel
import kr.co.nottodo.util.NotTodoAmplitude.trackEvent
import kr.co.nottodo.util.PublicString.NO_INTERNET_CONDITION_ERROR
import kr.co.nottodo.util.showNotTodoSnackBar
import kr.co.nottodo.util.showToast


class MyPageInformationActivity : AppCompatActivity(), OnWithdrawalDialogDismissListener {
    lateinit var binding: ActivityMyPageInformationBinding
    private val viewModel by viewModels<MyPageInformationViewModel>()
    private val withdrawalDialogFragment by lazy { WithdrawalDialogFragment() }
    private val withdrawalFeedbackDialogFragment by lazy { WithdrawalFeedbackDialogFragment() }
    private val myPageLogoutDialogFragment by lazy { MyPageLogoutDialogFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trackEvent(getString(R.string.view_account_info))
        setDataBinding()
        setViews()
        setClickEvents()
        setObservers()
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

    private fun setDataBinding() {
        binding = ActivityMyPageInformationBinding.inflate(layoutInflater)
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }

    private fun setObservers() {
        setWithdrawalObserver()
    }

    private fun setWithdrawalObserver() {
        setWithdrawalSuccessObserver()
        setWithdrawalErrorObserver()
    }

    private fun setWithdrawalErrorObserver() {
        viewModel.withdrawalErrorResponse.observe(this) { errorMessage ->
            if (errorMessage == NO_INTERNET_CONDITION_ERROR) showNotTodoSnackBar(
                binding.root, NO_INTERNET_CONDITION_ERROR
            )
            else showToast(errorMessage)
        }
    }

    private fun setWithdrawalSuccessObserver() {
        viewModel.withdrawalSuccessResponse.observe(this) {
            withdrawalDialogFragment.dismiss()
            withdrawalFeedbackDialogFragment.show(
                supportFragmentManager, withdrawalFeedbackDialogFragment.tag
            )
            trackEvent(getString(R.string.complete_withdrawal))
        }
    }

    private fun setViews() {
        setContentView(binding.root)
        setUserName()
        setUserEmail()
    }

    private fun setUserEmail() {
        binding.tvMyPageInformationEmail.text =
            SharedPreferences.getString(USER_EMAIL) ?: getString(R.string.no_email)
    }

    private fun setUserName() {
        binding.tvMyPageInformationName.text =
            SharedPreferences.getString(USER_NAME) ?: getString(R.string.no_name)
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
                    this, Manifest.permission.POST_NOTIFICATIONS
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
        setLogoutTvClickEvent()
        setBackIvClickEvent()
        setMemberWithdrawalTvClickEvent()
        setAlarmLayoutClickEvent()
    }

    private fun setAlarmLayoutClickEvent() {
        binding.layoutMyPageInformationAlarm.setOnClickListener {
            startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse(getString(R.string.package_package_name, packageName))
                )
            )
        }
    }

    private fun setMemberWithdrawalTvClickEvent() {
        binding.tvMyPageInformationMemberWithdrawal.setOnClickListener {
            withdrawalDialogFragment.show(supportFragmentManager, withdrawalDialogFragment.tag)
        }
    }

    private fun setBackIvClickEvent() {
        binding.ivMyPageInformationBackArrow.setOnClickListener {
            if (!isFinishing) finish()
        }
    }

    private fun setLogoutTvClickEvent() {
        binding.tvMyPageInformationLogout.setOnClickListener {
            startMyPageLogoutDialog()
        }
    }

    private fun startMyPageLogoutDialog() {
        myPageLogoutDialogFragment.show(supportFragmentManager, myPageLogoutDialogFragment.tag)
    }

    private fun logout() {
        SharedPreferences.clearForLogout()
        startActivity(Intent(this, MainActivity::class.java))
        if (!isFinishing) finish()
    }

    override fun onWithdrawalDialogDismiss() {
        logout()
    }
}