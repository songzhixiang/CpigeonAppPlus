package com.cpigeon.app.modular.usercenter.model.dao;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.usercenter.model.bean.UserInfo;

/**
 * Created by chenshuai on 2017/4/11.
 */

public interface IUserInfoDao extends IBaseDao {
    interface OnLoadCompleteListener {
        void onSuccess(UserInfo.DataBean userinfo);

        void onError(String msg);
    }

    void loadUserInfo(OnLoadCompleteListener onLoadCompleteListener);
}
