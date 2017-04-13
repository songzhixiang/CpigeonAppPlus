package com.cpigeon.app.modular.usercenter.view.activity.viewdao;

import com.cpigeon.app.commonstandard.view.activity.IView;

/**
 * Created by chenshuai on 2017/4/13.
 */

public interface ISetUserPwdView extends IView {
    int TAG_SetUserPwdSuccessAndRunLogin = 2332;

    String getOldPwd();

    String getNewPwd();

}
