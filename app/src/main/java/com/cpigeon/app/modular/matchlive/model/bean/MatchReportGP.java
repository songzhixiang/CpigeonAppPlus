package com.cpigeon.app.modular.matchlive.model.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 公棚报到数据
 * Created by Administrator on 2016/11/7.
 */
@Table(name = "matchreport_gp")
public class MatchReportGP implements Serializable {
    @Column(name = "id", isId = true, autoGen = false)
    private long id;
    @Column(name = "ssid",property = "NOT NULL CONSTRAINT fk_matchreport_gp_ssid REFERENCES matchInfo(\"ssid\") ON DELETE CASCADE ON UPDATE CASCADE")
    private String ssid;
    @Column(name = "mc")
    private int mc;
    @Column(name = "name")
    private String name;
    @Column(name = "foot")
    private String foot;
    @Column(name = "arrive")
    private String arrive;
    @Column(name = "speed")
    private Double speed;
    @Column(name = "area")
    private String area;
    @Column(name = "sex")
    private String sex;
    @Column(name = "color")
    private String color;
    @Column(name = "eye")
    private String eye;

    @Column(name = "ttzb")
    private String ttzb;
    @Column(name = "ring")
    private String ring;

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

    @Column(name = "cr1")
    private int cr1;
    @Column(name = "cr2")
    private int cr2;
    @Column(name = "cr3")
    private int cr3;
    @Column(name = "cr4")
    private int cr4;
    @Column(name = "cr5")
    private int cr5;
    @Column(name = "cr6")
    private int cr6;
    @Column(name = "cr7")
    private int cr7;
    @Column(name = "cr8")
    private int cr8;
    @Column(name = "cr9")
    private int cr9;
    @Column(name = "cr10")
    private int cr10;
    @Column(name = "cr11")
    private int cr11;
    @Column(name = "cr12")
    private int cr12;
    @Column(name = "cr13")
    private int cr13;
    @Column(name = "cr14")
    private int cr14;
    @Column(name = "cr15")
    private int cr15;
    @Column(name = "cr16")
    private int cr16;
    @Column(name = "cr17")
    private int cr17;
    @Column(name = "cr18")
    private int cr18;
    @Column(name = "cr19")
    private int cr19;
    @Column(name = "cr20")
    private int cr20;
    @Column(name = "cr21")
    private int cr21;
    @Column(name = "cr22")
    private int cr22;
    @Column(name = "cr23")
    private int cr23;
    @Column(name = "cr24")
    private int cr24;

    public MatchReportGP() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public int getMc() {
        return mc;
    }

