package com.juju.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.juju.core.ext.resourceHeader

/*
    Glide工具类
 */
object GlideUtils {
    @SuppressLint("CheckResult")
    private val commonRequestOptions =
        RequestOptions().placeholder(ColorDrawable(Color.GRAY)).error(ColorDrawable(Color.GRAY))
            .skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.RESOURCE)


    fun loadImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).load(url.resourceHeader()).apply(commonRequestOptions).into(imageView)
    }

    fun loadImage(
        context: Context, url: String, imageView: ImageView, customRequestOptions: RequestOptions
    ) {
        Glide.with(context).load(url.resourceHeader()).apply(customRequestOptions).into(imageView)
    }


    fun loadWithOutImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).load(url.resourceHeader()).into(imageView)
    }

    /*
        当fragment或者activity失去焦点或者destroyed的时候，Glide会自动停止加载相关资源，确保资源不会被浪费
     */
    fun loadUrlImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).load(url.resourceHeader()).apply(
            RequestOptions().placeholder(ColorDrawable(Color.GRAY)).error(ColorDrawable(Color.GRAY))
        ).into(imageView)

    }

    /*
       当fragment或者activity失去焦点或者destroyed的时候，Glide会自动停止加载相关资源，确保资源不会被浪费
    */
    fun loadUrlImage(context: Context, url: String, imageView: ImageView, placeholderRes: Int) {

        when {
            //net
            url.contains("attached") -> {
                Glide.with(imageView.context).load(url.resourceHeader())
                    .apply(RequestOptions().placeholder(placeholderRes)).into(imageView)
            }
            // else local
            url.contains("juju_zdd") -> {
                Glide.with(imageView.context).load(url)
                    .apply(RequestOptions().placeholder(placeholderRes)).into(imageView)
            }
            else -> {
                Glide.with(imageView.context).load(url)
                    .apply(RequestOptions().placeholder(placeholderRes)).into(imageView)
            }
        }
    }

    /*
       当fragment或者activity失去焦点或者destroyed的时候，Glide会自动停止加载相关资源，确保资源不会被浪费
    */
    fun loadUrlImage(
        context: Context, url: String, imageView: ImageView, placeholderRes: Drawable
    ) {
        Glide.with(imageView.context).load(url.resourceHeader())
            .apply(RequestOptions().placeholder(placeholderRes)).into(imageView)
    }


    /**
     * 加载圆形图片
     */
    fun loadCircleImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(imageView.context).load(url.resourceHeader())
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .apply(RequestOptions().placeholder(ColorDrawable(Color.GRAY))).into(imageView)
    }

    fun loadRoundImage(context: Context, url: String, imageView: ImageView, placeholderRes: Int) {
        // 圆角图片
        Glide.with(context).load(url.resourceHeader()).transform(RoundedCorners(8)) // 数字根据自己需求来改
            .into(imageView)
    }
}
