package com.juju.core.rx

import com.juju.core.data.protocol.BaseResp
import com.juju.core.net.exception.ApiException
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function

/**
 * 通用数据类型转换封装
 * <p>
 * Created by xiangyao on 2019/3/24.
 */
class BaseFunc1<T>: Function<BaseResp<T>, ObservableSource<BaseResp<T>>>{
    override fun apply(t: BaseResp<T>): ObservableSource<BaseResp<T>> {
        if (t.isSuccess()) {
            return Observable.just(t)
        }
        return Observable.error(ApiException(null, t.code?:500, t.msg))
    }

}