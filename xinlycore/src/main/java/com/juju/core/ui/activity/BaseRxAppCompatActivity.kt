package com.juju.core.ui.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.juju.core.helper.AppManager

/**
 * Activity基类
 * <p>
 * Created by xiangyao on 2019/3/27.
 */
abstract class BaseRxAppCompatActivity: RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 将Activity添加到AppManager
        AppManager.instance.addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 将Activity从AppManager中移除
        AppManager.instance.finishActivity(this)
    }

    //获取Window中视图content
    val contentView: View
        get() {
            val content = findViewById<FrameLayout>(android.R.id.content)
            return content.getChildAt(0)
        }
}