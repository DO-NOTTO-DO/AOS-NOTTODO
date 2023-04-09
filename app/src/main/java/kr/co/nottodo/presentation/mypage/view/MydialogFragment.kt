package kr.co.nottodo.presentation.mypage.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class MyDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Dialog Title")
                .setMessage("Dialog Message")
                .setPositiveButton("취소") { dialog, id ->
                    // OK 버튼 클릭 시 수행할 동작
                }
                .setNegativeButton("로그아웃") { dialog, id ->
                    // Cancel 버튼 클릭 시 수행할 동작
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}
