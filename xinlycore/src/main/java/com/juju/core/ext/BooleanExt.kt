package com.juju.core.ext

//Boolean扩展
//Created by xiangyao on 2019/3/24.

sealed class BooleanExt<out T>

object Otherwise : BooleanExt<Nothing>()
class WithData<T>(val data: T) : BooleanExt<T>()

inline fun <T> Boolean.yes(block: () -> T) =
    when {
        this -> {
            WithData(block())
        }
        else -> {
            Otherwise
        }
    }

inline fun <T> Boolean.no(block: () -> T) =
    when {
        this -> Otherwise
        else -> {
            WithData(block())
        }
    }

inline fun <T> BooleanExt<T>.otherwise(block: () -> T): T =
    when (this) {
        is Otherwise -> block()
        is WithData -> this.data
    }