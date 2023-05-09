package com.juju.core.base

import android.app.Application
import com.juju.core.binding.command.BindingAction
import com.juju.core.binding.command.BindingCommand
import com.juju.core.data.ToolBarData
import com.juju.core.viewmodel.BaseViewModel

/**
 * Created by xiangyao on 2019-06-30.
 */
abstract class BaseToolBarViewModel(application: Application) : BaseViewModel(application) {
    /**
     * TooBar数据
     */
    val toolBarData: ToolBarData = ToolBarData()
    /**
     * toolbar back event
     */
    val backClick: BindingCommand<Nothing> = BindingCommand(object : BindingAction {
        override fun call() {
            handLeftClick()
        }

    })

    /**
     * toolbar back event
     */
    val leftTextClick: BindingCommand<Nothing> = BindingCommand(object : BindingAction {
        override fun call() {
            handLeftTextClick()
        }

    })

    open fun handLeftClick() {
        finish()
    }
    open fun handLeftTextClick() {
        finish()
    }
    /**
     * toolbar text event
     */
    val rightTextClick: BindingCommand<Nothing> = BindingCommand(object : BindingAction {
        override fun call() {
            handRightText()
        }

    })
    open fun handRightText() {


    }

    /**
     * toolbar right img event
     */
    val rightImgClick: BindingCommand<Nothing> = BindingCommand(object : BindingAction {
        override fun call() {
            handRightImg()
        }

    })
    open fun handRightImg() {

    }

    /**
     * toolbar sub img event
     */
    val subImgClick: BindingCommand<Nothing> = BindingCommand(object : BindingAction {
        override fun call() {
            handSubImg()
        }

    })
    open fun handSubImg() {

    }
}