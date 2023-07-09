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
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.databinding.ActivityMyPageInformationBinding
import kr.co.nottodo.listeners.OnDialogDismissListener
import kr.co.nottodo.presentation.login.view.LoginActivity
import kr.co.nottodo.presentation.login.view.LoginActivity.Companion.DID_USER_CHOOSE_TO_BE_NOTIFIED
import kr.co.nottodo.presentation.mypage.viewmodel.MyPageInformationViewModel
import kr.co.nottodo.util.showToast


class MyPageInformationActivity : AppCompatActivity(), OnDialogDismissListener {
    lateinit var binding: ActivityMyPageInformationBinding
    private val viewModel by viewModels<MyPageInformationViewModel>()
    private val withdrawalDialogFragment by lazy { WithdrawalDialogFragment() }
    private val withdrawalFeedbackDialogFragment by lazy { WithdrawalFeedbackDialogFragment() }
    private val myPageLogoutDialogFragment by lazy { MyPageLogoutDialogFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setViews()
        setClickEvents()
        setObservers()
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
            showToast(errorMessage)
        }
    }

    private fun setWithdrawalSuccessObserver() {
        viewModel.withdrawalSuccessResponse.observe(this) {
            withdrawalDialogFragment.dismiss()
            withdrawalFeedbackDialogFragment.show(
                supportFragmentManager, withdrawalFeedbackDialogFragment.tag
            )
        }
    }

    private fun setViews() {
        setRootView()
        setUserName()
        setUserEmail()
    }

    private fun setRootView() {
        binding = ActivityMyPageInformationBinding.inflate(layoutInflater)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }

    private fun setUserEmail() {
        binding.tvMyPageInformationEmail.text =
            SharedPreferences.getString(LoginActivity.USER_EMAIL) ?: "연동된 이메일 정보가 없습니다."
    }

    private fun setUserName() {
        binding.tvMyPageInformationName.text =
            SharedPreferences.getString(LoginActivity.USER_NAME) ?: "익명의 도전자"
    }

    override fun onResume() {
        super.onResume()
        setData()
        setNotificationPermissionSwitchChecked()
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
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName")
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
        startActivity(Intent(this, LoginActivity::class.java))
        if (!isFinishing) finish()
    }

    override fun onDialogDismiss() {
        logout()
    }
}












