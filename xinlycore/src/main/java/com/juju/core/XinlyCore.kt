package com.juju.core

import android.annotation.SuppressLint
import android.content.Context


/**
 * jujuCore初始化
 * <p>
 * Created by xiangyao on 2019-05-10.
 */
class XinlyCore {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context private set

        /**
         * 初始化
         * @param context applicationContext
         */
        fun init(context: Context) {
            this.context = context.applicationContext
        }
    }
}