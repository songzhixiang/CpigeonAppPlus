package com.cpigeon.app.modular.usercenter.presenter;


import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.ILoginView;
import com.cpigeon.app.modular.usercenter.model.dao.ILoginDao;
import com.cpigeon.app.modular.usercenter.model.daoimpl.LoginDaoImpl;

/**
 * Created by chenshuai on 2017/4/7.
 */

public class LoginPresenter extends BasePresenter<ILoginView, ILoginDao> {
    public LoginPresenter(ILoginView mView) {
        super(mView);
    }

    @Override
    protected ILoginDao initDao() {
        return new LoginDaoImpl();
    }

    ILoginDao.OnLoginListener onLoadCompleteListener = new ILoginDao.OnLoginListener() {

        @Override
        public void loginPreError(final ILoginDao.OperateCheck operateCheck) {
            if (operateCheck != ILoginDao.OperateCheck.None) {
                post(new CheckAttachRunnable() {
                    @Override
                    public void runAttached() {
                        if (operateCheck.getVal() == ILoginDao.OperateCheck.UsernameIsEmpty.getVal()) {
                            mView.focusEditTextLoginName();
                        } else {
                            mView.focusEditTextLoginPassword();
                        }
                        mView.showTips(operateCheck.getTip(), IView.TipType.DialogError);
                    }
                });
                return;
            }
            post(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                    mView.showTips("登录中...", IView.TipType.LoadingShow);
                }
            });
        }

        @Override
        public void loginSuccess() {
            postDelayed(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                    mView.showTips("", IView.TipType.LoadingHide);
                    mView.loginSuccess();
                }
            }, 1000);
        }

        @Override
        public void loginFailed(final String msg) {
            post(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                    mView.showTips("", IView.TipType.LoadingHide);
                    mView.showTips(msg, IView.TipType.DialogError);
                }
            });
        }
    };

    ILoginDao.OnLoadUserHeadImageListener onLoadUserHeadImageListener = new ILoginDao.OnLoadUserHeadImageListener() {
        @Override
        public void onSuccess(final String url) {
            post(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                    mView.showUserHeadImg(url);
                }
            });
        }

        @Override
        public void onError(String msg) {
            post(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                    mView.showUserHeadImg("");
                }
            });
        }
    };

    public void login() {
        mDao.login(mView.getLoginName(), mView.getLoginPassword(), onLoadCompleteListener);
    }

    public void loadUserHeadImgURL() {
        mDao.loadUserHeadImg(mView.getLoginName(), onLoadUserHeadImageListener);
    }
}
