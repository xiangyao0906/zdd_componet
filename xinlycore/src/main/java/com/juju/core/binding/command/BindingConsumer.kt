package com.juju.core.binding.command

/**
 * 一个参数事件
 * <p>
 * Created by xiangyao on 2019/1/5.
 */
interface BindingConsumer<T> {
    fun call(t: T)
}