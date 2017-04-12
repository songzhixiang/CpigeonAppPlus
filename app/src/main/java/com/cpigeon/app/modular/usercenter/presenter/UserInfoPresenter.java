package com.cpigeon.app.modular.usercenter.presenter;

import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.usercenter.model.bean.UserInfo;
import com.cpigeon.app.modular.usercenter.model.dao.IUserInfoDao;
import com.cpigeon.app.modular.usercenter.model.daoimpl.UserInfoDaoImpl;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.IUserInfoView;
import com.cpigeon.app.utils.CpigeonData;

/**
 * Created by chenshuai on 2017/4/11.
 */

public class UserInfoPresenter extends BasePresenter<IUserInfoView, IUserInfoDao> {
    private IUserInfoDao.OnLoadCompleteListener onLoadCompleteListener = new IUserInfoDao.OnLoadCompleteListener() {
        @Override
        public void onSuccess(final UserInfo.DataBean userinfo) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mView.showUserinfo(userinfo);
                    mView.showTips("", IView.TipType.LoadingHide);
                }
            });
        }

        @Override
        public void onError(String msg) {
            CpigeonData.getInstance().setUserInfo(null);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mView.showTips("", IView.TipType.LoadingHide);
                    mView.showTips("加载失败", IView.TipType.DialogError);
                }
            });
        }
    };

    public UserInfoPresenter(IUserInfoView mView) {
        super(mView);
        mModel = new UserInfoDaoImpl();
    }

    public void loadUserInfo() {
        mView.showTips("加载信息中...", IView.TipType.LoadingShow);
        mModel.loadUserInfo(onLoadCompleteListener);
    }
}
