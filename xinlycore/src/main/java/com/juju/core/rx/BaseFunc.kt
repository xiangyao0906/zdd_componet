package com.juju.core.rx

import com.juju.core.data.protocol.BaseResp
import com.juju.core.net.exception.ApiException
import com.juju.core.net.exception.ExceptionEngine
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function

/**
 * 通用数据类型转换封装
 * <p>
 * Created by xiangyao on 2019/3/24.
 */
class BaseFunc<T>: Function<BaseResp<T>, ObservableSource<T>>{
    override fun apply(t: BaseResp<T>): ObservableSource<T> {
        if (t.isSuccess()) {
            return if(t.data==null){
                Observable.empty()
            }else{
                Observable.just(t.data)
            }
        }
        return Observable.error(ApiException(null, t.code, t.msg))
    }

}