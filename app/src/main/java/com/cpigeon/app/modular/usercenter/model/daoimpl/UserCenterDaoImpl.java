package com.cpigeon.app.modular.usercenter.model.daoimpl;

import android.content.Context;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.usercenter.model.dao.IUserCenterDao;
import com.cpigeon.app.utils.CallAPI;

import java.util.Map;

/**
 * Created by Administrator on 2017/4/10.
 */

public class UserCenterDaoImpl implements IUserCenterDao {
    private Context mContext = MyApp.getInstance();

    @Override
    public void loadUserBalance(final OnCompleteListener<Map<String, Object>> listener) {
        CallAPI.getUserYuEAndJiFen(mContext, new CallAPI.Callback<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> data) {
                listener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                listener.onFail(errorType+"");
            }
        });
    }

    @Override
    public void getUserSignStatus(final OnCompleteListener<Boolean> listener) {
        CallAPI.getUserSignStatus(mContext, System.currentTimeMillis() / 1000, new CallAPI.Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                listener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                listener.onFail(errorType+"");
            }
        });
    }
}
