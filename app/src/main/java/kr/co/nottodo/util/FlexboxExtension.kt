package kr.co.nottodo.util

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.android.flexbox.FlexboxLayout
import kr.co.nottodo.R
import kr.co.nottodo.util.NotTodoAmplitude.trackEventWithProperty

fun FlexboxLayout.addButtons(texts: List<String>, editText: EditText) {
    for (element in texts) {
        val button = LayoutInflater.from(context)
            .inflate(R.layout.view_addition_situation_list_button, null) as Button
        button.text = element
        button.setOnClickListener {
            editText.setText(element)
            editText.requestFocus()
            editText.setSelection(editText.length())
            it.context.showKeyboard(editText)
            trackEventWithProperty(
                context.getString(R.string.click_recommend_situation),
                context.getString(R.string.situation),
                element
            )
        }

        val layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT
        )
        layoutParams.marginEnd = context.dpToPx(5)
        layoutParams.bottomMargin = context.dpToPx(8)

        addView(button, layoutParams)
    }
}