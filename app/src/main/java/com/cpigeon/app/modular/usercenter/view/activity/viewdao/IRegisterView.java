package com.cpigeon.app.modular.usercenter.view.activity.viewdao;

import com.cpigeon.app.commonstandard.view.activity.IView;

/**
 * Created by chenshuai on 2017/4/8.
 */

public interface IRegisterView extends IView {


    /**
     * 获取运行模式（注册，重置密码）
     *
     * @return
     */
    int getRunModel();

    /**
     * 下一步
     */
    boolean nextStep();

    /**
     * 注册完成
     */
    void complete();

    interface IRegisterSetp1View extends IView {


        /**
         * 获取手机号码
         *
         * @return
         */
        String getPhoneNumber();

        /**
         * 是否同意注册协议
         *
         * @return
         */
        boolean isAgreeProtocol();

        /**
         * 聚焦手机号码输入框
         */
        void focusInputPhoneNumber();

        /**
         * 设置下一步的按钮启用
         *
         * @param enable
         */
        void setBtnNextEnable(boolean enable);
    }

    interface IRegisterSetp2View extends IView {

        /**
         * 获取输入的验证码
         *
         * @return
         */
        String getInputYZM();

        /**
         * 获取从接口返回的验证码MD5值
         *
         * @return
         */
        String getYzmMd5();

        /**
         * 获取验证码发送次数
         *
         * @return
         */
        int getSendTimes();

        /**
         * 聚焦验证码输入框
         */
        void focusInputYZM();

        void sendYZMSuccess(String phone, String yzmMd5);

        void sendYZMFail(String msg);

        void runSendTimer();

        void stopSendTimer();
    }

    interface IRegisterSetp3View extends IView {


        /**
         * 获取密码
         *
         * @return
         */
        String getPassword();

        /**
         * 获取确认密码
         *
         * @return
         */
        String getConfirmPassword();

        /**
         * 聚焦密码输入框
         */
        void focusInputPassword();

        /**
         * 聚焦确认密码输入框
         */
        void focusInputConfirmPassword();
    }

}
