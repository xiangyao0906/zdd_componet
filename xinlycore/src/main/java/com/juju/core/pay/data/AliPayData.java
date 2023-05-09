package com.juju.core.pay.data;

import android.text.TextUtils;
import com.juju.core.pay.PayData;
import com.juju.core.pay.inf.IPayData;

/**
 * Created by xiangyao on 2018/10/24.
 */
public class AliPayData implements IPayData<AliPayData> {
    public String sign;

    public AliPayData(PayData payData) {
        initPayData(payData);
    }

    /**
     *
     * @param payData
     */
    private void initPayData(PayData payData) {
        this.sign = payData.getSign();
    }

    @Override
    public AliPayData getPayData() {
        return this;
    }

    @Override
    public boolean checkPayData() {
        if (TextUtils.isEmpty(sign)){
            return false;
        }
        return true;
    }
}
