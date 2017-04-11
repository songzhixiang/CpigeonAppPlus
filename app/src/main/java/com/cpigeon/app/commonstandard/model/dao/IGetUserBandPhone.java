package com.cpigeon.app.commonstandard.model.dao;

/**
 * Created by chenshuai on 2017/4/11.
 * 获取用户绑定手机号接口
 */

public interface IGetUserBandPhone {
    void getUserBandPhone(OnCompleteListener onCompleteListener);

    interface OnCompleteListener {
        void onSuccess(String phoneNumber, boolean isBand);

        void onFail();
    }
}
