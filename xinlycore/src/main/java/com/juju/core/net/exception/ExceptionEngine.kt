package com.juju.core.net.exception

import android.net.ParseException
import com.google.gson.JsonParseException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

/**
 * Created by xiangyao on 2019-06-20.
 */
class ExceptionEngine {
    companion object {
        private const val ERROR_FORBIDDEN = "登录已过期，请重新登录"
        private const val ERROR_SERVER_TRY_AGAIN = "遇到错误了，请稍后再试"
        private const val ERROR_CONNECT_TRAIN = "网络不太好，请检查网络再试试"
        private const val ERROR_HTTP_NOT_FOUND = "404"
        private const val ERROR_HTTP_BAD_WAY = "网关错误"
        private const val ERROR_SERVER_COMMON = "服务器错误"
        private const val ERROR_SERVER_NOT_RESPONSE = "服务器响应超时"
        private const val ERROR_SERVICE = "服务器内部错误"

        private const val UNAUTHORIZED = 401
        private const val FORBIDDEN = 403
        private const val NOT_FOUND = 404
        private const val REQUEST_TIMEOUT = 408
        private const val INTERNAL_SERVER_ERROR = 500
        private const val BAD_GATEWAY = 502
        private const val SERVICE_UNAVAILABLE = 503
        private const val GATEWAY_TIMEOUT = 504

        fun handleException(e: Throwable): ApiException {
            return when (e) {

                is HttpException -> {
                    when (e.code()) {
                        /**
                         * 1权限错误
                         */
                        UNAUTHORIZED, FORBIDDEN ->
                            ApiException(e, ERROR.PERMISSION_ERROR, ERROR_FORBIDDEN)
                        /**
                         * 2网络错误
                         */
                        REQUEST_TIMEOUT, GATEWAY_TIMEOUT ->
                            ApiException(e, ERROR.NETWORD_ERROR, ERROR_CONNECT_TRAIN)
                        /**
                         * 2网络错误
                         */
                        NOT_FOUND -> ApiException(e, ERROR.HTTP_ERROR, ERROR_HTTP_NOT_FOUND)

                        BAD_GATEWAY -> ApiException(e, ERROR.HTTP_ERROR, ERROR_HTTP_BAD_WAY)
                        SERVICE_UNAVAILABLE -> ApiException(
                            e,
                            ERROR.HTTP_ERROR,
                            ERROR_SERVER_COMMON
                        )
                        INTERNAL_SERVER_ERROR -> ApiException(e, ERROR.HTTP_ERROR, ERROR_SERVICE)
                        /**
                         * 2网络错误
                         */
                        else ->
                            ApiException(e, ERROR.HTTP_ERROR, ERROR_SERVER_TRY_AGAIN)
                    }
                }

                /**
                 * 3链接错误
                 */
                is ConnectException -> ApiException(e, ERROR.NETWORD_ERROR, ERROR_CONNECT_TRAIN)
                is SocketTimeoutException -> ApiException(
                    e,
                    ERROR.NETWORD_ERROR,
                    ERROR_SERVER_NOT_RESPONSE
                )
                /**
                 * 4.服务器返回错误
                 */
                is ServerException -> {
                    /**
                     * 无权限
                     */
                    val message: String = if (e.isForbidden()) {
                        e.message ?: ERROR_FORBIDDEN
                    } else {
                        e.message ?: ERROR_SERVER_TRY_AGAIN
                    }
                    ApiException(e, ERROR.RETURN_ERROR, message)
                }
                /**
                 * 解析错误
                 */
                is JsonParseException -> ApiException(e, ERROR.PARSE_ERROR, ERROR_SERVER_TRY_AGAIN)
                is JSONException -> ApiException(e, ERROR.PARSE_ERROR, ERROR_SERVER_TRY_AGAIN)
                is ParseException -> ApiException(e, ERROR.PARSE_ERROR, ERROR_SERVER_TRY_AGAIN)
                /**
                 * 服务器错误
                 */
                is ApiException -> ApiException(e, e.code, e.msg)
                else ->
                    ApiException(e, ERROR.UNKNOWN, e.message ?: ERROR_SERVER_TRY_AGAIN)
            }
        }
    }


    /**
     * 错误
     */
    object ERROR {
        /**
         * 未知错误
         */
        const val UNKNOWN: Int = 1000

        /**
         * 解析错误
         */
        const val PARSE_ERROR: Int = 1001

        /**
         * data null
         */
        const val PARSE_DATA_ERROR: Int = 1009

        /**
         * 网络错误
         */
        const val NETWORD_ERROR: Int = 1002

        /**
         * 协议出错
         */
        const val HTTP_ERROR: Int = 1003

        /**
         * 权限
         */
        const val PERMISSION_ERROR: Int = 401

        /**
         * 返回结果为错误
         */
        const val RETURN_ERROR: Int = 1005
    }

}