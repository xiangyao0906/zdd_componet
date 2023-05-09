package com.juju.core.ui.fragment

/**
 * Created by xiangyao on 2019/3/28.
 */
interface IBaseFragment {
    /**
     * 初始化界面传递参数
     */
    fun initParam()

    /**
     * 初始化数据
     */
    fun initData()
    /**
     * 初始化界面观察者监听
     */
    fun initViewObservable()
}