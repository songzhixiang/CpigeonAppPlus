package com.cpigeon.app.modular.usercenter.model.dao;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.model.dao.IGetUserBandPhone;

/**
 * Created by chenshuai on 2017/4/11.
 */

public interface IFeedBackDao extends IBaseDao, IGetUserBandPhone {
    interface OnCompleteListener {
        void onSuccess();

        void onFail(String msg);
    }

    void feedback(String content, String phoneNum, OnCompleteListener onCompleteListener);
}
