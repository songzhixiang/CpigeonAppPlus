package com.cpigeon.app.modular.usercenter.model.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/18.
 */
@Table(name = "userscore")
public class UserScore implements Serializable {
    @Column(name = "id", isId = true, autoGen = false)
    private int id;
    @Column(name = "userid")
    private int userid;
    @Column(name = "score")
    private int score;
    @Column(name = "item")
    private String item;
    @Column(name = "time")
    private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
