package com.juju.core.rx

import com.juju.core.data.protocol.BaseResp
import com.juju.core.net.exception.ApiException
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function

/**
 * Boolean类型转换封装
 * <p>
 * Created by xiangyao on 2019/3/24.
 */
class BaseFuncBoolean<T>: Function<BaseResp<T>, ObservableSource<out Boolean>> {
    override fun apply(t: BaseResp<T>): ObservableSource<out Boolean> {
        if (t.isSuccess()) {
            return Observable.just(true)
        }
        return Observable.error(ApiException(null, t.code, t.msg))
    }
}