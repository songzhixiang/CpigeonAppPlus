package com.cpigeon.app.modular.matchlive.model.bean;

import org.xutils.db.annotation.Column;

import java.io.Serializable;

/**
 * 集鸽数据，上笼清单的抽象类
 * Created by Administrator on 2016/11/24.
 */

public abstract class MatchPigeons implements Serializable {
    @Column(name = "order")
    private int order;
    @Column(name = "tid", isId = true, autoGen = false)
    private long tid;
    @Column(name = "name")
    private String name;
    @Column(name = "foot")
    private String foot;
    @Column(name = "jgtime")
    private String jgtime;
    @Column(name = "ssid",property = "NOT NULL CONSTRAINT fk_collection_ssid REFERENCES matchInfo(\"ssid\") ON DELETE CASCADE ON UPDATE CASCADE")
    private String ssid;

    @Column(name = "c1")
    private boolean c1;
    @Column(name = "c2")
    private boolean c2;
    @Column(name = "c3")
    private boolean c3;
    @Column(name = "c4")
    private boolean c4;
    @Column(name = "c5")
    private boolean c5;
    @Column(name = "c6")
    private boolean c6;
    @Column(name = "c7")
    private boolean c7;
    @Column(name = "c8")
    private boolean c8;
    @Column(name = "c9")
    private boolean c9;
    @Column(name = "c10")
    private boolean c10;
    @Column(name = "c11")
    private boolean c11;
    @Column(name = "c12")
    private boolean c12;
    @Column(name = "c13")
    private boolean c13;
    @Column(name = "c14")
    private boolean c14;
    @Column(name = "c15")
    private boolean c15;
    @Column(name = "c16")
    private boolean c16;
    @Column(name = "c17")
    private boolean c17;
    @Column(name = "c18")
    private boolean c18;
    @Column(name = "c19")
    private boolean c19;
    @Column(name = "c20")
    private boolean c20;
    @Column(name = "c21")
    private boolean c21;
    @Column(name = "c22")
    private boolean c22;
    @Column(name = "c23")
    private boolean c23;
    @Column(name = "c24")
    private boolean c24;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFoot() {
        return foot;
    }

    public void setFoot(String foot) {
        this.foot = foot;
    }

    public String getJgtime() {
        return jgtime;
    }

    public void setJgtime(String jgtime) {
        this.jgtime = jgtime;
    }


    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public boolean isC1() {
        return c1;
    }

    public void setC1(boolean c1) {
        this.c1 = c1;
    }

    public boolean isC2() {
        return c2;
    }

    public void setC2(boolean c2) {
        this.c2 = c2;
    }

    public boolean isC3() {
        return c3;
    }

    public void setC3(boolean c3) {
        this.c3 = c3;
    }

    public boolean isC4() {
        return c4;
    }

    public void setC4(boolean c4) {
        this.c4 = c4;
    }

    public boolean isC5() {
        return c5;
    }

    public void setC5(boolean c5) {
        this.c5 = c5;
    }

    public boolean isC6() {
        return c6;
    }

    public void setC6(boolean c6) {
        this.c6 = c6;
    }

    public boolean isC7() {
        return c7;
    }

    public void setC7(boolean c7) {
        this.c7 = c7;
    }

    public boolean isC8() {
        return c8;
    }

    public void setC8(boolean c8) {
        this.c8 = c8;
    }

    public boolean isC9() {
        return c9;
    }

    public void setC9(boolean c9) {
        this.c9 = c9;
    }

    public boolean isC10() {
        return c10;
    }

    public void setC10(boolean c10) {
        this.c10 = c10;
    }

    public boolean isC11() {
        return c11;
    }

    public void setC11(boolean c11) {
        this.c11 = c11;
    }

    public boolean isC12() {
        return c12;
    }

    public void setC12(boolean c12) {
        this.c12 = c12;
    }

    public boolean isC13() {
        return c13;
    }

    public void setC13(boolean c13) {
        this.c13 = c13;
    }

    public boolean isC14() {
        return c14;
    }

    public void setC14(boolean c14) {
        this.c14 = c14;
    }

    public boolean isC15() {
        return c15;
    }

    public void setC15(boolean c15) {
        this.c15 = c15;
    }

    public boolean isC16() {
        return c16;
    }

    public void setC16(boolean c16) {
        this.c16 = c16;
    }

    public boolean isC17() {
        return c17;
    }

    public void setC17(boolean c17) {
        this.c17 = c17;
    }

    public boolean isC18() {
        return c18;
    }

    public void setC18(boolean c18) {
        this.c18 = c18;
    }

    public boolean isC19() {
        return c19;
    }

    public void setC19(boolean c19) {
        this.c19 = c19;
    }

    public boolean isC20() {
        return c20;
    }

    public void setC20(boolean c20) {
        this.c20 = c20;
    }

    public boolean isC21() {
        return c21;
    }

    public void setC21(boolean c21) {
        this.c21 = c21;
    }

    public boolean isC22() {
        return c22;
    }

    public void setC22(boolean c22) {
        this.c22 = c22;
    }

    public boolean isC23() {
        return c23;
    }

    public void setC23(boolean c23) {
        this.c23 = c23;
    }

    public boolean isC24() {
        return c24;
    }

    public void setC24(boolean c24) {
        this.c24 = c24;
    }

    public String CZtoString() {
        return (c1 ? "A" : "") + (c2 ? "B" : "") + (c3 ? "C" : "") + (c4 ? "D" : "") + (c5 ? "E" : "") + (c6 ? "F" : "") +
                (c7 ? "G" : "") + (c8 ? "H" : "") + (c9 ? "I" : "") + (c10 ? "J" : "") + (c11 ? "K" : "") + (c12 ? "L" : "") +
                (c13 ? "M" : "") + (c14 ? "N" : "") + (c15 ? "O" : "") + (c16 ? "P" : "") + (c17 ? "Q" : "") + (c18 ? "R" : "") +
                (c19 ? "S" : "") + (c20 ? "T" : "") + (c21 ? "U" : "") + (c22 ? "V" : "") + (c23 ? "W" : "") + (c24 ? "X" : "");
    }
}
