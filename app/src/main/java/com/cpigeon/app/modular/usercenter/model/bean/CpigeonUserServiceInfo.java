package com.cpigeon.app.modular.usercenter.model.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/13.
 * 用户服务信息
 */

public class CpigeonUserServiceInfo implements Serializable {
    private int serviceId;//服务ID
    private int numbers;//剩余次数
    private int servicetimes;//总次数
    private String unitname;//单位
    private String name;//服务名称
    private String packageName;//服务套餐
    private String brief;//简介
    private int showNumber;

    public int getShowNumber() {
        return showNumber;
    }

    public void setShowNumber(int showNumber) {
        this.showNumber = showNumber;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getNumbers() {
        return numbers;
    }

    public void setNumbers(int numbers) {
        this.numbers = numbers;
    }

    public int getServicetimes() {
        return servicetimes;
    }

    public void setServicetimes(int servicetimes) {
        this.servicetimes = servicetimes;
    }

    public String getUnitname() {
        return unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
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
}
