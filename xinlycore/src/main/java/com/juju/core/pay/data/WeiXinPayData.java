package com.juju.core.pay.data;

import android.text.TextUtils;
import android.util.Log;

import com.juju.core.pay.PayData;
import com.juju.core.pay.inf.IPayData;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xiangyao on 2018/10/24.
 */
public class WeiXinPayData implements IPayData<WeiXinPayData> {
    public String appId;
    public String partnerId;
    public String prepayId;
    public String nonceStr;
    public String packageValue;
    public String timeStamp;
    public String sign;


    public WeiXinPayData(PayData payData) {
        initPayData(payData);
    }

    /**
     * @param payData
     */
    private void initPayData(PayData payData) {
        String sign = payData.getSign();
        try {
            JSONObject jsonObject = new JSONObject(sign);
            this.appId = jsonObject.getString("appid");
            this.partnerId = jsonObject.getString("partnerid");
            this.prepayId = jsonObject.getString("prepayid");
            this.packageValue = jsonObject.getString("package");
            this.nonceStr = jsonObject.getString("noncestr");
            this.timeStamp = jsonObject.getString("timestamp");

            if(jsonObject.has("paySign")){
                this.sign = jsonObject.getString("paySign");
            }

            if(jsonObject.has("sign")){
                this.sign = jsonObject.getString("sign");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public WeiXinPayData getPayData() {
        return this;
    }

    @Override
    public boolean checkPayData() {
        if (TextUtils.isEmpty(appId)
                || TextUtils.isEmpty(this.partnerId)
                || TextUtils.isEmpty(this.prepayId)
                || TextUtils.isEmpty(this.nonceStr)
                || TextUtils.isEmpty(this.partnerId)
                || TextUtils.isEmpty(this.timeStamp)
                || TextUtils.isEmpty(this.sign)
                || TextUtils.isEmpty(this.packageValue)) {
            return false;
        }
        return true;
    }

    @NotNull
    @Override
    public String toString() {
        return "WeiXinPayData{" +
                "appId='" + appId + '\'' +
                ", partnerId='" + partnerId + '\'' +
                ", prepayId='" + prepayId + '\'' +
                ", nonceStr='" + nonceStr + '\'' +
                ", packageValue='" + packageValue + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
