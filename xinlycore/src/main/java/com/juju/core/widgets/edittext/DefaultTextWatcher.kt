package com.juju.core.widgets.edittext

import android.text.Editable
import android.text.TextWatcher

/**
 * 默认TextWatcher，空实现
 * <p>
 * Created by xiangyao on 2019/3/24.
 */
open class DefaultTextWatcher: TextWatcher {
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}