package com.cpigeon.app.modular.usercenter.model.daoimpl;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.usercenter.model.dao.ISetUserPwdDao;
import com.cpigeon.app.utils.CallAPI;

import java.net.ConnectException;

/**
 * Created by chenshuai on 2017/4/13.
 */

public class SetUserPwdDaoImpl implements ISetUserPwdDao {
    @Override
    public void setUserPwd(String oldPwd, String newPwd, @NonNull final OnCompleteListener<Boolean> onCompleteListener) {

        CallAPI.setUserPwd(MyApp.getInstance(), oldPwd, newPwd, new CallAPI.Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                onCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                String msg = "操作超时，请稍候再试";
                if (errorType == ERROR_TYPE_API_RETURN) {
                    switch ((int) data) {
                        case -1:
                        case -2:
                            msg = "操作超时";
                            break;
                        case 20000:
                            msg = "新密码至少6位";
                            break;
                        case 20001:
                            msg = "原密码错误";
                            break;
                    }

                } else if (errorType == ERROR_TYPE_REQUST_EXCEPTION) {
                    if (data instanceof ConnectException)
                        msg = "网络无法连接，请检查您的网络";
                }
                onCompleteListener.onFail(msg);
            }
        });
    }
}
