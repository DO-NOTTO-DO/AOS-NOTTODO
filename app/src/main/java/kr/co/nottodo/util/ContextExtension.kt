package kr.co.nottodo.util

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import kr.co.nottodo.view.snackbar.NotTodoSnackbar

/** convert dp to px */
fun Context.dpToPx(dp: Int): Int {
    return (TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), this.resources.displayMetrics
    )).toInt()
}

/** hide keyboard from window */
fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

/** show keyboard from window */
fun Context.showKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, 0)
}

/** Make a Snackbar to display a message for 2 seconds */
fun Context.showSnackBar(view: View, msg: String) {
    Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
}

fun Context.showNotTodoSnackBar(view: View, msg: String) {
    NotTodoSnackbar(view, msg).show()
}

/** Make a Toast to display a message for 2 seconds */
fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}