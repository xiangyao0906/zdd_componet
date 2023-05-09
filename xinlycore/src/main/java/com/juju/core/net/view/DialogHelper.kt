package com.juju.core.net.view

import android.content.Context

/**
 * Created by xiangyao on 2019-06-19.
 */
class DialogHelper private constructor() {

    private var dialogDismissListener: DialogUtil.DialogDismissListener? = null

    companion object {
        val instance: DialogHelper by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { DialogHelper() }
    }

    fun showLoadingDialog(context: Context) {
        dialogDismissListener?.onDismiss()
        dialogDismissListener = DialogUtil.createLoadingDialog(context, null, "")
    }

    fun dismissLoading() {
        dialogDismissListener?.onDismiss()
        dialogDismissListener = null
    }
}