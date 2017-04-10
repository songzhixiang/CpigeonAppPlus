package com.cpigeon.app.modular.cpigeongroup.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/23.
 */

public class MyFoucs {

    /**
     * status : true
     * errorCode : 0
     * msg :
     * data : [{"userinfo":{"username":"zgtfluu","uid":14051,"headimgurl":"http://www.cpigeon.com/Content/faces/zgw_avatar1_2016071501283066AA8ABAU9.jpg","nickname":"德立鸽舍"},"time":"2017-03-20 15:33:04"},{"userinfo":{"username":"pigeonof","uid":5473,"headimgurl":"http://www.cpigeon.com/Content/faces/zgw_avatar1_2016012505492613O20DYJWB.jpg","nickname":"中鸽网"},"time":"2017-03-22 15:02:28"}]
     */

    private boolean status;
    private int errorCode;
    private String msg;
    private List<DataBean> data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * userinfo : {"username":"zgtfluu","uid":14051,"headimgurl":"http://www.cpigeon.com/Content/faces/zgw_avatar1_2016071501283066AA8ABAU9.jpg","nickname":"德立鸽舍"}
         * time : 2017-03-20 15:33:04
         */

        private UserinfoBean userinfo;
        private String time;

        public UserinfoBean getUserinfo() {
            return userinfo;
        }

        public void setUserinfo(UserinfoBean userinfo) {
            this.userinfo = userinfo;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public static class UserinfoBean {
            /**
             * username : zgtfluu
             * uid : 14051
             * headimgurl : http://www.cpigeon.com/Content/faces/zgw_avatar1_2016071501283066AA8ABAU9.jpg
             * nickname : 德立鸽舍
             */

            private String username;
            private int uid;
            private String headimgurl;
            private String nickname;

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getHeadimgurl() {
                return headimgurl;
            }

            public void setHeadimgurl(String headimgurl) {
                this.headimgurl = headimgurl;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }
        }
    }
}
