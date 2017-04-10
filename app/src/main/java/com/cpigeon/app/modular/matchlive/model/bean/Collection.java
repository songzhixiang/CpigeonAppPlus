package com.cpigeon.app.modular.matchlive.model.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2016/11/16.
 */

@Table(name = "collection")
public class Collection {

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "value")
    private String value;//

    @Column(name = "key")
    private String key;//关注关键字

    @Column(name = "content")
    private String content;//关注内容

    @Column(name = "type", property = " NOT NULL")
    private int type;//关注类型

    @Column(name = "iscoll")
    private boolean isCollection;

    @Column(name = "colltime")
    private long collTime;

    @Column(name = "colluserid")
    private int collUserid;

    public Collection() {
    }

    public Collection(int id, int type, String key, boolean isCollection, long collTime) {
        this.id = id;
        this.type = type;
        this.key = key;
        this.isCollection = isCollection;
        this.collTime = collTime;
    }

    public Collection(int type, String value, boolean isCollection, long collTime) {
        this.type = type;
        this.value = value;
        this.isCollection = isCollection;
        this.collTime = collTime;
    }

    public Collection(int type, String value, long collTime) {
        this.type = type;
        this.value = value;
        this.collTime = collTime;
    }

    public Collection(int type, long collTime) {
        this.type = type;
        this.collTime = collTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isCollection() {
        return isCollection;
    }

    public void setCollection(boolean collection) {
        isCollection = collection;
    }

    public long getCollTime() {
        return collTime;
    }

    public void setCollTime(long collTime) {
        this.collTime = collTime;
    }

    public int getCollUserid() {
        return collUserid;
    }

    public void setCollUserid(int collUserid) {
        this.collUserid = collUserid;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", key='" + key + '\'' +
                ", type=" + type +
                ", isCollection=" + isCollection +
                ", collTime=" + collTime +
                '}';
    }

    public enum CollectionType {
        NEWS(1),//新闻
        RACE(2),//比赛
        ORG(3);//组织(协会 公棚)

        private final int value;

        CollectionType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

}
