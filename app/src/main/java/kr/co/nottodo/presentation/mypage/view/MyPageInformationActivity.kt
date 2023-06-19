package kr.co.nottodo.presentation.mypage.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.databinding.ActivityMyPageInformationBinding
import kr.co.nottodo.presentation.login.view.LoginActivity
import kr.co.nottodo.presentation.login.view.LoginActivity.Companion.DID_USER_CHOOSE_TO_BE_NOTIFIED
import kr.co.nottodo.util.showToast


class MyPageInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyPageInformationBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setResultLaunchers()
        setViews()
        setClickEvents()
    }

    private fun setResultLaunchers() {
        setRequestPermissionLauncher()
    }

    private fun setRequestPermissionLauncher() {
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            SharedPreferences.setBoolean(DID_USER_CHOOSE_TO_BE_NOTIFIED, isGranted)
        }
    }

    private fun setViews() {
        setUserName()
        setUserEmail()
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
        setNotificationPermissionSwitchChecked()
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
        setNotificationPermissionSwitchClickEvent()
    }

    private fun setNotificationPermissionSwitchClickEvent() {
        binding.switchMyPageInformationNotificationPermission.setOnClickListener {
            if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                showToast("권한이 거부되었습니다. 설정에서 권한을 허용해주세요")
                return@setOnClickListener
            } else if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_DENIED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            ) {
                showToast("권한 재요청")
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else showToast("?")
        }
    }

    private fun setMemberWithdrawalTvClickEvent() {
        binding.tvMyPageInformationMemberWithdrawal.setOnClickListener {
            WithdrawalDialogFragment().show(supportFragmentManager, "WithdrawalDialog")
        }
    }

    private fun setBackIvClickEvent() {
        binding.ivMyPageInformationBackArrow.setOnClickListener {
            if (!isFinishing) finish()
        }
    }

    private fun setLogoutTvClickEvent() {
        binding.tvMyPageInformationLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        SharedPreferences.clearForLogout()
        startActivity(Intent(this, LoginActivity::class.java))
        if (!isFinishing) finish()
    }
}












