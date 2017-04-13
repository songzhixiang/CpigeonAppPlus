package com.cpigeon.app.modular.usercenter.model.daoimpl;

import android.support.annotation.NonNull;
import android.view.View;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.model.daoimpl.SendVerificationCodeAndGetUserBandPhoneImpl;
import com.cpigeon.app.modular.usercenter.model.dao.ISetPayPwdDao;
import com.cpigeon.app.utils.CallAPI;
import com.orhanobut.logger.Logger;

/**
 * Created by chenshuai on 2017/4/13.
 */

public class SetPayPwdDaoImpl extends SendVerificationCodeAndGetUserBandPhoneImpl implements ISetPayPwdDao {

    @Override
    public void setUserPayPwd(String yzm, String payPwd, String userBandPhone, @NonNull final IBaseDao.OnCompleteListener<Boolean> onCompleteListener) {
        CallAPI.setUserPayPwd(MyApp.getInstance(), yzm, payPwd, userBandPhone, new CallAPI.Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                onCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                Logger.i(String.valueOf(errorType));
                String msg = "设置失败，请稍后再试";
                switch (errorType) {
                    case CallAPI.Callback.ERROR_TYPE_API_RETURN:
                        switch ((int) data) {
                            case 20001://支付密码解析错误
                                break;
                            case 20002:
                                //支付密码长度不在限制范围内
                                msg = "请设置6~12位的支付密码";
                                break;
                            case 20003:
                                msg = "验证码验证失败";
                                break;
                            case 20004:
                                msg = "手机号码未绑定";
                                break;
                        }
                }
                onCompleteListener.onFail(msg);
            }
        });
    }
}
