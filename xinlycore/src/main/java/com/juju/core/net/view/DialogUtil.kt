package com.juju.core.net.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.juju.core.ext.yes
import com.juju.core.utils.ScreenUtils
import com.xinly.core.R

/**
 * Created by xiangyao on 2019-06-19.
 */
class DialogUtil private constructor(){

    companion object{
        fun createLoadingDialog(context: Context, dialogCancelListener: DialogCancelListener?, tips: String): DialogDismissListener? {
            if (context is Activity && context.isFinishing) {
                return null
            }
            val customLoadingDialog = CustomLoadingDialog(context, tips)
            customLoadingDialog.apply {
                this.dialogCancelListener = dialogCancelListener
                setCanceledOnTouchOutside(false)
                setCancelable(true)
            }.show()
            return object : DialogDismissListener {
                override fun onDismiss() {
                    (context is Activity && !context.isFinishing).yes {
                        customLoadingDialog.dismiss()
                    }
                }
            }
        }
    }

    class CustomLoadingDialog(context: Context, val tips: String): AlertDialog(context, R.style.TransparentDialogStyle) {

        var dialogCancelListener: DialogCancelListener? = null
        private var animationDrawable: AnimationDrawable? = null

        @SuppressLint("InflateParams")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.dialog_loading_custom_layout, null)
            if (tips.isNotEmpty()) {
                val tvTips = view.findViewById<TextView>(R.id.dialog_loading_custom_tv_tips)
                tvTips.visibility = View.VISIBLE
                tvTips.text = tips
            }

            val dialogWindow = window
//            window?.requestFeature(1)
            dialogWindow?.setContentView(view)
            val p: WindowManager.LayoutParams = window?.attributes!!
            p.width = (ScreenUtils.screenSize(context)[0] * 0.3).toInt()
            p.height = p.width
            window?.attributes = p

            this.setOnCancelListener {
                animationDrawable?.stop()
                dialogCancelListener?.onCancel()
            }
        }

        override fun onWindowFocusChanged(hasFocus: Boolean) {
            super.onWindowFocusChanged(hasFocus)
            animationDrawable?.start()
        }
    }

    interface DialogDismissListener {
        fun onDismiss()
    }

    interface DialogCancelListener {
        fun onCancel()
    }
}