package kr.co.nottodo.presentation.mypage.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kr.co.nottodo.R
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.databinding.FragmentMyPageInformationBinding
import kr.co.nottodo.presentation.base.fragment.DataBindingFragment
import kr.co.nottodo.presentation.login.view.LoginActivity
import kr.co.nottodo.presentation.login.view.LoginActivity.Companion.DID_USER_CHOOSE_TO_BE_NOTIFIED
import kr.co.nottodo.presentation.mypage.viewmodel.MyPageInformationNewViewModel
import kr.co.nottodo.util.NotTodoAmplitude.trackEvent

class MyPageInformationFragment :
    DataBindingFragment<FragmentMyPageInformationBinding>(R.layout.fragment_my_page_information) {

    private val viewModel: MyPageInformationNewViewModel by viewModels()
    private val withdrawalDialogFragment by lazy { WithdrawalDialogFragment.newInstance() }
    private val myPageLogoutDialogFragment by lazy { MyPageLogoutDialogFragment.newInstance() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackEnterMyPageInformation()
        setViews()
        setClickEvents()
        setNotificationPermissionSwitchChecked()
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
        setCheckedChangeEvents()
    }

    private fun setData() {
        setIsNotificationPermissionValid()
    }

    private fun setIsNotificationPermissionValid() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            viewModel.setIsNotificationPermissionValid(
                isNotificationPermissionValid = ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS,
                ) == PERMISSION_GRANTED,
            )
        } else {
            viewModel.setIsNotificationPermissionValid(isNotificationPermissionValid = true)
        }
    }

    private fun setNotificationPermissionSwitchChecked() {
        val isSystemSwitch = checkAlamSystem()
        binding.switchMyPageInformationNotificationPermission.isChecked =
            SharedPreferences.getBoolean(
                DID_USER_CHOOSE_TO_BE_NOTIFIED,
            )
        if (binding.switchMyPageInformationNotificationPermission.isChecked != isSystemSwitch) {
            binding.switchMyPageInformationNotificationPermission.isChecked = isSystemSwitch!!
        }
    }

    private fun checkAlamSystem() = context?.let {
        NotificationManagerCompat.from(it).areNotificationsEnabled()
    }

    private fun setCheckedChangeEvents() {
        setNotificationPermissionSwitchCheckedChangeEvent()
    }

    private fun setNotificationPermissionSwitchCheckedChangeEvent() {
        val isSystemSwitch = checkAlamSystem()
        binding.switchMyPageInformationNotificationPermission.setOnCheckedChangeListener { _, isChecked ->
            navigateToSystemSetting(isSystemSwitch!!, isChecked)
            if (isChecked) {
                trackEvent(getString(R.string.complete_push_on))
            } else {
                trackEvent(getString(R.string.complete_push_off))
            }
        }
    }

    private fun navigateToSystemSetting(isSystemSwitch: Boolean, isAppCheck: Boolean) {
        if (isSystemSwitch != isAppCheck) {
            val intent = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    Intent().apply {
                        action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                        putExtra(Settings.EXTRA_APP_PACKAGE, activity?.packageName)
                    }
                }

                else -> {
                    Intent().apply {
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        addCategory(Intent.CATEGORY_DEFAULT)
                        data = Uri.parse("package:${activity?.packageName}")
                    }
                }
            }
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        setDidUserChooseToBeNotified()
    }

    private fun setDidUserChooseToBeNotified() {
        SharedPreferences.setBoolean(
            DID_USER_CHOOSE_TO_BE_NOTIFIED,
            binding.switchMyPageInformationNotificationPermission.isChecked,
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
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse(
                        getString(
                            R.string.package_package_name,
                            requireContext().packageName,
                        ),
                    ),
                ),
            )
        }
    }

    private fun setMemberWithdrawalTvClickEvent() {
        binding.tvMyPageInformationMemberWithdrawal.setOnClickListener {
            withdrawalDialogFragment.show(
                requireActivity().supportFragmentManager,
                withdrawalDialogFragment.tag,
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
            requireActivity().supportFragmentManager,
            myPageLogoutDialogFragment.tag,
        )
    }

    private fun setBackIvClickEvent() {
        binding.ivMyPageInformationBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun bindViewModelWithBinding() {
        binding.vm = viewModel
    }
}
