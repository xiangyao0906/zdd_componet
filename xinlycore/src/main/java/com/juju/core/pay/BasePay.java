package com.juju.core.pay;


import com.juju.core.pay.inf.IPay;
import com.juju.core.pay.inf.IPayData;

/**
 * Created by xiangyao on 2018/10/24.
 */
public abstract class BasePay<T> implements IPay<T> {
    protected PayCallback mPayCallback;
    protected IPayData<T> mPayData;


    @Override
    public void setPayData(IPayData<T> payData) {
        this.mPayData = payData;
    }

    @Override
    public void setPayCallback(PayCallback callback) {
        this.mPayCallback = callback;
    }

    @Override
    public void pay() {

    }
}
