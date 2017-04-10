package com.cpigeon.app.modular.usercenter.model.dao;


import com.cpigeon.app.commonstandard.model.IBaseDao;
import com.cpigeon.app.commonstandard.model.ISendVerificationCode;

/**
 * Created by chenshuai on 2017/4/8.
 */

public interface IRegisterDao extends IBaseDao, ISendVerificationCode {

    void registUser(String phoneNumber, String password, String yzm, OnCompleteListener onCompleteListener);

    void findUserPassword(String phoneNumber, String password, String yzm, OnCompleteListener onCompleteListener);

    interface OnCompleteListener {
        void onSuccess();

        void onFail(String msg);
    }
}
