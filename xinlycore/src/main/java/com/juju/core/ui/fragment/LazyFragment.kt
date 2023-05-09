package com.juju.core.ui.fragment

import android.content.res.Configuration
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.gyf.immersionbar.components.SimpleImmersionOwner
import com.gyf.immersionbar.components.SimpleImmersionProxy
import com.juju.core.viewmodel.BaseViewModel

abstract class LazyFragment<V : ViewDataBinding, VM : BaseViewModel>  : BaseFragment<V,VM>(),
    SimpleImmersionOwner {
    /**
     * ImmersionBar代理类
     */
    private val mSimpleImmersionProxy = SimpleImmersionProxy(this)


    private var mIsFirstVisible = true
    private var isSupportUserVisible = false
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mSimpleImmersionProxy.isUserVisibleHint = isVisibleToUser
        if (mViewCreated) {
            if (isVisibleToUser && !isSupportUserVisible) {
                dispatchUserVisibleHint(true)
            } else if (!isVisibleToUser && isSupportUserVisible) {
                dispatchUserVisibleHint(false)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mSimpleImmersionProxy.onActivityCreated(savedInstanceState)
        if (!isHidden && userVisibleHint) {
            dispatchUserVisibleHint(true)

        }

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mSimpleImmersionProxy.onHiddenChanged(hidden)
        if (hidden) {
            dispatchUserVisibleHint(false)
        } else {
            dispatchUserVisibleHint(true)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!mIsFirstVisible) {
            if (!isHidden && !isSupportUserVisible && userVisibleHint) {
                dispatchUserVisibleHint(true)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (isSupportUserVisible && userVisibleHint) {
            dispatchUserVisibleHint(false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mIsFirstVisible = true
    }

    private fun dispatchUserVisibleHint(visible: Boolean) {
        if (visible && !isParentVisible) {
            return
        }
        if (isSupportUserVisible == visible) {
            return
        }
        isSupportUserVisible = visible
        if (visible) {
            if (mIsFirstVisible) {
                mIsFirstVisible = false
                onVisible(true)
            } else {
                onVisible(false)
            }
            dispatchChildVisibleState(true)
        } else {
            dispatchChildVisibleState(false)
            onInvisible()
        }
    }

    private val isParentVisible: Boolean
        private get() {
            val fragment = parentFragment ?: return true
            if (fragment is LazyFragment<*, *>) {
                return fragment.isSupportUserVisible
            }
            return fragment.isVisible
        }

    private fun dispatchChildVisibleState(visible: Boolean) {
        val childFragmentManager = childFragmentManager
        val fragments = childFragmentManager.fragments
        if (fragments.isNotEmpty()) {
            for (child in fragments) {
                if (child is LazyFragment<*, *> && !child.isHidden() && child.getUserVisibleHint()) {
                    child.dispatchUserVisibleHint(visible)
                }
            }
        }
    }

     open fun onVisible(isFirstVisible: Boolean) {
     }
     open  fun onInvisible() {}

    override fun onDestroy() {
        super.onDestroy()
        mSimpleImmersionProxy.onDestroy()
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