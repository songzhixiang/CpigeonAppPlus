package com.cpigeon.app.modular.order.model.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/4.
 */

public class CpigeonServicesInfo implements Serializable {

    private int id;
    private String name;
    private String packageName;
    private String brief;
    private String detial;
    private String unitname;
    private String opentime;
    private String expiretime;
    private int servicetimes;
    private int flag;
    private double price;
    private int scores;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getDetial() {
        return detial;
    }

    public void setDetial(String detial) {
        this.detial = detial;
    }

    public String getUnitname() {
        return unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public void setExpiretime(String expiretime) {
        this.expiretime = expiretime;
    }

    public String getOpentime() {
        return opentime;
    }

    public String getExpiretime() {
        return expiretime;
    }

    public int getServicetimes() {
        return servicetimes;
    }

    public void setServicetimes(int servicetimes) {
        this.servicetimes = servicetimes;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }
}
