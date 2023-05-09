package com.juju.core.rx

/**
 * 定义通用异常
 * <p>
 * Created by xiangyao on 2019/3/24.
 */
class BaseException(val code:Int, val msg:String): Throwable()