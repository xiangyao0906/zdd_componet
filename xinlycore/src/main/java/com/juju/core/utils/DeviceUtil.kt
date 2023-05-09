package com.juju.core.utils

import android.Manifest.permission.READ_PHONE_STATE
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.Environment.MEDIA_MOUNTED
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresPermission
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.*


/**
 * Created by xiangyao on 2019-06-26.
 */
object DeviceUtil {

    /**
     * dp转换为px
     * @param context
     * @param dpValue
     * @return
     */
    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * px 转化为 dp
     * @param context
     * @param pxValue
     * @return
     */
    fun px2dp(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity;
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * 获取设备宽度（px）
     * @param context
     * @return
     */
    fun deviceWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    /**
     * 获取设备高度（px）
     * @param context
     * @return
     */
    fun deviceHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    /**
     * SD卡判断
     * @return
     */
    fun isSDCardAvailable(): Boolean {
        return Environment.getExternalStorageState() == MEDIA_MOUNTED
    }

    /**
     * 返回版本名字
     * 对应build.gradle中的versionName
     *
     * @param context
     * @return
     */
    fun getVersionName(context: Context): String {
        return try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName
        } catch (e: Exception) {
            Log.e(DeviceUtil::class.java.name, e.message, e)
            ""
        }
    }

    /**
     * 返回版本号
     * 对应build.gradle中的versionCode
     *
     * @param context
     * @return
     */
    fun getVersionCode(context: Context): String {
        return try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode.toString()
            } else {
                packageInfo.versionCode.toString()
            }
        } catch (e: Exception) {
            Log.e(DeviceUtil::class.java.name, e.message, e)
            ""
        }
    }

    /**
     * 获取设备的唯一标识，deviceId
     * @param context
     * @return
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(READ_PHONE_STATE)
    private fun getDeviceId(context: Context): String {
        val tm: TelephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val deviceId: String? = tm.deviceId
        return deviceId ?: ""
    }

    /**
     * 获取IMEI
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    fun getIMEI(context: Context): String {
        val tm: TelephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        when {
            Build.VERSION.SDK_INT >= 29 -> {
                return Settings.System.getString(
                    context.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                return tm.imei
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                try {
                    val clazz: Class<*> = tm.javaClass
                    //noinspection unchecked
                    val getImeiMethod = clazz.getDeclaredMethod("getImei")
                    getImeiMethod.isAccessible = true
                    val imei: String? = getImeiMethod.invoke(tm) as? String
                    if (imei != null) return imei
                } catch (e: Exception) {
                    Log.e(DeviceUtil::class.java.name, e.message, e)
                }
            }
        }
        return getDeviceId(context)
    }

    /**
     * 获取手机品牌
     *
     * @return
     */
    fun getPhoneBrand(): String {
        return Build.BRAND
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    fun getPhoneModel(): String {
        return Build.MODEL
    }

    /**
     * 获取手机Android 版本（4.4、5.0、5.1 ...）
     *
     * @return
     */
    fun getBuildVersion(): String {
        return Build.VERSION.RELEASE
    }

    /**
     * 获取当前App进程的id
     *
     * @return
     */
    fun getAppProcessId(): Int {
        return android.os.Process.myPid()
    }

    /**
     * 获取AndroidManifest.xml里 的值
     *
     * @param context
     * @param name
     * @return
     */
    fun getMetaData(context: Context, name: String): String? {
        var value: String? = null
        try {
            val appInfo = context.packageManager.getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            )
            value = appInfo.metaData.getString(name)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return value
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    fun getProcessName(pid: Int): String {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                reader?.close()
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
        return ""
    }

    fun getUnPermissionId(): String {

        var serial: String? = null
        val m_szDevIDShort =
            "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 + Build.HOST.length % 10 + Build.ID.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10 + Build.TAGS.length % 10 + Build.TYPE.length % 10 + Build.USER.length % 10 //13 位
        try {
            serial = Build::class.java.getField("SERIAL")[null].toString()
            //API>=9 使用serial号
            return UUID(
                m_szDevIDShort.hashCode().toLong(),
                serial.hashCode().toLong()
            ).toString()
        } catch (exception: java.lang.Exception) {
            //serial需要一个初始化
            serial = "serial" // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
    }
}