package kr.co.nottodo.presentation.home.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kr.co.nottodo.MainActivity.Companion.REQUEST_PHONE_STATE_OR_NUMBERS_CODE
import kr.co.nottodo.R
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.databinding.FragmentNotificationPermissionRequestBinding
import kr.co.nottodo.presentation.base.fragment.DataBindingFragment
import kr.co.nottodo.presentation.home.viewmodel.NotificationPermissionRequestViewModel
import kr.co.nottodo.presentation.login.view.LoginFragment.Companion.DID_USER_CHOOSE_TO_BE_NOTIFIED
import kr.co.nottodo.util.PublicString.DID_USER_WATCHED_NOTIFICATION_PERMISSION_FRAGMENT

class NotificationPermissionRequestFragment :
    DataBindingFragment<FragmentNotificationPermissionRequestBinding>(
        R.layout.fragment_notification_permission_request
    ) {
    private val viewModel: NotificationPermissionRequestViewModel by viewModels()
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SharedPreferences.setBoolean(DID_USER_WATCHED_NOTIFICATION_PERMISSION_FRAGMENT, true)
        setResultLaunchers()
        setObservers()
    }

    private fun setResultLaunchers() {
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            SharedPreferences.setBoolean(DID_USER_CHOOSE_TO_BE_NOTIFIED, isGranted)
            findNavController().popBackStack()
        }
    }

    private fun setObservers() {
        setCompleteBtnClickHandlerObserver()
    }

    private fun setCompleteBtnClickHandlerObserver() {
        viewModel.completeBtnClickHandler.observe(viewLifecycleOwner) {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        requestNotificationPermission()
        requestPhoneStateOrNumbers()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            SharedPreferences.setBoolean(DID_USER_CHOOSE_TO_BE_NOTIFIED, true)
        }
    }

    private fun requestPhoneStateOrNumbers() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Manifest.permission.READ_PHONE_NUMBERS else Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED
        ) return

        val permissions: Array<String> = arrayOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Manifest.permission.READ_PHONE_NUMBERS else Manifest.permission.READ_PHONE_STATE
        )
        requireActivity().requestPermissions(permissions, REQUEST_PHONE_STATE_OR_NUMBERS_CODE)
    }

    override fun bindViewModelWithBinding() {
        binding.vm = viewModel
    }

}