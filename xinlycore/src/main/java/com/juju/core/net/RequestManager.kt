package com.juju.core.net

import java.util.*

/**
 * Created by xiangyao on 2019-06-19.
 */
open class RequestManager {
    protected var sRequestManager: HashMap<Class<*>, Any> = HashMap()

    @Suppress("UNCHECKED_CAST")
    fun <T: Any> getRequest(clazz: Class<T>, baseUrl: String): T {
        var t: T? = sRequestManager[clazz] as? T
        if (t == null) {
            t = RetrofitClient.createApi(clazz, baseUrl)
            sRequestManager[clazz] = t
        }
        return t
    }
}