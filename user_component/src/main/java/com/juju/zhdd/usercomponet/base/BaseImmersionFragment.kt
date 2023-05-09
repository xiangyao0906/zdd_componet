package com.juju.zhdd.usercomponet.base

import android.content.res.Configuration
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.databinding.ViewDataBinding
import com.gyf.immersionbar.components.SimpleImmersionOwner
import com.juju.core.ui.fragment.BaseFragment
import com.juju.core.viewmodel.BaseViewModel
import com.gyf.immersionbar.components.SimpleImmersionProxy



/**
 *
 * <p>
 * Created by xiangyao on 2021/11/23.
 */
abstract class BaseImmersionFragment<V : ViewDataBinding, VM : BaseViewModel>:BaseFragment<V,VM>(),SimpleImmersionOwner  {
    /**
     * ImmersionBar代理类
     */
    private val mSimpleImmersionProxy = SimpleImmersionProxy(this)

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mSimpleImmersionProxy.isUserVisibleHint = isVisibleToUser
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mSimpleImmersionProxy.onActivityCreated(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mSimpleImmersionProxy.onDestroy()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mSimpleImmersionProxy.onHiddenChanged(hidden)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mSimpleImmersionProxy.onConfigurationChanged(newConfig)
    }

    /**
     * 是否可以实现沉浸式，当为true的时候才可以执行initImmersionBar方法
     * Immersion bar enabled boolean.
     *
     * @return the boolean
     */
    override fun immersionBarEnabled(): Boolean {
        return true
    }
}