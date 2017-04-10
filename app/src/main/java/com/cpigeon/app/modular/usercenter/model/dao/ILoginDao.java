package com.cpigeon.app.modular.usercenter.model.dao;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;

/**
 * Created by chenshuai on 2017/4/7.
 */

public interface ILoginDao extends IBaseDao {

    enum OperateCheck {
        UsernameIsEmpty(1, "用户名不能为空"),
        PasswordIsEmpty(2, "密码不能为空"),
        None(0, "");

        OperateCheck(int v, String tip) {
            this.val = v;
            this.tip = tip;
        }

        int val;

        public String getTip() {
            return tip;
        }

        String tip;

        public int getVal() {
            return val;
        }
    }

    interface OnLoginListener {
        void loginPreError(OperateCheck operateCheck);

        void loginSuccess();

        void loginFailed(String msg);
    }
    void login(String username, String password, OnLoginListener onLoginListener);

    interface OnLoadUserHeadImageListener{
        void onSuccess(String url);
        void onError(String msg);
    }
    void loadUserHeadImg(String username, OnLoadUserHeadImageListener onLoadUserHeadImageListener);

}
