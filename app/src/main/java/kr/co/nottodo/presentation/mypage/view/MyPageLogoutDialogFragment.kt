package kr.co.nottodo.presentation.mypage.view

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kr.co.nottodo.data.local.SharedPreferences
import kr.co.nottodo.presentation.login.view.LoginActivity

class MyPageLogoutDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setTitle("로그아웃 하시겠습니까?")
            builder.setMessage("로그아웃을 하면\n다른 기기와 낫투두 기록을 연동하지 못해요.")
                .setPositiveButton("로그아웃") { dialog, id ->
                    logout()
                }.setNegativeButton("취소") { _, _ ->
                    dismiss()
                }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun logout() {
        SharedPreferences.clearForLogout()
        startActivity(Intent(activity, LoginActivity::class.java))
        if (activity?.isFinishing == false) activity?.finish()
    }
}