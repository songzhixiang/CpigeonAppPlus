package com.cpigeon.app.modular.cpigeongroup.model.bean;

/**
 * Created by Administrator on 2017/3/22.
 */

public class CpigeonGroupUserInfo {


    /**
     * status : true
     * errorCode : 0
     * msg :
     * data : {"username":"zg18328495932","gzCount":2,"nickname":"Faded","fsCount":0,"sign":"啦啦啦～","birth":"1996-03-08","headimgurl":"http://www.cpigeon.com/Content/faces/20170310142202.png","msgCount":3}
     */

    private boolean status;
    private int errorCode;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * username : zg18328495932
         * gzCount : 2
         * nickname : Faded
         * fsCount : 0
         * sign : 啦啦啦～
         * birth : 1996-03-08
         * headimgurl : http://www.cpigeon.com/Content/faces/20170310142202.png
         * msgCount : 3
         */

        private String username;
        private int gzCount;
        private String nickname;
        private int fsCount;
        private String sign;
        private String birth;
        private String headimgurl;
        private int msgCount;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getGzCount() {
            return gzCount;
        }

        public void setGzCount(int gzCount) {
            this.gzCount = gzCount;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getFsCount() {
            return fsCount;
        }

        public void setFsCount(int fsCount) {
            this.fsCount = fsCount;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getBirth() {
            return birth;
        }

        public void setBirth(String birth) {
            this.birth = birth;
        }

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
        }

        public int getMsgCount() {
            return msgCount;
        }

        public void setMsgCount(int msgCount) {
            this.msgCount = msgCount;
        }
    }
}
