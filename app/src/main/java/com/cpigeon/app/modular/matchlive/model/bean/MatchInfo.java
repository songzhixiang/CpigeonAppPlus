package com.cpigeon.app.modular.matchlive.model.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 比赛信息
 * Created by Administrator on 2016-10-26.
 */

@Table(name = "matchInfo")
public class MatchInfo implements Serializable{

    @Column(name = "ssid", isId = true, autoGen = false)
    private String ssid;//比赛ID
    @Column(name = "mc")
    private String mc;//公棚名称
    @Column(name = "bsmc")
    private String bsmc;//比赛项目名称
    @Column(name = "st")
    private String st;//司放时间
    @Column(name = "area")
    private String area;//司放地点
    @Column(name = "sfjd")
    private String sfjd;//司放经度
    @Column(name = "sfwd")
    private String sfwd;//司放纬度
    @Column(name = "cpz")
    private String cpz;//裁判长
    @Column(name = "sfz")
    private String sfz;//司放长
    @Column(name = "cpy")
    private String cpy;//裁判员
    @Column(name = "slys")
    private int slys;//上笼羽数
    @Column(name = "gcys")
    private int gcys;//归巢羽数
    @Column(name = "bskj")
    private double bskj;//比赛空距
    @Column(name = "csys")
    private int csys;//参赛羽数
    @Column(name = "jd")
    private String jd;//经度（网站后台设置）
    @Column(name = "wd")
    private String wd;//纬度（网站后台设置）
    @Column(name = "zzid")
    private int zzid;//公棚id
    @Column(name = "tq")
    private String tq;//天气（网站后台训放设置）
    @Column(name = "fl")
    private String fl;//风力（网站后台训放设置）
    @Column(name = "fx")
    private String fx;//风向（网站后台训放设置）
    @Column(name = "xsys")
    private int xsys;//显示羽数（网站后台训放设置）
    @Column(name = "xmbt")
    private String xmbt;//项目标题（网站后台训放设置）
    @Column(name = "lx")
    private String lx;//赛事类型
    @Column(name = "dt")
    private String dt;//数据类型
    @Column(name = "ruid")
    private int ruid;

    public int getRuid() {
        return ruid;
    }

    public void setRuid(int ruid) {
        this.ruid = ruid;
    }

