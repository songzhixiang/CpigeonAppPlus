package com.cpigeon.app.modular.usercenter.presenter;

import android.os.Handler;

import com.cpigeon.app.commonstandard.activity.IView;
import com.cpigeon.app.modular.usercenter.model.daoimpl.UserCenterDaoImpl;
import com.cpigeon.app.modular.usercenter.fragment.viewdao.IUserCenterView;
import com.cpigeon.app.modular.usercenter.model.dao.IUserCenterDao;

import java.util.Map;

/**
 * Created by Administrator on 2017/4/10.
 */

public class UserCenterPre {
    private IUserCenterView iUserCenterView;
    private IUserCenterDao iUserCenterDao;
    private Handler mHandler = new Handler();
    public UserCenterPre(IUserCenterView iUserCenterView) {
        this.iUserCenterView = iUserCenterView;
        this.iUserCenterDao = new UserCenterDaoImpl();
    }
    public void loadBalance()
    {
        iUserCenterView.showLoading();
        iUserCenterDao.loadUserBalance(new IUserCenterDao.OnLoadCompleteListener() {
            @Override
            public void loadSuccess(final Map<String, Object> data) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iUserCenterView.hideLoading();
                        iUserCenterView.showUserInfo(data);
                    }
                });

            }

            @Override
            public void laodFailed() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iUserCenterView.hideLoading();
                        iUserCenterView.showTips("加载失败", IView.TipType.ViewError);
                    }
                });
            }
        });

    }
    public void loadSignStatus(){
        iUserCenterDao.getUserSignStatus(new IUserCenterDao.OnGetCompleteListener() {
            @Override
            public void loadSuccess(final Boolean isSign) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iUserCenterView.isSign(isSign);
                    }
                });
            }

            @Override
            public void loadFailed() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iUserCenterView.showTips("获取签到信息失败",IView.TipType.ToastShort);
                    }
                });
            }
        });
    }


}
