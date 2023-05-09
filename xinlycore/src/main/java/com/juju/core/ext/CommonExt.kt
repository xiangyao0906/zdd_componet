package com.juju.core.ext

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.dovar.dtoast.DToast
import com.jakewharton.rxbinding2.view.RxView
import com.juju.core.XinlyCore
import com.juju.core.data.protocol.BaseResp
import com.juju.core.rx.BaseFunc
import com.juju.core.rx.BaseFunc1
import com.juju.core.rx.BaseFuncBoolean
import com.juju.core.rx.BaseSubscriber
import com.juju.core.utils.GlideUtils
import com.juju.core.widgets.edittext.DefaultTextWatcher
import com.trello.rxlifecycle3.LifecycleProvider
import com.xinly.core.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileInputStream
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

//Kotlin通用扩展
//Created by xiangyao on 2019/3/24.

/**
 * 扩展Observable执行
 */
fun <T : Any> Observable<T>.execute(
    subscriber: BaseSubscriber<T>, lifecycleProvider: LifecycleProvider<*>
) {
    this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .compose(lifecycleProvider.bindToLifecycle()).subscribe(subscriber)
}

/**
 * 扩展Observable执行
 */
fun <T : Any> Observable<T>.execute(
    subscriber: BaseSubscriber<T>
) {
    this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber)
}

/**
 * 扩展数据转换
 */
fun <T> Observable<BaseResp<T>>.convert(): Observable<T> {
    return this.flatMap(BaseFunc())
}

//
fun Disposable.addTo(){

    val compositeDisposable = CompositeDisposable()
    compositeDisposable.add(this)
    compositeDisposable.dispose()
    compositeDisposable.clear()
}

/**
 * 扩展数据转换
 * 没有code message
 */
fun <T> Observable<T>.withOutCode(): Observable<T> {
    return this
}

/**
 * 扩展数据处理
 */
fun <T> Observable<BaseResp<T>>.handleResult(): Observable<BaseResp<T>> {
    return this.flatMap(BaseFunc1())
}

/**
 * 扩展Boolean类型数据转换
 */
fun <T> Observable<BaseResp<T>>.convertBoolean(): Observable<Boolean> {
    return this.flatMap(BaseFuncBoolean())
}

/**
 * 扩展点击事件
 */
fun View.onClick(listener: View.OnClickListener): View {
    setOnClickListener(listener)
    return this
}

/**
 * 扩展点击事件，参数为方法
 */
fun View.onClick(method: () -> Unit): View {
    setOnClickListener {
        method()
    }
    return this
}


/**
 * 扩展点击事件，参数为方法
 */
@SuppressLint("CheckResult")
fun View.onLazyClick(method: () -> Unit): View {
    RxView.clicks(this).throttleFirst(1, TimeUnit.SECONDS).subscribe {
        method()
    }
    return this
}

/**
 * 扩展Button可用性
 */
fun Button.enable(et: EditText, method: () -> Boolean) {
    val btn = this
    et.addTextChangedListener(object : DefaultTextWatcher() {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            btn.isEnabled = method()
        }
    })
}

/**
 * 扩展View的显示与隐藏
 */
fun View.setVisible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

/**
 * ImageView加载网络图片
 */
fun ImageView.loadUrl(url: String) {
    GlideUtils.loadImage(context, url, this)
}

/**
 * 扩展String的copy到系统剪贴板功能
 */
fun String.copy(): Boolean {
    val link = this
    val clipData = ClipData.newPlainText("text", link)
    // 复制
    val cm = XinlyCore.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    // 将文本内容放到系统剪贴板里。
    cm.setPrimaryClip(clipData)
    return true
}

/**
 * 扩展String的toastShow方法
 * 默认显示
 */
fun String.show() {
    val toast = DToast.make(XinlyCore.context)
    val tvText = toast.view.findViewById<TextView>(R.id.tv_content_default)
    tvText?.text = this
    toast.setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, 70).show()
}


fun String.encodeToString(): String {

    try {
        return Base64.encodeToString(this.toByteArray(Charset.forName("UTF-8")), Base64.URL_SAFE)
    } catch (e: UnsupportedEncodingException) {
        e.printStackTrace()
    }
    return ""


}

fun String.urlEncoded(): String {
    if (this == "") {
        return ""
    }
    try {
        var str = String(this.toByteArray(), Charset.forName("UTF-8"))
        str = URLEncoder.encode(str, "UTF-8")
        return str
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""


}

/**
 * 扩展String的toastShow方法
 * 居中显示
 */
fun String.showAtTop() {
    val toastRoot = View.inflate(XinlyCore.context, R.layout.layout_toast_center, null)
    val tvText = toastRoot.findViewById<TextView>(R.id.tv_content)
    tvText?.text = this
    DToast.make(XinlyCore.context).setView(toastRoot).setGravity(Gravity.TOP).show()
}


/**
 * 扩展String的base64 encode方法
 * 居中显示
 */
fun String.base64Encode(): String {
    try {
        return Base64.encodeToString(this.toByteArray(Charset.defaultCharset()), Base64.URL_SAFE)
    } catch (e: UnsupportedEncodingException) {
        e.printStackTrace()
    }
    return ""

}

/**
 *
 * */


/**
 * 扩展String的base64 decode方法
 * 居中显示
 */
fun String.decodeBase64ToString(): String {
    try {
        return String(Base64.decode(this.toByteArray(Charset.defaultCharset()), Base64.DEFAULT));
    } catch (e: UnsupportedEncodingException) {
        e.printStackTrace()
    }
    return ""

}

/**
 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
 */
fun Int.dp2px(): Int {
    return (0.5f + this * Resources.getSystem().displayMetrics.density).toInt()
}


fun String.fromHtml(): Spanned {
    this.apply {
        this.replace("<p>", "<br>")
        this.replace("</p>", "")
        this.replaceFirst("<br>", "")

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(this)
        }
    }

}

fun <T> Array<T>.plus(element: T): Array<T?> {
    val index = size
    val result = this.copyOf(index + 1)
    result[index] = element
    return result
}


//Glide
fun String?.resourceHeader(): GlideUrl {

    return GlideUrl(
        if (this.isNullOrEmpty()) "https://app.zddyun.com/" else this,
        LazyHeaders.Builder().addHeader("Referer", "https://app.zddyun.com/").build()
    )

}

fun String.byteCount(): Int {

    return this.toByteArray(Charset.forName("GBK")).size
}


fun String.fileType(): String {
    if (this.contains(".")) {
        return this.substring(this.lastIndexOf(".") + 1)
    }
    return "unknow"
}


/**
 * 复制文件到应用私有目录分享
 * */
fun Context.copyToAppProvider(sourceFile: String, fileName: String): File {

    val file = File(getExternalFilesDir(""), "file_temp_dir/${fileName}");
    file.run {
        parentFile?.mkdirs()
        if (!file.exists()) file.createNewFile()
    }
    val uriForFile = FileProvider.getUriForFile(this, "com.juju.zhdd" + ".fileprovider", file)
    val fos = contentResolver.openOutputStream(uriForFile)

    val fis = FileInputStream(sourceFile)
    val byteArray = ByteArray(1024)
    try {
        fos.use { outputStream ->
            fis.use { inputStream ->
                while (true) {
                    val readLen = inputStream.read(byteArray)
                    if (readLen == -1) {
                        break
                    }
                    outputStream?.write(byteArray, 0, readLen)
                }
            }
        }
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    return file;


}

fun Any.log(){

    Log.i("xiangyao",this.toString())


}