package com.juju.core.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager


object ScreenUtils {
    @JvmStatic
    fun dp2px(paramFloat: Float): Float {
        return TypedValue.applyDimension(1, paramFloat, Resources.getSystem().displayMetrics)
    }

    @JvmStatic
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun screenSize(context: Context):IntArray{

        val manager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        manager.defaultDisplay.getMetrics(outMetrics)

        return intArrayOf(outMetrics.widthPixels,outMetrics.heightPixels)
    }
    @JvmStatic
    fun isPortrait(context: Context):Boolean{
        return context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    }
    @JvmStatic
    fun isLandscape(context: Context):Boolean{
        return context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    }
}