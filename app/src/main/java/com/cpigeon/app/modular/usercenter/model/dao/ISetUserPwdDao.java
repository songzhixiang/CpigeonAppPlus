package com.cpigeon.app.modular.usercenter.model.dao;

import android.support.annotation.NonNull;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;

/**
 * Created by chenshuai on 2017/4/13.
 */

public interface ISetUserPwdDao extends IBaseDao {

    void setUserPwd(String oldPwd, String newPwd, @NonNull OnCompleteListener<Boolean> onCompleteListener);
}
