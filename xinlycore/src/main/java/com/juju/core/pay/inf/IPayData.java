package com.juju.core.pay.inf;

/**
 * Created by xiangyao on 2018/10/24.
 */
public interface IPayData<T> {

    /**
     * 获取数据
     * @return
     */
    T getPayData();

    /**
     * 检查数据合法性
     * @return
     */
    boolean checkPayData();
}
