package com.line.fukuokabclient.Dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.line.fukuokabclient.R

class ChannelNameDialogHelper (context: Context): BaseDialogHelper() {
    override val dialogView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.dialog_edit_channel_name, null)
    }
    override val builder: AlertDialog.Builder = AlertDialog.Builder(context).setView(dialogView)

    val edtName: EditText by lazy {
        dialogView.findViewById<EditText>(R.id.dialog_edit_channel_name_text)
    }

    val btnCancel: Button by lazy {
        dialogView.findViewById<Button>(R.id.dialog_edit_channel_name_btn_cancel)
    }

    val btnOk: Button by lazy {
        dialogView.findViewById<Button>(R.id.dialog_edit_channel_name_btn_ok)
    }

    fun btnCancelOnClick(func: (() -> Unit)? = null) =
            with(btnCancel) {
                setOnClickListener {
                    func?.invoke()
                    dialog?.dismiss()
                }
            }

    fun btnOkOnClick(func: (() -> Unit)? = null) =
            with(btnOk) {
                setOnClickListener {
                    func?.invoke()
                    dialog?.dismiss()
                }
            }
}