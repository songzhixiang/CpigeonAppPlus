package com.cpigeon.app.modular.usercenter.view.activity.viewdao;

import com.cpigeon.app.commonstandard.view.activity.IView;

/**
 * Created by chenshuai on 2017/4/7.
 */

public interface ILoginView extends IView {
    /**
     * 获取用户输入的登录名
     * @return
     */
    String getLoginName();

    /**
     * 获取用户输入的密码
     * @return
     */
    String getLoginPassword();

    /**
     * 聚焦登录名输入框
     */
    void focusEditTextLoginName();

    /**
     * 聚焦密码输入框
     */
    void focusEditTextLoginPassword();

    /**
     * 登录成功时的界面处理
     */
    void loginSuccess();

    /**
     * 显示用户头像
     * @param imgurl
     */
    void showUserHeadImg(String imgurl);
}
