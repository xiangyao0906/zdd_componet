package com.juju.core.data.protocol

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Created by xiangyao on 2019-06-27.
 */
abstract class BaseReq<T>(protected val data: T) : IReq<T> {

    override fun toRequestBody(): RequestBody {
        val json: String = Gson().toJson(this)
        return json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    }
}