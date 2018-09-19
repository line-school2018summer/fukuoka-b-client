package com.line.fukuokabclient.Dialog

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View

abstract class BaseDialogHelper {
    abstract val dialogView: View
    abstract val builder: AlertDialog.Builder

    open var cancelable: Boolean = true
    open var isBackgroundTransparent = true

    open var dialog: AlertDialog? = null

    open fun create(): AlertDialog {
        dialog = builder
                .setCancelable(cancelable)
                .create()

        if (isBackgroundTransparent) dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog!!
    }

    open fun onCancelListener(func: () -> Unit): AlertDialog.Builder? =
            builder.setOnCancelListener {
                func()
            }
}