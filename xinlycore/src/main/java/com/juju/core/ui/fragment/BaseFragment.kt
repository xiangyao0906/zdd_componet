package com.juju.core.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.juju.core.viewmodel.BaseViewModel
import com.juju.core.viewmodel.IBaseViewModel
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.trello.rxlifecycle3.components.support.RxFragment
import com.xinly.core.R
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 一个拥有DataBinding框架的基Fragment
 * <p>
 * 这里根据项目业务可以换成你自己熟悉的BaseFragment, 但是需要继承RxFragment,方便LifecycleProvider管理生命周期
 * <p>
 * Created by xiangyao on 2019/3/28.
 */
abstract class BaseFragment<V : ViewDataBinding, VM : BaseViewModel> : RxFragment(), IBaseFragment {
    protected var viewModel: VM? = null

    protected lateinit var binding: V
    private var viewModelId: Int = 0

    private var mRootView: View? = null
    protected var mViewCreated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //页面参数方法
        initParam()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包

        val contentViewId = initContentView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(
            inflater,
            contentViewId,
            container,
            false
        )
        viewModelId = initVariableId()
        viewModel = initViewModel() ?: createViewModel()
        //关联ViewModel
        binding.setVariable(viewModelId, viewModel)
        viewModel?.let {
            //注入context
            it.injectContext(this.requireContext())
            //让ViewModel拥有View的生命周期感应
            lifecycle.addObserver(it)
            //注入RxLifecycle生命周期
            it.injectLifecycleProvider(this)

        }

        if (mRootView == null) {
            if (contentViewId > 0) {
                mRootView = binding.root
            }
        }
        mViewCreated = true


        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //私有的ViewModel与View的契约事件回调逻辑
        registerUIChangeLiveDataCallBack()
        //初始化数据
        initData()
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable()
        //注册RxBus
        viewModel?.registerRxBus()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewModel?.let {
            //解除ViewModel生命周期感应
            lifecycle.removeObserver(it)
            //移除RxBus监听
            it.removeRxBus()
        }
        mRootView = null
        mViewCreated = false
        //取消DataBinding绑定
        binding.unbind()

    }

    /**
     * 创建ViewModel实例
     * @return VM
     */
    private fun createViewModel(): VM {
        sequence<Type> {
            val thisClass: Class<*> = this@BaseFragment.javaClass
            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            yield(thisClass.genericSuperclass!!)
        }.filter {
            it is ParameterizedType
        }.flatMap {
            (it as ParameterizedType).actualTypeArguments.asSequence()
        }.first {
            it is Class<*> && IBaseViewModel::class.java.isAssignableFrom(it)
        }.let {
            @Suppress("UNCHECKED_CAST")
            return createViewModel(this, it as Class<VM>)
        }
    }

    /**
     * =====================================================================
     **/
    //注册ViewModel与View的契约UI回调事件
    private fun registerUIChangeLiveDataCallBack() {
        viewModel?.uc?.apply {
            //跳转页面
            startActivityEvent.observe(this@BaseFragment, Observer { params->


                when{
                    params[BaseViewModel.ParameterField.CLASS]!=null->{
                        val clz: Class<*> = params[BaseViewModel.ParameterField.CLASS] as Class<*>
                        val bundle: Bundle? = params[BaseViewModel.ParameterField.BUNDLE] as? Bundle
                        startActivity(clz, bundle)

                    }
                }




            })
            //关闭界面
            finishEvent.observe(this@BaseFragment, Observer {
                activity?.finish()
            })
            //关闭上一层
            onBackPressedEvent.observe(this@BaseFragment, Observer {
                activity?.onBackPressed()
            })

            onFinishRefreshEvent.observe(this@BaseFragment){



            }
        }
    }

    /**
     * 跳转页面
     * @param clz 所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    fun startActivity(clz: Class<*>, bundle: Bundle?=null) {
        val intent = Intent(activity, clz)
        bundle?.let {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    /**
     * =====================================================================
     **/
    override fun initParam() {
    }

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    abstract fun initContentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): Int

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    abstract fun initVariableId(): Int

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    open fun initViewModel(): VM? {
        return null
    }

    override fun initData() {
    }

    override fun initViewObservable() {
    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return T
     */
    open fun <T : ViewModel> createViewModel(fragment: Fragment, cls: Class<T>): T {
        return ViewModelProvider(fragment)[cls]
    }
}
