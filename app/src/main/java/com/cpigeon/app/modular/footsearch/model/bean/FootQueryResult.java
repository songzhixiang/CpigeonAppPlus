package com.cpigeon.app.modular.footsearch.model.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/3.
 */

public class FootQueryResult implements Serializable {
    //    mc,orgname,xmmc,bskj,st,csys,speed,foot
    private int mc;
    private String orgname;
    private String xmmc;
    private double bskj;
    private String st;
    private int csys;
    private double speed;
    private String foot;
    private String name;

    public int getMc() {
        return mc;
    }

    public void setMc(int mc) {
        this.mc = mc;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    public double getBskj() {
        return bskj;
    }

    public void setBskj(double bskj) {
        this.bskj = bskj;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public int getCsys() {
        return csys;
    }

    public void setCsys(int csys) {
        this.csys = csys;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getFoot() {
        return foot;
    }

    public void setFoot(String foot) {
        this.foot = foot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
