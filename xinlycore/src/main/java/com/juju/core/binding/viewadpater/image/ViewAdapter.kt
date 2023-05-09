package com.juju.core.binding.viewadpater.image

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.juju.core.ext.no
import com.juju.core.ext.resourceHeader
import com.juju.core.utils.GlideUtils
import com.xinly.core.R

/**
 * Created by xiangyao on 2019-06-30.
 */
object ViewAdapter {

    @BindingAdapter(value = ["url", "placeholderRes"], requireAll = false)
    @JvmStatic
    fun setImageUri(imageView: ImageView, url: String?, @ColorRes placeholderRes: Int) {
        GlideUtils.loadUrlImage(imageView.context,url?:"",imageView,placeholderRes)



    }


    @BindingAdapter(value = ["url", "placeholderRes"], requireAll = false)
    @JvmStatic
    fun setImageUri(imageView: ImageView, url: String?, placeholderRes: Drawable) {
        GlideUtils.loadUrlImage(imageView.context,url?:"",imageView,placeholderRes)
    }

    @BindingAdapter(value = ["headurl", "placeholderRes"], requireAll = false)
    @JvmStatic
    fun setHeadImageUri(imageView: ImageView, url: String?, placeholderRes: Int) {
        GlideUtils.loadUrlImage(imageView.context,url?:"",imageView,R.drawable.mine_default)
    }






    @BindingAdapter(value = ["src"], requireAll = true)
    @JvmStatic
    fun setImageRes(imageView: ImageView, src: Int) {
        imageView.setImageResource(src)
    }

    @BindingAdapter(value = ["bmp"], requireAll = true)
    @JvmStatic
    fun setImageBmp(imageView: ImageView, bitmap: Bitmap?) {
        bitmap?.let {
            imageView.setImageBitmap(bitmap)
        }
    }

    @BindingAdapter(value = ["circleImage", "placeholderRes"], requireAll = false)
    @JvmStatic
    fun setCircleImageUri(imageView: ImageView, url: String?, placeholderRes: Int) {
        url.isNullOrEmpty().no {
            //使用Glide框架加载图片
            Glide.with(imageView.context)
                .load(url?.resourceHeader())
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .apply(RequestOptions().placeholder(placeholderRes))
                .into(imageView)
        }
    }
}