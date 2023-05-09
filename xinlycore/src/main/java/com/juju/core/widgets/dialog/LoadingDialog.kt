package com.juju.core.widgets.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.juju.core.utils.ScreenUtils
import com.xinly.core.R

/**
 *
 * <p>
 *
 * Created by xiangyao on 2020/5/14.
 */
class LoadingDialog {


    private var dialog: Dialog? = null
    private var rootView: View? = null

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun showDialog(
        context: Context,
        content: String = context.resources.getString(R.string.dialog_loading_text_0),
        cancelOutSize: Boolean = true
    ): LoadingDialog {
        if (dialog == null) {
            dialog = Dialog(context, R.style.BottomViewTheme_Default)
            //加载布局
            rootView = View.inflate(context, R.layout.dialog_loading_layout, null)
            //获取dialog实例
            dialog?.apply {
                window?.requestFeature(1)
                setContentView(rootView!!)
                val contentTv = rootView?.findViewById<TextView>(R.id.dialog_loading_custom_tv_tips)
                contentTv?.text = content
                val p: WindowManager.LayoutParams = window!!.attributes
                p.width = (ScreenUtils.screenSize(context)[0] * 0.3).toInt()
                p.height = p.width
                window?.attributes = p
                setCanceledOnTouchOutside(cancelOutSize)
            }
        }
        //显示
        dialog?.show()
        return this
    }

    fun cancel() {
        if (dialog != null) {
            dialog?.cancel()
        }
    }


}