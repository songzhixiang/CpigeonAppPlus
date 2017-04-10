package com.cpigeon.app.modular.usercenter.presenter;


import com.cpigeon.app.commonstandard.activity.IView;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.modular.usercenter.activity.viewdao.ILoginView;
import com.cpigeon.app.modular.usercenter.model.dao.ILoginDao;
import com.cpigeon.app.modular.usercenter.model.daoimpl.LoginDaoImpl;

/**
 * Created by chenshuai on 2017/4/7.
 */

public class LoginPresenter extends BasePresenter<ILoginView, ILoginDao> {
    public LoginPresenter(ILoginView mView) {
        super(mView);
        mModel = new LoginDaoImpl();
    }

    ILoginDao.OnLoginListener onLoadCompleteListener = new ILoginDao.OnLoginListener() {


        @Override
        public void loginPreError(final ILoginDao.OperateCheck operateCheck) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mView.hideLoading();
                    if (operateCheck.getVal() == ILoginDao.OperateCheck.UsernameIsEmpty.getVal()) {
                        mView.focusEditTextLoginName();
                    } else {
                        mView.focusEditTextLoginPassword();
                    }
                    mView.showTips(operateCheck.getTip(), IView.TipType.DialogError);
                }
            });
        }

        @Override
        public void loginSuccess() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mView.hideLoading();
                    mView.loginSuccess();
                }
            });
        }

        @Override
        public void loginFailed(final String msg) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mView.hideLoading();
                    mView.showTips(msg, IView.TipType.DialogError);
                }
            });
        }
    };

    ILoginDao.OnLoadUserHeadImageListener onLoadUserHeadImageListener = new ILoginDao.OnLoadUserHeadImageListener() {
        @Override
        public void onSuccess(final String url) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mView.showUserHeadImg(url);
                }
            });
        }

        @Override
        public void onError(String msg) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mView.showUserHeadImg("");
                }
            });
        }
    };

    public void login() {
        mView.showLoading();
        mModel.login(mView.getLoginName(), mView.getLoginPassword(), onLoadCompleteListener);
    }

    public void loadUserHeadImgURL() {
        mModel.loadUserHeadImg(mView.getLoginName(), onLoadUserHeadImageListener);
    }
}
