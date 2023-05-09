package com.juju.zhdd.usercomponet.base

import android.os.Bundle
import android.util.Log
import androidx.databinding.ViewDataBinding
import com.gyf.immersionbar.ImmersionBar
import com.juju.core.ui.activity.BaseActivity
import com.juju.core.viewmodel.BaseViewModel
import com.juju.zhdd.usercomponet.R

/**
 * Created by xiangyao on 2019-07-16.
 */
abstract class BaseMVVMActivity<V : ViewDataBinding, VM : BaseViewModel> : BaseActivity<V, VM>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBar()
        Log.e("activity=>", javaClass.simpleName)

    }


    open fun setStatusBar() {
        ImmersionBar.with(this)
            .statusBarColor(R.color.white)
            .transparentNavigationBar()
            .fitsSystemWindows(true)
            .navigationBarEnable(!ImmersionBar.hasNavigationBar(this))
            .statusBarDarkFont(
                true,
                0.2f
            ) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
            .init()
    }

}