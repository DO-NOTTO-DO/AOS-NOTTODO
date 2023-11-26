package kr.co.nottodo.view.snackbar

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kr.co.nottodo.databinding.ViewNottodoSnackbarBinding

class NotTodoSnackbar(view: View, private val message: CharSequence) {
    private val context = view.context
    private val snackbar = Snackbar.make(view, message, 2000)
    private val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
    private val inflater = LayoutInflater.from(context)
    private val binding: ViewNottodoSnackbarBinding =
        ViewNottodoSnackbarBinding.inflate(inflater, null, false)

    init {
        initView()
        initData()
        initClickEvent()
    }

    private fun initView() {
        snackbarLayout.run {
            removeAllViews()
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(binding.root, 0)
        }
    }

    private fun initData() {
        binding.tvNottodoSnackbarDesc.text = message
    }

    private fun initClickEvent() {
        binding.root.setOnClickListener {
            snackbar.dismiss()
        }
    }

    fun show() {
        snackbar.show()
    }
}