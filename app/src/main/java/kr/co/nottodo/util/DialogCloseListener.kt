package kr.co.nottodo.util

import android.content.DialogInterface

interface DialogCloseListener {
    fun handleDialogClose(dialog: DialogInterface)
}