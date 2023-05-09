package com.juju.core.ui.activity

/**
 * Created by xiangyao on 2019/3/27.
 */
interface IBaseActivity {
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

    /**
     * 阿里Arouter(非必需)
     * */
    fun injectRouter()
}