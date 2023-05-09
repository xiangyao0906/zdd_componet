package com.juju.core.utils

import android.text.TextWatcher
import android.widget.EditText

/**
 *
 * <p>扩展类
 * Created by xiangyao on 2021/6/17.
 */

/**
 * 手机号验证
 * */
fun String.isPhone(): Boolean {
    return CoreRexUtils.checkPhone(this)
}

/**
 * 纯中文验证
 * */
fun String.isAllChinese(): Boolean {
    return CoreRexUtils.isAllChinese(this)
}

/**
 * 手机号验证
 * */
fun String.isIdCard(): Boolean {
    return CoreRexUtils.checkIdCard(this)
}

/**
 * 邮箱验证
 * */
fun String.isEmail(): Boolean {
    return CoreRexUtils.checkEmail(this)
}

inline fun EditText.onTextChange(textWatcher: TextWatcherDsl.() -> Unit): TextWatcher {
    val watcher = TextWatcherDsl().apply(textWatcher)
    addTextChangedListener(watcher)
    return watcher
}