package com.juju.core.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.juju.core.viewmodel.BaseViewModel
import com.juju.core.viewmodel.BaseViewModel.ParameterField
import com.juju.core.viewmodel.IBaseViewModel
import com.juju.core.widgets.dialog.LoadingDialog
import java.lang.reflect.ParameterizedType

/**
 * 一个拥有DataBinding框架的基Activity
 * <p>
 * 这里根据项目业务可以换成你自己熟悉的BaseActivity, 但是需要继承RxAppCompatActivity,方便LifecycleProvider管理生命周期
 * <p>
 * Created by xiangyao on 2019/3/27.
 */
abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel> : BaseRxAppCompatActivity(),
    IBaseActivity {
    protected var viewModel: VM? = null
    protected lateinit var binding: V
    private var viewModelId: Int = 0
    private var loadingDialog: LoadingDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = LoadingDialog()

        injectRouter()
        //页面参数方法
        initParam()
        //私有的初始化dataBinding和ViewModel方法
        initViewDataBinding(savedInstanceState)
        //私有的ViewModel与View的契约事件回调逻辑
        registerUIChangeLiveDataCallBack()
        //初始化数据
        initData()
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable()

    }

    override fun injectRouter() {}

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.let {
            //解除ViewModel生命周期感应
            lifecycle.removeObserver(it)
            //移除RxBus监听
            it.removeRxBus()
        }
        //取消DataBinding绑定
        binding.unbind()
    }

    private fun initViewDataBinding(savedInstanceState: Bundle?) {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        binding = DataBindingUtil.setContentView(this, initContentViewId(savedInstanceState))
        viewModelId = initVariableId()
        viewModel = initViewModel() ?: createViewModel()
        //关联ViewModel
        binding.setVariable(viewModelId, viewModel)
        viewModel?.let {
            //让ViewModel拥有View的生命周期感应
            lifecycle.addObserver(it)
            //注入RxLifecycle生命周期
            it.injectLifecycleProvider(this)
            //注入context
            it.injectContext(this)
            //注册RxBus
            it.registerRxBus()
        }
    }

    /**
     * 创建ViewModel实例
     * @return VM
     */
    private fun createViewModel(): VM {
        sequence {
            val thisClass: Class<*> = this@BaseActivity.javaClass
            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            yield(thisClass.genericSuperclass!!)
        }.filter {
            it is ParameterizedType
        }.flatMap {
            (it as ParameterizedType).actualTypeArguments.asSequence()
        }.last {
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
            startActivityEvent.observe(this@BaseActivity) { params ->

                when {
                    params[ParameterField.CLASS] != null -> {
                        val clz: Class<*> = params[ParameterField.CLASS] as Class<*>
                        val bundle: Bundle? = params[ParameterField.BUNDLE] as? Bundle
                        startActivity(clz, bundle)
                    }

                }


            }
            //关闭界面
            finishEvent.observe(this@BaseActivity) {
                finish()
            }
            //关闭上一层
            onBackPressedEvent.observe(this@BaseActivity) {
                onBackPressed()
            }
        }
    }

    /**
     * 跳转页面
     * @param clz 所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    fun startActivity(clz: Class<*>, bundle: Bundle? = null) {
        val intent = Intent(this, clz)
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
    abstract fun initContentViewId(savedInstanceState: Bundle?): Int

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
    open fun <T : ViewModel> createViewModel(fragment: FragmentActivity, cls: Class<T>): T {
        return ViewModelProvider(fragment)[cls]
    }
//
//    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
//        if (event.action == MotionEvent.ACTION_DOWN) {
//            val v: View? = currentFocus
//            if (v is EditText) {
//                val outRect = Rect()
//                v.getGlobalVisibleRect(outRect)
//                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
//                    v.setFocusable(false)
//                    v.setFocusableInTouchMode(true)
//                    val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
//                }
//            }
//        }
//        return super.dispatchTouchEvent(event)
//    }
//}

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        //don't click on edit text then hide keyboard and hide cursor
        if (ev.action == MotionEvent.ACTION_UP) {
            val currentFocus = currentFocus
            if (currentFocus != null) {
                val pressed = currentFocus.isPressed
                //don't click on edit text
                if (currentFocus is EditText && !pressed) {
                    currentFocus.setFocusable(false)
                    currentFocus.setFocusableInTouchMode(true)
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)

    }

    fun showLoadingDialog(loadingText: String, cancelOutSize: Boolean = true) {
        createLoadingDialog()
        loadingDialog?.showDialog(this, loadingText, cancelOutSize)
    }


    fun showLoadingDialog() {
        createLoadingDialog()
        loadingDialog?.showDialog(this)
    }

    fun hideLoadingDialog() {
        loadingDialog?.cancel()
    }


    private fun createLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog()
        }
    }
}
