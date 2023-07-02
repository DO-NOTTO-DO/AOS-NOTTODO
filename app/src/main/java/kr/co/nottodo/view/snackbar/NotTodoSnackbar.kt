package kr.co.nottodo.view.snackbar

import android.text.Spanned
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
    }

    private fun initView() {
        with(snackbarLayout) {
            removeAllViews()
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(binding.root, 0)
        }
    }

    private fun initData() {
        when (message) {
            is Spanned -> {
                binding.tvNottodoSnackbarDesc.text = message
            }

            else -> {
                binding.tvNottodoSnackbarDesc.text = message
            }
        }
    }

    fun show() {
        snackbar.show()
    }
}