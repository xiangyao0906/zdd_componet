package com.juju.core.data.protocol

import com.juju.core.data.BaseResultCode

/**
 * 返回数据基类
 * <p>
 * Created by xiangyao on 2019/3/24.
 */
data class BaseResp<out T>(val code: Int, val msg: String, val data: T) : IResp {
    override fun getICode(): Int {
        return code
    }

    override fun getMessage(): String {
        return msg
    }


    //兼容code
    override fun isSuccess(): Boolean {
        if (code == 0) {
            return true
        }
        return code == BaseResultCode.SUCCESS
    }
}