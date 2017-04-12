package com.cpigeon.app.modular.usercenter.view.activity.viewdao;

import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.usercenter.model.bean.UserInfo;

/**
 * Created by chenshuai on 2017/4/11.
 */

public interface IUserInfoView extends IView {

    void showUserinfo(UserInfo.DataBean userinfo);

    boolean hasChangedUserInfo();

    boolean hasChangedUserHeadImage();

    String getChangedUserHeadImageLocalPath();

    UserInfo.DataBean getModifiedUserInfo();
}
