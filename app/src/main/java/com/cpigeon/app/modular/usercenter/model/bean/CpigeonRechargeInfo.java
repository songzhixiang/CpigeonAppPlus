package com.cpigeon.app.modular.usercenter.model.bean;

import java.util.List;

/**
 * Created by cpigeon on 17-2-28.
 */

public class CpigeonRechargeInfo {


    /**
     * status : true
     * errorCode : 0
     * msg :
     * data : [{"price":3.02,"id":608,"payway":"微信充值","statusname":"待充值","time":"2017-02-28 14:27:12","number":"ZGCZ2017022801705900008"}]
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
         * price : 3.02
         * id : 608
         * payway : 微信充值
         * statusname : 待充值
         * time : 2017-02-28 14:27:12
         * number : ZGCZ2017022801705900008
         */

        private double price;
        private int id;
        private String payway;
        private String statusname;
        private String time;
        private String number;

        public DataBean() {
        }

        public DataBean(double price, int id, String payway, String statusname, String time, String number) {
            this.price = price;
            this.id = id;
            this.payway = payway;
            this.statusname = statusname;
            this.time = time;
            this.number = number;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPayway() {
            return payway;
        }

        public void setPayway(String payway) {
            this.payway = payway;
        }

        public String getStatusname() {
            return statusname;
        }

        public void setStatusname(String statusname) {
            this.statusname = statusname;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }
}
