package com.juju.zhdd.usercomponet.login

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.juju.core.base.BaseToolBarViewModel
import com.juju.core.binding.command.BindingAction
import com.juju.core.binding.command.BindingCommand
import com.juju.core.binding.command.BindingConsumer
import com.juju.core.ext.showAtTop
import com.juju.core.ext.yes
import com.juju.core.utils.isPhone
import com.juju.zhdd.usercomponet.BuildConfig


class LogInViewModel(application: Application) : BaseToolBarViewModel(application) {
    val phoneNumber by lazy { ObservableField<String>() }

    val agreementTips by lazy { ObservableField<Boolean>(false) }

    //服务协议是否选中
    val mServiceCheckStatus by lazy { ObservableBoolean(false) }

    val wechatLogin by lazy { ObservableField<Boolean>(false) }

    val codeEnabled by lazy { ObservableField<Boolean>(false) }

    val code by lazy { ObservableField<String>("") }

    val codeMessage by lazy { ObservableField<String>("") }

    val inviteCode by lazy { ObservableField<String>() }

    override fun onCreate() {
        super.onCreate()
        toolBarData.titleText = "登录"
    }


    val sendCodeAction = BindingCommand<Nothing>(object : BindingAction {
        override fun call() {
            checkParams().yes {
                //发送验证码
            }
        }
    })

    val isMobileCheckAction = BindingCommand(object : BindingConsumer<Boolean> {
        override fun call(t: Boolean) {
            codeEnabled.set(t)
        }
    })


    //重新发送验证码
    val logInAction = BindingCommand<Nothing>(object : BindingAction {
        override fun call() {
            checkParams().yes {
                if (mServiceCheckStatus.get()) {
                    //登录方法
                } else {
                    agreementTips.set(!agreementTips.get()!!)
                }
            }
        }
    })


    val weChatLoginAction = BindingCommand<Nothing>(object : BindingAction {
        override fun call() {

            if (mServiceCheckStatus.get()) {
                wechatLogin.set(!wechatLogin.get()!!)
            } else {
                agreementTips.set(!agreementTips.get()!!)
            }


        }
    })


    private fun checkParams(): Boolean {

        if (phoneNumber.get() == null || phoneNumber.get()?.isEmpty() == true) {
            "请输入手机号码".showAtTop()
            return false
        }
        if (!(phoneNumber.get()?:"").isPhone()&&!BuildConfig.DEBUG) {
            "请输入正确的手机号码".showAtTop()
            return false
        }

        return true

    }


}