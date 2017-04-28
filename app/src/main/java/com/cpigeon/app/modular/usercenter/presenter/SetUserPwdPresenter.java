package com.cpigeon.app.modular.usercenter.presenter;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;

import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.usercenter.model.dao.ISetUserPwdDao;
import com.cpigeon.app.modular.usercenter.model.daoimpl.SetUserPwdDaoImpl;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.ISetUserPwdView;

/**
 * Created by chenshuai on 2017/4/13.
 */

public class SetUserPwdPresenter extends BasePresenter<ISetUserPwdView, ISetUserPwdDao> {
    private IBaseDao.OnCompleteListener<Boolean> onCompleteListener = new IBaseDao.OnCompleteListener<Boolean>() {
        @Override
        public void onSuccess(Boolean data) {
            postDelayed(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                    mView.showTips(null, IView.TipType.LoadingHide);
                    mView.showTips("修改成功，请使用新密码登录", IView.TipType.DialogSuccess, mView.TAG_SetUserPwdSuccessAndRunLogin);
                }
            }, 300);
        }

        @Override
        public void onFail(final String msg) {
            postDelayed(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                    mView.showTips(null, IView.TipType.LoadingHide);
                    mView.showTips(msg, IView.TipType.DialogError);
                }
            }, 300);
        }
    };

    public SetUserPwdPresenter(ISetUserPwdView mView) {
        super(mView);

    }

    @Override
    protected ISetUserPwdDao initDao() {
        return new SetUserPwdDaoImpl();
    }

    public void setUserPwd() {
        mView.showTips("修改中，请稍后...", IView.TipType.LoadingShow);
        mDao.setUserPwd(mView.getOldPwd(), mView.getNewPwd(), onCompleteListener);
    }
}
