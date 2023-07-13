package kr.co.nottodo.presentation.mypage.view

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kr.co.nottodo.R
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.presentation.login.view.LoginActivity
import kr.co.nottodo.util.NotTodoAmplitude.trackEvent

class MyPageLogoutDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        trackEvent(getString(R.string.appear_logout_modal))
        val builder = AlertDialog.Builder(requireActivity()).apply {
            setTitle(getString(R.string.do_you_want_to_logout))
            setMessage(getString(R.string.you_can_not_sync_history))
            setPositiveButton(getString(R.string.logout)) { _, _ ->
                logout()
            }
            setNegativeButton(getString(R.string.cancel)) { _, _ ->
                dismiss()
            }
        }

        return builder.create()
    }

    private fun logout() {
        trackEvent(getString(R.string.complete_logout))
        SharedPreferences.clearForLogout()
        startActivity(
            Intent(
                requireContext(), LoginActivity::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        if (!requireActivity().isFinishing) requireActivity().finish()
    }
}