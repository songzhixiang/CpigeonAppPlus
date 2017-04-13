package com.cpigeon.app.modular.usercenter.view.activity.viewdao;

import com.cpigeon.app.commonstandard.view.activity.IView;

/**
 * Created by chenshuai on 2017/4/13.
 */

public interface ISetPayPwdView extends IView {

    int TAG_UnBandPhone = 32;
    int TAG_SetPayPwdSuccess = 323;
    int TAG_YZMError = 333;

    int getBandPhoneRetryTimes();

    String getPayPwd();

    String getInputYZM();

    void sendYzmSuccess(String yzmMd5);

}
