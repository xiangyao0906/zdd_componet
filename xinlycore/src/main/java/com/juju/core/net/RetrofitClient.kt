package com.juju.core.net

import com.juju.core.XinlyCore
import com.juju.core.ext.no
import com.juju.core.ext.yes
import com.juju.core.utils.NetWorkUtils
import com.juju.core.utils.SystemUtil
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * 当前项目使用retrofit 作为http请求引擎
 * <p>
 * Created by xiangyao on 2019-06-19.
 */
open class RetrofitClient {

    companion object {
        fun <T> createApi(clazz: Class<T>, baseUrl: String): T {
            val builder: Retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getClient())
                .build()
            return builder.create(clazz)
        }

        private fun getClient(): OkHttpClient {
            /**
             * 使用自定义logInterceptor 支持 chrome输出，在debug 或 beta版本
             */
            val logInterceptor = JHttpLoggingInterceptor()
            logInterceptor.level = JHttpLoggingInterceptor.Level.BODY

            /**
             * 本地缓存
             */
            val cacheFile = File(XinlyCore.context.cacheDir, "cache")
            val cache = Cache(cacheFile, 1024 * 1024 * 100)

            /**
             * 请求缓存策略
             */
            val mRewriteCacheControlInterceptor = getInterceptor()

            /**
             * ssl
             */
            val sslParams = SSL.getSslSocketFactory(null, null, null)

            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            val okHttpClient: OkHttpClient.Builder = OkHttpClient.Builder()
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(mRewriteCacheControlInterceptor)
                .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                .sslSocketFactory(sslParams.sSLSocketFactory!!, sslParams.trustManager!!)
                .cache(cache)

            (SystemUtil.isCanLog()).yes {
//                okHttpClient.addInterceptor(logInterceptor)
            }
            return okHttpClient.build()
        }

        //读超时长，单位：毫秒
        const val READ_TIME_OUT: Long = 30

        //连接时长，单位：毫秒
        const val CONNECT_TIME_OUT: Long = 30

        /*************************缓存设置*********************/
        /*
        1. noCache 不使用缓存，全部走网络

        2. noStore 不使用缓存，也不存储缓存

        3. onlyIfCached 只使用缓存

        4. maxAge 设置最大失效时间，失效则不使用 需要服务器配合

        5. maxStale 设置最大失效时间，失效则不使用 需要服务器配合

        6. minFresh 设置有效时间，依旧如上

        7. FORCE_NETWORK 只走网络

        8. FORCE_CACHE 只走缓存
        */

        /**
         * 设缓存有效期为两天
         */
        private const val CACHE_STALE_SEC: Long = 60 * 60 * 24 * 2

        /**
         * 查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
         * max-stale 指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可接收超出超时期指定值之内的响应消息。
         */
        private const val CACHE_CONTROL_CACHE = "only-if-cached, max-stale=$CACHE_STALE_SEC"

        /**
         * 查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
         * (假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
         */
        private const val CACHE_CONTROL_AGE = "max-age=0"

        fun getInterceptor(): Interceptor {
            return Interceptor { chain ->
                var request = chain.request()

                NetWorkUtils.isNetWorkAvailable(XinlyCore.context).no {
                    request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build()
                }
                val originalResponse = chain.proceed(request)

                if (NetWorkUtils.isNetWorkAvailable(XinlyCore.context)) {
                    //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                    val cacheControl = request.cacheControl.toString()
                    return@Interceptor originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build()
                } else {
                    return@Interceptor originalResponse.newBuilder()
                        .header(
                            "Cache-Control",
                            "public, only-if-cached, max-stale=$CACHE_STALE_SEC"
                        )
                        .removeHeader("Pragma")
                        .build()
                }
            }
        }
    }
}