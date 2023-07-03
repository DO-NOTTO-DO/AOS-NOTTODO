package kr.co.nottodo.presentation.home.view

import android.content.DialogInterface

interface DialogCloseListener {
//    fun handleDialogClose(dialog: DialogInterface)
    fun onDismissAndDataPass(selectFirstDay: String?)
}