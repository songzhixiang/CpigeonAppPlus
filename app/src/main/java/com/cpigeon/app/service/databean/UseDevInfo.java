package com.cpigeon.app.service.databean;

/**
 * Created by chenshuai on 2017/4/28.
 */


import java.io.Serializable;

/**
 * 使用设备信息
 */
public class UseDevInfo implements Serializable {

    /**
     * type : Android
     * time : 2017-04-18 14:22:19
     * devinfo : BLT-10
     */

    private String type;
    private String time;
    private String devinfo;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDevinfo() {
        return devinfo;
    }

    public void setDevinfo(String devinfo) {
        this.devinfo = devinfo;
    }

    public String getString() {
        return "UseDevInfo{" +
                "type='" + type + '\'' +
                ", time='" + time + '\'' +
                ", devinfo='" + devinfo + '\'' +
                '}';
    }
}