package com.cpigeon.app.modular.cpigeongroup.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/27.
 */

public class ShieldMessage implements Serializable {

    /**
     * status : true
     * errorCode : 0
     * msg :
     * data : [{"msgInfo":{"time":"2017-03-25 12:00:03","loabs":"","uid":14165,"lo":"","st":0,"fn":0,"msg":"push test","tid":0,"mid":8132,"userinfo":{"username":"zg18328495932","uid":14165,"headimgurl":"http://www.cpigeon.com/Content/faces/20170310142202.png","nickname":"Faded"},"from":"手机客户端"},"time":"2017-03-27 11:03:00"},{"msgInfo":{"time":"2017-03-23 14:49:28","loabs":"","uid":16952,"lo":"","st":0,"fn":0,"msg":"发布图片","tid":0,"mid":8125,"userinfo":{"username":"zg18380449953","uid":16952,"headimgurl":"http://www.cpigeon.com/Content/faces/20170313175442.jpg","nickname":"SweetCandy3"},"from":"手机客户端"},"time":"2017-03-27 11:03:05"},{"msgInfo":{"time":"2017-03-23 14:48:49","loabs":"","uid":16952,"lo":"","st":0,"fn":0,"msg":"发布图片","tid":0,"mid":8124,"userinfo":{"username":"zg18380449953","uid":16952,"headimgurl":"http://www.cpigeon.com/Content/faces/20170313175442.jpg","nickname":"SweetCandy3"},"from":"手机客户端"},"time":"2017-03-27 11:03:08"}]
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
         * msgInfo : {"time":"2017-03-25 12:00:03","loabs":"","uid":14165,"lo":"","st":0,"fn":0,"msg":"push test","tid":0,"mid":8132,"userinfo":{"username":"zg18328495932","uid":14165,"headimgurl":"http://www.cpigeon.com/Content/faces/20170310142202.png","nickname":"Faded"},"from":"手机客户端"}
         * time : 2017-03-27 11:03:00
         */

        private MsgInfoBean msgInfo;
        private String time;

        public MsgInfoBean getMsgInfo() {
            return msgInfo;
        }

        public void setMsgInfo(MsgInfoBean msgInfo) {
            this.msgInfo = msgInfo;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public static class MsgInfoBean {
            /**
             * time : 2017-03-25 12:00:03
             * loabs :
             * uid : 14165
             * lo :
             * st : 0
             * fn : 0
             * msg : push test
             * tid : 0
             * mid : 8132
             * userinfo : {"username":"zg18328495932","uid":14165,"headimgurl":"http://www.cpigeon.com/Content/faces/20170310142202.png","nickname":"Faded"}
             * from : 手机客户端
             */

            private String time;
            private String loabs;
            private int uid;
            private String lo;
            private int st;
            private int fn;
            private String msg;
            private int tid;
            private int mid;
            private UserinfoBean userinfo;
            private String from;

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getLoabs() {
                return loabs;
            }

            public void setLoabs(String loabs) {
                this.loabs = loabs;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getLo() {
                return lo;
            }

            public void setLo(String lo) {
                this.lo = lo;
            }

            public int getSt() {
                return st;
            }

            public void setSt(int st) {
                this.st = st;
            }

            public int getFn() {
                return fn;
            }

            public void setFn(int fn) {
                this.fn = fn;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public int getTid() {
                return tid;
            }

            public void setTid(int tid) {
                this.tid = tid;
            }

            public int getMid() {
                return mid;
            }

            public void setMid(int mid) {
                this.mid = mid;
            }

            public UserinfoBean getUserinfo() {
                return userinfo;
            }

            public void setUserinfo(UserinfoBean userinfo) {
                this.userinfo = userinfo;
            }

            public String getFrom() {
                return from;
            }

            public void setFrom(String from) {
                this.from = from;
            }

            public static class UserinfoBean {
                /**
                 * username : zg18328495932
                 * uid : 14165
                 * headimgurl : http://www.cpigeon.com/Content/faces/20170310142202.png
                 * nickname : Faded
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
}
