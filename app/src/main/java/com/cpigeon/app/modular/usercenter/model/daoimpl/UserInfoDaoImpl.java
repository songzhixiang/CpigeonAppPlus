package com.cpigeon.app.modular.usercenter.model.daoimpl;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.modular.usercenter.model.bean.UserInfo;
import com.cpigeon.app.modular.usercenter.model.dao.IUserCenterDao;
import com.cpigeon.app.modular.usercenter.model.dao.IUserInfoDao;
import com.cpigeon.app.utils.CallAPI;
import com.cpigeon.app.utils.CpigeonData;

/**
 * Created by chenshuai on 2017/4/11.
 */

public class UserInfoDaoImpl implements IUserInfoDao {

    @Override
    public void loadUserInfo(final OnLoadCompleteListener onLoadCompleteListener) {
        CallAPI.getBasicUserInfo(MyApp.getInstance(), new CallAPI.Callback<UserInfo.DataBean>() {
            @Override
            public void onSuccess(UserInfo.DataBean data) {
                CpigeonData.getInstance().setUserInfo(data);
                if (onLoadCompleteListener != null)
                    onLoadCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                if (onLoadCompleteListener != null)
                    onLoadCompleteListener.onError("");
            }
        });
    }
}
