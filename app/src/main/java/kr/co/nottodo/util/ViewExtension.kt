package kr.co.nottodo.util

import android.content.Context
import android.view.View
import kr.co.nottodo.view.snackbar.NotTodoSnackbar

fun View.showNotTodoSnackBar(msg: String) {
    NotTodoSnackbar(this, msg).show()
}