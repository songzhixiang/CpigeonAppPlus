package com.cpigeon.app.commonstandard.model;

import com.cpigeon.app.utils.CallAPI;

/**
 * Created by chenshuai on 2017/4/8.
 * 发送验证码接口
 */

public interface ISendVerificationCode {
    void sendYZM(String phoneNumber, int yzmType, OnSendCompleteListener onSendCompleteListener);

    interface OnSendCompleteListener {
        void onSuccess(String yzmMd5);

        void onFail(int errorCode, String msg);
    }
}
