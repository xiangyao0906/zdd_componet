package com.juju.core.binding.viewadpater.view

import android.annotation.SuppressLint
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.jakewharton.rxbinding2.view.RxView
import com.juju.core.binding.command.BindingCommand
import com.juju.core.ext.onLazyClick
import com.juju.core.utils.KeyboardUtil
import com.juju.core.utils.isPhone
import com.juju.core.utils.onTextChange
import com.juju.core.widgets.edittext.OnInputListener
import com.juju.core.widgets.edittext.SplitEditTextView

/**
 * Created by xiangyao on 2019/1/5.
 */
object ViewAdapter {
    // 防重复点击间隔(秒)
    private const val CLICK_INTERVAL: Long = 1

    /**
     * requireAll 是意思是是否需要绑定全部参数, false为否
     * View的onClick事件绑定
     * onClickCommand 绑定的命令,
     * isThrottleFirst 是否开启防止过快点击
     */
    @SuppressLint("CheckResult")
    @BindingAdapter(value = ["onClickCommand", "isThrottleFirst"], requireAll = false)
    @JvmStatic
    fun onClickCommand(
        view: View,
        clickCommand: BindingCommand<Nothing>,
        isThrottleFirst: Boolean
    ) {
        view.onLazyClick {
            clickCommand.execute()
        }
    }

    /**
     * view的onLongClick事件绑定
     */
    @SuppressLint("CheckResult")
    @JvmStatic
    @BindingAdapter(value = ["onLongClickCommand"], requireAll = false)
    fun onLongClickCommand(view: View, clickCommand: BindingCommand<Nothing>) {
        RxView.longClicks(view)
            .subscribe {
                clickCommand.execute()
            }
    }

    /**
     * 手机号码的验证
     * */
    @JvmStatic
    @BindingAdapter(value = ["checkMobile"], requireAll = false)
    fun isValuedMobile(editText: EditText, isMobile: BindingCommand<Boolean>) {

        editText.onTextChange {
            afterTextChanged {
                isMobile.execute(it.toString().trim().isPhone())
            }
        }
    }

    /**
     * 手机号码的验证
     * */
    @JvmStatic
    @BindingAdapter(value = ["inputCode"], requireAll = false)
    fun onCodeFinish(editText: SplitEditTextView, inputCode: BindingCommand<String>) {

        editText.setOnInputListener(object : OnInputListener() {
            override fun onInputFinished(content: String?) {
                inputCode.execute(content ?: "")
            }
        })

    }

    /**
     * 软键盘搜索键
     * */
    @JvmStatic
    @BindingAdapter(value = ["searchAction"], requireAll = false)
    fun searchAction(editText: EditText, search: BindingCommand<String>) {
        editText.setOnEditorActionListener { v, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                search.execute(v.text.toString().trim())

                KeyboardUtil.closeSoftKeyboard(editText)

                return@setOnEditorActionListener true


            }
            return@setOnEditorActionListener false
        }


    }
}