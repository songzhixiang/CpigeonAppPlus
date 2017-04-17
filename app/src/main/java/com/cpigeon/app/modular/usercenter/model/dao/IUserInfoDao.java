package com.cpigeon.app.modular.usercenter.model.dao;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.usercenter.model.bean.UserInfo;

import java.io.File;

/**
 * Created by chenshuai on 2017/4/11.
 */

public interface IUserInfoDao extends IBaseDao {
    interface OnUserinfoMotifyCompleteListener {
        void onSuccess();

        void onError(String msg);
    }

    void modifyUserInfo(UserInfo.DataBean userinfo, OnUserinfoMotifyCompleteListener onUserinfoMotifyCompleteListener);

    interface OnUpdateUserFaceImageCompleteListener {
        void onSuccess(String url);

        void onError(String msg);
    }

    void updateUserFaceImage(File file, OnUpdateUserFaceImageCompleteListener onUpdateUserFaceImageCompleteListener);

}
