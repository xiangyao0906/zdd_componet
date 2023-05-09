package com.juju.core.ext

import android.annotation.SuppressLint
import android.content.Context
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * SharedPreferences扩展
 * <p>
 * Created by xiangyao on 2019/3/24.
 */
@SuppressLint("WrongConstant")
class Preferences<T>(private val context: Context, val name: String, private val default: T, private val preName: String="sp_juju"): ReadWriteProperty<Any?, T>{

    private val prefs by lazy {
        context.getSharedPreferences(preName, Context.MODE_APPEND)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreferences(name)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
       putPreferences(name, value)
    }

    @SuppressLint("CommitPrefEdits")
    private fun putPreferences(key: String, value: T) {
        with(prefs.edit()) {
            when(value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Boolean -> putBoolean(key, value)
                else -> throw IllegalArgumentException("Unsupported type.")
            }
        }.apply()
    }

    @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
    private fun findPreferences(key: String): T {
        return when(default) {
            is String -> prefs.getString(key, default)
            is Int -> prefs.getInt(key, default)
            is Long -> prefs.getLong(key, default)
            is Boolean -> prefs.getBoolean(key, default)
            else -> throw IllegalArgumentException("Unsupported type.")
        } as T
    }
}