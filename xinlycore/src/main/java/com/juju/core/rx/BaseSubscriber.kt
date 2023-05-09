package com.juju.core.rx

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Rx订阅者默认实现
 * <p>
 * Created by xiangyao on 2019/3/24.
 */
abstract class BaseSubscriber<T : Any>: Observer<T> {

    override fun onSubscribe(d: Disposable) {
    }

    override fun onComplete() {
    }

    override fun onNext(t: T) {

    }

    override fun onError(e: Throwable) {

    }
}