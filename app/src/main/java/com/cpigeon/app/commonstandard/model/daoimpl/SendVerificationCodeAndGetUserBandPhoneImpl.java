package com.cpigeon.app.commonstandard.model.daoimpl;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.dao.IGetUserBandPhone;
import com.cpigeon.app.commonstandard.model.dao.ISendVerificationCode;
import com.cpigeon.app.utils.CallAPI;
import com.cpigeon.app.utils.CpigeonData;

import java.net.ConnectException;
import java.util.Map;

/**
 * Created by chenshuai on 2017/4/13.
 */

public class SendVerificationCodeAndGetUserBandPhoneImpl implements ISendVerificationCode, IGetUserBandPhone {
    @Override
    public void getUserBandPhone(final OnCompleteListener onCompleteListener) {
        CallAPI.getUserBandPhone(MyApp.getInstance(), new CallAPI.Callback<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> data) {
                String userPhone = (String) data.get("phone");
                if ((int) data.get("band") == 1) {
                    CpigeonData.getInstance().setUserBindPhone(userPhone);
                }
                if (onCompleteListener != null)
                    onCompleteListener.onSuccess(userPhone, (int) data.get("band") == 1);
            }

            @Override
            public void onError(int errorType, Object data) {
                if (onCompleteListener != null)
                    onCompleteListener.onFail();
            }
        });
    }


    CallAPI.Callback<String> callback = new CallAPI.Callback<String>() {
        @Override
        public void onSuccess(String data) {
            if (onSendCompleteListener != null)
                onSendCompleteListener.onSuccess(data);
        }

        @Override
        public void onError(int errorType, Object data) {
            if (onSendCompleteListener == null)
                return;
            String msg = "发送失败，请稍候再试";
            int errorcode = 0;
            if (errorType == ERROR_TYPE_API_RETURN) {
                switch ((int) data) {
                    case -1:
                    case -2:
                        msg = "操作超时";
                        break;
                    case 1000:
                        msg = "手机号不能为空";
                        break;
                    case 1003:
                        msg = "手机号已被注册";
                        break;
                    case 1004:
                        msg = "手机号格式不正确";
                        break;
                    case 1005:
                        msg = "同一手机号每天最多获取两次";
                        break;
                    case 1008:
                        msg = "该手机号码未绑定账户";
                        break;
                    case 1009:
                        msg = "验证码已发送";
                        break;
                }
                errorcode = (int) data;
            } else if (errorType == ERROR_TYPE_REQUST_EXCEPTION) {
                if (data instanceof ConnectException)
                    msg = "网络无法连接，请检查您的网络";
            }
            onSendCompleteListener.onFail(errorcode, msg);
        }
    };

    OnSendCompleteListener onSendCompleteListener;

    @Override
    public void sendYZM(String phoneNumber, CallAPI.DATATYPE.YZM yzmType, OnSendCompleteListener onSendCompleteListener) {
        this.onSendCompleteListener = onSendCompleteListener;
        CallAPI.sendYZM(yzmType, phoneNumber, callback);
    }
}
