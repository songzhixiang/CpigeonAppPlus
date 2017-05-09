package com.cpigeon.app.modular.matchlive.model.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 上笼清单
 * Created by Administrator on 2016/11/24.
 */
@Table(name = "matchpigeons_gp")
public class MatchPigeonsGP extends MatchPigeons {

    @Column(name = "area")
    private String area;
    @Column(name = "color")
    private String color;
    @Column(name = "ring")
    private String ring;
    @Column(name = "uptime")
    private String uptime;
    @Column(name = "ttzb")
    private String ttzb;

    public MatchPigeonsGP() {
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRing() {
        return ring;
    }

    public void setRing(String ring) {
        this.ring = ring;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getTtzb() {
        return ttzb;
    }

    public void setTtzb(String ttzb) {
        this.ttzb = ttzb;
    }
}
