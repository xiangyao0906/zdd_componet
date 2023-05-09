package com.juju.core.pay;

/**
 * 支付数据
 * <p>
 * Created by xiangyao on 2018/10/24.
 */
public class PayData {
    private String sign;

    public PayData() {
    }

    public PayData(String sign) {
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
