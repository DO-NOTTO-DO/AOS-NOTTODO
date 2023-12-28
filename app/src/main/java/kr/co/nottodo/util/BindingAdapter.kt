package kr.co.nottodo.util

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("app:selected")
fun isSelected(view: View, isSelected: Boolean) {
    view.isSelected = isSelected
}