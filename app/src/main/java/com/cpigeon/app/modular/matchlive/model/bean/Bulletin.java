package com.cpigeon.app.modular.matchlive.model.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 公告类
 * Created by Administrator on 2016/11/16.
 */

@Table(name = "bulletin")
public class Bulletin implements Serializable {


    @Column(name = "ssid",isId = true,property = "NOT NULL CONSTRAINT fk_bulletin_ssid REFERENCES matchInfo(\"ssid\") ON DELETE CASCADE ON UPDATE CASCADE")
    private String ssid;//比赛ID

    @Column(name = "content")
    private String content;

    @Column(name = "date")
    private String date;

    public Bulletin() {
    }

    public Bulletin(String ssid, String content, String date) {
        this.ssid = ssid;
        this.content = content;
        this.date = date;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Bulletin{" +
                "ssid='" + ssid + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
