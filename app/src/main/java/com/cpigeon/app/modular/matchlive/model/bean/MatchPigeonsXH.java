package com.cpigeon.app.modular.matchlive.model.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 集鸽数据
 * Created by Administrator on 2016/11/24.
 */
@Table(name = "matchpigeons_xh")
public class MatchPigeonsXH extends MatchPigeons {
    @Column(name = "pn")
    private String pn;
    @Column(name = "zx")
    private String zx;
    @Column(name = "zy")
    private String zy;

    public MatchPigeonsXH() {
    }

    public String getPn() {
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }

    public String getZx() {
        return zx;
    }

    public void setZx(String zx) {
        this.zx = zx;
    }

    public String getZy() {
        return zy;
    }

    public void setZy(String zy) {
        this.zy = zy;
    }
}
