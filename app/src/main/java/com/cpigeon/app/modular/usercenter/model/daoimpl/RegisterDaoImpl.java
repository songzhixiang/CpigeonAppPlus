package com.cpigeon.app.modular.usercenter.model.daoimpl;


import com.cpigeon.app.commonstandard.model.daoimpl.SendVerificationCodeImpl;
import com.cpigeon.app.modular.usercenter.model.dao.IRegisterDao;
import com.cpigeon.app.utils.CallAPI;


import java.net.ConnectException;

/**
 * Created by chenshuai on 2017/4/8.
 */

public class RegisterDaoImpl extends SendVerificationCodeImpl implements IRegisterDao {

    CallAPI.Callback registUserCallback = new CallAPI.Callback() {
        @Override
        public void onSuccess(Object data) {
            if (onCompleteListener != null)
                onCompleteListener.onSuccess(data);
        }

        @Override
        public void onError(int errorType, Object data) {
            if (onCompleteListener == null) return;
            String msg = "注册失败，请稍候再试";
            if (errorType == ERROR_TYPE_API_RETURN) {
                switch ((int) data) {
                    case 1000:
                        msg = "手机号码，验证码，或密码不能为空";
                        break;
                    case 1002:
                        msg = "该手机号已被注册";
                        break;
                    case 1003:
                        msg = "验证码失效";
                }
            } else if (errorType == ERROR_TYPE_REQUST_EXCEPTION) {
                if (data instanceof ConnectException)
                    msg = "网络无法连接，请检查您的网络";
            }
            onCompleteListener.onFail(msg);
        }
    };

    CallAPI.Callback resetUserPasswordCallback = new CallAPI.Callback() {
        @Override
        public void onSuccess(Object data) {
            if (onCompleteListener != null)
                onCompleteListener.onSuccess(data);
        }

        @Override
        public void onError(int errorType, Object data) {
            if (onCompleteListener == null) return;
            String msg = "重置失败，请稍候再试";
            if (errorType == ERROR_TYPE_API_RETURN) {
                switch ((int) data) {
                    case 1000:
                        msg = "手机号码，验证码，或密码不能为空";
                        break;
                    case 1002:
                        msg = "没有查到与此手机号码绑定的账户";
                        break;
                    case 1003:
                        msg = "验证码失效";
                        break;
                }
            } else if (errorType == ERROR_TYPE_REQUST_EXCEPTION) {
                if (data instanceof ConnectException)
                    msg = "网络无法连接，请检查您的网络";
            }
            onCompleteListener.onFail(msg);
        }
    };
    IRegisterDao.OnCompleteListener onCompleteListener;

    @Override
    public void registUser(String phoneNumber, String password, String yzm, OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
        CallAPI.registUser(phoneNumber, password, yzm, registUserCallback);
    }

    @Override
    public void findUserPassword(String phoneNumber, String password, String yzm, OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
        CallAPI.findUserPassword(phoneNumber, password, yzm, resetUserPasswordCallback);
    }

}
