package com.cpigeon.app.modular.usercenter.model.dao;

import android.support.annotation.NonNull;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.model.dao.IGetUserBandPhone;
import com.cpigeon.app.commonstandard.model.dao.ISendVerificationCode;

/**
 * Created by chenshuai on 2017/4/13.
 */

public interface ISetPayPwdDao extends IBaseDao, ISendVerificationCode, IGetUserBandPhone {

    /**
     * 设置支付密码
     *
     * @param yzm                手机验证码
     * @param payPwd             支付密码
     * @param userBandPhone      用户绑定手机号码
     * @param onCompleteListener
     */
    void setUserPayPwd(String yzm, String payPwd, String userBandPhone, @NonNull IBaseDao.OnCompleteListener<Boolean> onCompleteListener);
}
