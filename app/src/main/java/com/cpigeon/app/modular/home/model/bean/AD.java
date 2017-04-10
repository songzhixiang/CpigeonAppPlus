package com.cpigeon.app.modular.home.model.bean;

/**
 * Created by Administrator on 2016/12/8.
 */

public class AD {
    private int id;
    private String adImageUrl;
    private String adUrl;

    public AD() {
    }

    public AD(String adImageUrl, String adUrl) {
        this.adImageUrl = adImageUrl;
        this.adUrl = adUrl;
    }

    public AD(int id, String adImageUrl, String adUrl) {
        this.id = id;
        this.adImageUrl = adImageUrl;
        this.adUrl = adUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdImageUrl() {
        return adImageUrl;
    }

    public void setAdImageUrl(String adImageUrl) {
        this.adImageUrl = adImageUrl;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }
}