    public MatchInfo() {
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    /**
     * 获取赛事id
     *
     * @return
     */
    public String getSsid() {
        return ssid;
    }

    /**
     * 设置赛事id
     *
     * @param ssid
     */
    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    /**
     * 获取公棚/协会名称
     *
     * @return
     */
    public String getMc() {
        return mc;
    }

    /***
     * 设置公棚/协会名称
     *
     * @param mc
     */
    public void setMc(String mc) {
        this.mc = mc;
    }

    /**
     * 获取比赛名称
     *
     * @return
     */
    public String getBsmc() {
        return bsmc;
    }


    /**
     * 设置比赛名称
     *
     * @param bsmc
     */
    public void setBsmc(String bsmc) {
        this.bsmc = bsmc;
    }

    /**
     * 获取司放时间
     *
     * @return
     */
    public String getSt() {
        return st;
    }

    /**
     * 设置司放时间
     *
     * @param st
     */
    public void setSt(String st) {
        this.st = st;
    }

    /**
     * 获取司放地点
     *
     * @return
     */
    public String getArea() {
        return area;
    }

    /**
     * 设置司放地点
     *
     * @param area
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * 获取司放经度
     *
     * @return
     */
    private String getSfjd() {
        return sfjd;
    }

    /**
     * 设置司放经度
     *
     * @param sfjd
     */
    public void setSfjd(String sfjd) {
        this.sfjd = sfjd;
    }

    /**
     * 获取司放纬度
     *
     * @return
     */
    private String getSfwd() {
        return sfwd;
    }

    /**
     * 设置司放纬度
     *
     * @param sfwd
     */
    public void setSfwd(String sfwd) {
        this.sfwd = sfwd;
    }

    /**
     * 获取裁判长
     *
     * @return
     */
    public String getCpz() {
        return cpz;
    }

    /**
     * 设置裁判长
     *
     * @param cpz
     */
    public void setCpz(String cpz) {
        this.cpz = cpz;
    }

    /**
     * 获取司放长
     *
     * @return
     */
    public String getSfz() {
        return sfz;
    }

    /**
     * 设置司放长
     *
     * @param sfz
     */
    public void setSfz(String sfz) {
        this.sfz = sfz;
    }

    /**
     * 获取裁判员
     *
     * @return
     */
    public String getCpy() {
        return cpy;
    }

    /**
     * 设置裁判员
     *
     * @param cpy
     */
    public void setCpy(String cpy) {
        this.cpy = cpy;
    }

    /**
     * 获取上笼羽数
     *
     * @return
     */
    private int getSlys() {
        return slys;
    }

    /**
     * 设置上笼羽数
     *
     * @param slys
     */
    public void setSlys(int slys) {
        this.slys = slys;
    }

    /**
     * 获取归巢羽数
     *
     * @return
     */
    public int getGcys() {
        return gcys;
    }

    /**
     * 设置归巢羽数
     *
     * @param gcys
     */
    public void setGcys(int gcys) {
        this.gcys = gcys;
    }

    /**
     * 获取比赛空距
     *
     * @return
     */
    public double getBskj() {
        return bskj;
    }

    /**
     * 设置比赛空距
     *
     * @param bskj
     */
    public void setBskj(double bskj) {
        this.bskj = bskj;
    }

    /**
     * 获取参赛羽数
     *
     * @return
     */
    public int getCsys() {
        return csys;
    }

    /**
     * 设置参赛羽数
     *
     * @param csys
     */
    public void setCsys(int csys) {
        this.csys = csys;
    }

    /**
     * 获取经度
     *
     * @return
     */
    private String getJd() {
        return jd;
    }

    /**
     * 设置经度
     *
     * @param jd
     */
    public void setJd(String jd) {
        this.jd = jd;
    }

    /**
     * 获取纬度
     *
     * @return
     */
    private String getWd() {
        return wd;
    }

    /**
     * 设置纬度
     *
     * @param wd
     */
    public void setWd(String wd) {
        this.wd = wd;
    }

    /**
     * 获取公棚/协会id
     *
     * @return
     */
    public int getZzid() {
        return zzid;
    }

    /**
     * 设置公棚/协会id
     *
     * @param zzid
     */
    public void setZzid(int zzid) {
        this.zzid = zzid;
    }

    /**
     * 获取天气
     *
     * @return
     */
    public String getTq() {
        return tq;
    }

    /**
     * 设置天气
     *
     * @param tq
     */
    public void setTq(String tq) {
        this.tq = tq;
    }

    /**
     * 获取风力
     *
     * @return
     */
    public String getFl() {
        return fl;
    }

    /**
     * 设置风力
     *
     * @param fl
     */
    public void setFl(String fl) {
        this.fl = fl;
    }

    /**
     * 获取风向
     *
     * @return
     */
    public String getFx() {
        return fx;
    }

    /**
     * 设置风向
     *
     * @param fx
     */
    public void setFx(String fx) {
        this.fx = fx;
    }

    /**
     * 获取显示羽数
     *
     * @return
     */
    public int getXsys() {
        return xsys;
    }

    /**
     * 设置显示羽数
     *
     * @param xsys
     */
    public void setXsys(int xsys) {
        this.xsys = xsys;
    }

    /**
     * 获取项目标题
     *
     * @return
     */
    public String getXmbt() {
        return xmbt;
    }

    /**
     * 设置项目标题
     *
     * @param xmbt
     */
    public void setXmbt(String xmbt) {
        this.xmbt = xmbt;
    }

    /**
     * 获取类型（公棚：gp；协会：xh）
     *
     * @return
     */
    public String getLx() {
        return lx;
    }

    /**
     * 设置类型（公棚：gp；协会：xh）
     *
     * @param lx
     */
    public void setLx(String lx) {
        this.lx = lx;
    }


    /**
     * 计算比赛名称
     *
     * @return
     */
    public String computerBSMC() {
        if (!"bs".equals(this.getDt())) return "xh".equals(getLx()) ? "集鸽完毕" : "上笼完毕";
        if ("xh".equals(this.getLx())) return bsmc;
        if (this.getCsys() > 0) return bsmc;
        if (!"".equals(this.getXmbt())) return this.getXmbt();
        if (this.getBskj() == 0) return "家飞测试";
        else return ((int) this.getBskj()) + "公里训放";
    }

    public boolean isMatch() {
        if ("gp".equals(this.getLx()) && this.getCsys() == 0) {
            return false;
        }
        return true;
    }

    /**
     * 计算司放坐标
     *
     * @return
     */
    public String computerSFZB() {
        if ("".equals(this.getXmbt()) && this.getCsys() <= 0) return "训放家飞无此项";
        if (!"".equals(this.getSfjd()) && !"".equals(this.getSfwd()))
            return this.getSfjd() + "/" + this.getSfwd();
        if (!"".equals(this.getJd()) && !"".equals(this.getWd()))
            return this.getJd() + "/" + this.getWd();
        return "无";
    }

    /**
     * 计算上笼羽数
     *
     * @return
     */
    public String compuberSLYS() {
        if (getCsys() > 0) return getCsys() + "羽";
        return getSlys() > 0 ? getSlys() + "羽" : "";
    }

    /**
     * 计算归巢羽数
     *
     * @param show
     * @return
     */
    public String compuberGcys(boolean show) {
        return getGcys() > 0 ? getGcys() + "羽" : show ? "0羽" : "";
    }

}
