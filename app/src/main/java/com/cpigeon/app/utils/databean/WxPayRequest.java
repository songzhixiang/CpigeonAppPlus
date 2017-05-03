package com.cpigeon.app.utils.databean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chenshuai on 2017/5/3.
 */

public class WxPayRequest {

    /**
     * appid : wxc9d120321bd1180a
     * partnerid : 1434404202
     * prepayid : wx20170503094448465a1522300759368240
     * package : Sign=WXPay
     * noncestr : ccb0989662211f61edae2e26d58ea92f
     * timestamp : 1493776678
     * sign : 2006053352672C3DE7408E4543632C7E
     */

    private String appid;
    private String partnerid;
    private String prepayid;
    @SerializedName("package")
    private String packageX;
    private String noncestr;
    private int timestamp;
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
