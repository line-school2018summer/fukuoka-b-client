package com.line.fukuokabclient.Dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import com.line.fukuokabclient.R

class ColorChangeDialogHelper(context: Context): BaseDialogHelper() {
    override val dialogView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.dialog_color_change, null)
    }
    override val builder: AlertDialog.Builder = AlertDialog.Builder(context).setView(dialogView)

    val radioGroup: RadioGroup by lazy {
        dialogView.findViewById<RadioGroup>(R.id.dialog_color_radio_group)
    }

    val rbDefault: RadioButton by lazy {
        dialogView.findViewById<RadioButton>(R.id.radio_default)
    }

    val rbSakura: RadioButton by lazy {
        dialogView.findViewById<RadioButton>(R.id.radio_sakura_fedfe1)
    }

    val rbYama: RadioButton by lazy {
        dialogView.findViewById<RadioButton>(R.id.radio_yamabukicya_d19826)
    }

    val rbNae: RadioButton by lazy {
        dialogView.findViewById<RadioButton>(R.id.radio_nae_86c166)
    }

    val rbAotake: RadioButton by lazy {
        dialogView.findViewById<RadioButton>(R.id.radio_aotake_00896c)
    }

    val rbSora: RadioButton by lazy {
        dialogView.findViewById<RadioButton>(R.id.radio_sora_58b2dc)
    }

    val rbGofun: RadioButton by lazy {
        dialogView.findViewById<RadioButton>(R.id.radio_gofunn_fffffb)
    }

    val btnCancel: Button by lazy {
        dialogView.findViewById<Button>(R.id.dialog_color_btn_cancel)
    }

    val btnOk: Button by lazy {
        dialogView.findViewById<Button>(R.id.dialog_color_btn_ok)
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