    public void setMc(int mc) {
        this.mc = mc;
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

    public String getArrive() {
        return arrive;
    }

    public void setArrive(String arrive) {
        this.arrive = arrive;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEye() {
        return eye;
    }

    public void setEye(String eye) {
        this.eye = eye;
    }

    public String getTtzb() {
        return ttzb;
    }

    public void setTtzb(String ttzb) {
        this.ttzb = ttzb;
    }

    public String getRing() {
        return ring;
    }

    public void setRing(String ring) {
        this.ring = ring;
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

    public int getCr1() {
        return cr1;
    }

    public void setCr1(int cr1) {
        this.cr1 = cr1;
    }

    public int getCr2() {
        return cr2;
    }

    public void setCr2(int cr2) {
        this.cr2 = cr2;
    }

    public int getCr3() {
        return cr3;
    }

    public void setCr3(int cr3) {
        this.cr3 = cr3;
    }

    public int getCr4() {
        return cr4;
    }

    public void setCr4(int cr4) {
        this.cr4 = cr4;
    }

    public int getCr5() {
        return cr5;
    }

    public void setCr5(int cr5) {
        this.cr5 = cr5;
    }

    public int getCr6() {
        return cr6;
    }

    public void setCr6(int cr6) {
        this.cr6 = cr6;
    }

    public int getCr7() {
        return cr7;
    }

    public void setCr7(int cr7) {
        this.cr7 = cr7;
    }

    public int getCr8() {
        return cr8;
    }

    public void setCr8(int cr8) {
        this.cr8 = cr8;
    }

    public int getCr9() {
        return cr9;
    }

    public void setCr9(int cr9) {
        this.cr9 = cr9;
    }

    public int getCr10() {
        return cr10;
    }

    public void setCr10(int cr10) {
        this.cr10 = cr10;
    }

    public int getCr11() {
        return cr11;
    }

    public void setCr11(int cr11) {
        this.cr11 = cr11;
    }

    public int getCr12() {
        return cr12;
    }

    public void setCr12(int cr12) {
        this.cr12 = cr12;
    }

    public int getCr13() {
        return cr13;
    }

    public void setCr13(int cr13) {
        this.cr13 = cr13;
    }

    public int getCr14() {
        return cr14;
    }

    public void setCr14(int cr14) {
        this.cr14 = cr14;
    }

    public int getCr15() {
        return cr15;
    }

    public void setCr15(int cr15) {
        this.cr15 = cr15;
    }

    public int getCr16() {
        return cr16;
    }

    public void setCr16(int cr16) {
        this.cr16 = cr16;
    }

    public int getCr17() {
        return cr17;
    }

    public void setCr17(int cr17) {
        this.cr17 = cr17;
    }

    public int getCr18() {
        return cr18;
    }

    public void setCr18(int cr18) {
        this.cr18 = cr18;
    }

    public int getCr19() {
        return cr19;
    }

    public void setCr19(int cr19) {
        this.cr19 = cr19;
    }

    public int getCr20() {
        return cr20;
    }

    public void setCr20(int cr20) {
        this.cr20 = cr20;
    }

    public int getCr21() {
        return cr21;
    }

    public void setCr21(int cr21) {
        this.cr21 = cr21;
    }

    public int getCr22() {
        return cr22;
    }

    public void setCr22(int cr22) {
        this.cr22 = cr22;
    }

    public int getCr23() {
        return cr23;
    }

    public void setCr23(int cr23) {
        this.cr23 = cr23;
    }

    public int getCr24() {
        return cr24;
    }

    public void setCr24(int cr24) {
        this.cr24 = cr24;
    }

    public String CZtoString() {
//        return String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s",
//                c1 ? "A" : "", c2 ? "B" : "", c3 ? "C" : "", c4 ? "D" : "", c5 ? "E" : "", c6 ? "F" : "",
//                c7 ? "G" : "", c8 ? "H" : "", c9 ? "I" : "", c10 ? "J" : "", c11 ? "K" : "", c12 ? "L" : "",
//                c13 ? "M" : "", c14 ? "N" : "", c15 ? "O" : "", c16 ? "P" : "", c17 ? "Q" : "", c18 ? "R" : "",
//                c19 ? "S" : "", c20 ? "T" : "", c21 ? "U" : "", c22 ? "V" : "", c23 ? "W" : "", c24 ? "X" : "");
        StringBuilder builder = new StringBuilder();
        builder.append(c1 ? "A" + (cr1 == 0 ? "" : "-" + cr1 + " ") : "");
        builder.append(c2 ? "B" + (cr2 == 0 ? "" : "-" + cr2 + " ") : "");
        builder.append(c3 ? "C" + (cr3 == 0 ? "" : "-" + cr3 + " ") : "");
        builder.append(c4 ? "D" + (cr4 == 0 ? "" : "-" + cr4 + " ") : "");
        builder.append(c5 ? "E" + (cr5 == 0 ? "" : "-" + cr5 + " ") : "");
        builder.append(c6 ? "F" + (cr6 == 0 ? "" : "-" + cr6 + " ") : "");
        builder.append(c7 ? "G" + (cr7 == 0 ? "" : "-" + cr7 + " ") : "");
        builder.append(c8 ? "H" + (cr8 == 0 ? "" : "-" + cr8 + " ") : "");
        builder.append(c9 ? "I" + (cr9 == 0 ? "" : "-" + cr9 + " ") : "");
        builder.append(c10 ? "J" + (cr10 == 0 ? "" : "-" + cr10 + " ") : "");
        builder.append(c11 ? "K" + (cr11 == 0 ? "" : "-" + cr11 + " ") : "");
        builder.append(c12 ? "L" + (cr12 == 0 ? "" : "-" + cr12 + " ") : "");
        builder.append(c13 ? "M" + (cr13 == 0 ? "" : "-" + cr13 + " ") : "");
        builder.append(c14 ? "N" + (cr14 == 0 ? "" : "-" + cr14 + " ") : "");
        builder.append(c15 ? "O" + (cr15 == 0 ? "" : "-" + cr15 + " ") : "");
        builder.append(c16 ? "P" + (cr16 == 0 ? "" : "-" + cr16 + " ") : "");
        builder.append(c17 ? "Q" + (cr17 == 0 ? "" : "-" + cr17 + " ") : "");
        builder.append(c18 ? "R" + (cr18 == 0 ? "" : "-" + cr18 + " ") : "");
        builder.append(c19 ? "S" + (cr19 == 0 ? "" : "-" + cr19 + " ") : "");
        builder.append(c20 ? "T" + (cr20 == 0 ? "" : "-" + cr20 + " ") : "");
        builder.append(c21 ? "U" + (cr21 == 0 ? "" : "-" + cr21 + " ") : "");
        builder.append(c22 ? "V" + (cr22 == 0 ? "" : "-" + cr22 + " ") : "");
        builder.append(c23 ? "W" + (cr23 == 0 ? "" : "-" + cr23 + " ") : "");
        builder.append(c24 ? "X" + (cr24 == 0 ? "" : "-" + cr24 + " ") : "");
        System.out.println(builder.toString());
        return builder.toString();
//        return (c1 ? "A" : "") + (c2 ? "B" : "") + (c3 ? "C" : "") + (c4 ? "D" : "") + (c5 ? "E" : "") + (c6 ? "F" : "") +
//                (c7 ? "G" : "") + (c8 ? "H" : "") + (c9 ? "I" : "") + (c10 ? "J" : "") + (c11 ? "K" : "") + (c12 ? "L" : "") +
//                (c13 ? "M" : "") + (c14 ? "N" : "") + (c15 ? "O" : "") + (c16 ? "P" : "") + (c17 ? "Q" : "") + (c18 ? "R" : "") +
//                (c19 ? "S" : "") + (c20 ? "T" : "") + (c21 ? "U" : "") + (c22 ? "V" : "") + (c23 ? "W" : "") + (c24 ? "X" : "");
    }
}
