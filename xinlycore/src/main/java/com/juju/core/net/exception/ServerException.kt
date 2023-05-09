package com.juju.core.net.exception

import java.lang.RuntimeException

/**
 * Created by xiangyao on 2019-06-20.
 */
class ServerException(var code: Int, message: String?, var any: Any?) : RuntimeException(message) {

    /**
     * 权限失效
     */
    fun isForbidden(): Boolean {
        return code == -2
    }
}