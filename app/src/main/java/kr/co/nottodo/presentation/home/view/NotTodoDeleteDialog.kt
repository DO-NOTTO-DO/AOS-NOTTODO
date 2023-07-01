package com.recodream_aos.recordream.util.customview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import androidx.annotation.LayoutRes
import com.recodream_aos.recordream.databinding.DocumentDeleteDialogBinding
import kr.co.nottodo.databinding.FragmentNotTodoDeleteDialogBinding

class NotTodoDeleteDialog(private val context: Context) {
    private lateinit var deleteBinding: FragmentNotTodoDeleteDialogBinding
    private val dialog = Dialog(context)
    private lateinit var inflater: LayoutInflater
    private lateinit var onClickedListener: ButtonClickListener

    init {
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(context)
        }
    }

    fun interface ButtonClickListener {
        fun onClicked()
    }

    fun setOnClickedListener(listener: ButtonClickListener) {
        onClickedListener = listener
    }

    fun showDeleteDialog(@LayoutRes layout: Int) {
        deleteBinding = DocumentDeleteDialogBinding.inflate(inflater)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(deleteBinding.root)
            setCancelable(false)
        }
        deleteBinding.tvDocumentCancel.setOnClickListener {
            dialog.dismiss()
        }
        deleteBinding.tvDocumentDelete.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


}