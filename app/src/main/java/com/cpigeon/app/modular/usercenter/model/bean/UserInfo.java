package com.cpigeon.app.modular.usercenter.model.bean;

/**
 * Created by Administrator on 2017/3/7.
 */

public class UserInfo {

    /**
     * status : true
     * errorCode : 0
     * msg :
     * data : {"username":"zg18328495932","sex":"保密","brithday":null,"nickname":null,"headimg":"http://www.cpigeon.com/Content/faces/zgw_avatar1_2016111611131071M5ZFASO8.jpg","signs":null}
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
         * sex : 保密
         * brithday : null
         * nickname : null
         * headimg : http://www.cpigeon.com/Content/faces/zgw_avatar1_2016111611131071M5ZFASO8.jpg
         * signs : null
         */

        private String username;
        private String sex;
        private String brithday;
        private String nickname;
        private String headimg;
        private String signs;

        public DataBean() {
        }

        public DataBean(String username, String sex, String brithday, String nickname, String headimg, String signs) {
            this.username = username;
            this.sex = sex;
            this.brithday = brithday;
            this.nickname = nickname;
            this.headimg = headimg;
            this.signs = signs;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getBrithday() {
            return brithday;
        }

        public void setBrithday(String brithday) {
            this.brithday = brithday;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getSigns() {
            return signs;
        }

        public void setSigns(String signs) {
            this.signs = signs;
        }
    }
}
