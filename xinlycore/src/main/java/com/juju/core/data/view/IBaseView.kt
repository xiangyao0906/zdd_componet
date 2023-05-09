package com.juju.core.data.view

/**
 * Created by xiangyao on 2019/3/24.
 */
interface IBaseView {
    fun showLoading()
    fun hideLoading()
    fun onError(text:String)
}