package com.cpigeon.app.modular.usercenter.presenter;

import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.usercenter.model.bean.UserInfo;
import com.cpigeon.app.modular.usercenter.model.dao.IUserInfoDao;
import com.cpigeon.app.modular.usercenter.model.daoimpl.UserInfoDaoImpl;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.IUserInfoView;
import com.cpigeon.app.utils.CpigeonData;

import java.io.File;

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
    private IUserInfoDao.OnUpdateUserFaceImageCompleteListener onUpdateUserFaceImageCompleteListener = new IUserInfoDao.OnUpdateUserFaceImageCompleteListener() {
        @Override
        public void onSuccess(String url) {
            if (mView == null) return;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mView.showTips("更新成功", IView.TipType.ToastShort);
                }
            });
        }

        @Override
        public void onError(String msg) {
            if (mView == null) return;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mView.showTips("更新失败", IView.TipType.ToastShort);
                }
            });
        }
    };
    private IUserInfoDao.OnUserinfoMotifyCompleteListener onUserinfoMotifyCompleteListener = new IUserInfoDao.OnUserinfoMotifyCompleteListener() {
        @Override
        public void onSuccess() {
            if (mView == null) return;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mView.showTips("更新成功", IView.TipType.ToastShort);
                }
            });
        }

        @Override
        public void onError(String msg) {
            if (mView == null) return;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mView.showTips("更新失败", IView.TipType.ToastShort);
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

    public void updateUserInfo() {
        if (mView.hasChangedUserHeadImage()) {
            final File file = new File(mView.getChangedUserHeadImageLocalPath());
            if (file.exists())
                mModel.updateUserFaceImage(file, onUpdateUserFaceImageCompleteListener);
        }
        if (mView.hasChangedUserInfo())
            mModel.modifyUserInfo(mView.getModifiedUserInfo(), onUserinfoMotifyCompleteListener);

    }
}
