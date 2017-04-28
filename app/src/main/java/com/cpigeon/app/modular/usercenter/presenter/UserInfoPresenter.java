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

    private IUserInfoDao.OnUpdateUserFaceImageCompleteListener onUpdateUserFaceImageCompleteListener = new IUserInfoDao.OnUpdateUserFaceImageCompleteListener() {
        @Override
        public void onSuccess(String url) {
            post(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                    mView.showTips("更新成功", IView.TipType.ToastShort);
                }
            });
        }

        @Override
        public void onError(String msg) {
            post(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                    mView.showTips("更新失败", IView.TipType.ToastShort);
                }
            });
        }
    };
    private IUserInfoDao.OnUserinfoMotifyCompleteListener onUserinfoMotifyCompleteListener = new IUserInfoDao.OnUserinfoMotifyCompleteListener() {
        @Override
        public void onSuccess() {
            post(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                    mView.showTips("更新成功", IView.TipType.ToastShort);
                }
            });
        }

        @Override
        public void onError(String msg) {
            post(new CheckAttachRunnable() {
                @Override
                public void runAttached() {
                    mView.showTips("更新失败", IView.TipType.ToastShort);
                }
            });
        }
    };

    public UserInfoPresenter(IUserInfoView mView) {
        super(mView);
    }

    @Override
    protected IUserInfoDao initDao() {
        return new UserInfoDaoImpl();
    }

    public void updateUserInfo() {
        if (mView.hasChangedUserHeadImage()) {
            final File file = new File(mView.getChangedUserHeadImageLocalPath());
            if (file.exists())
                mDao.updateUserFaceImage(file, onUpdateUserFaceImageCompleteListener);
        }
        if (mView.hasChangedUserInfo())
            mDao.modifyUserInfo(mView.getModifiedUserInfo(), onUserinfoMotifyCompleteListener);

    }
}
