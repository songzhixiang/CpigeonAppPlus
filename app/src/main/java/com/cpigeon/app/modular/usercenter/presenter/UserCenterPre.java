package com.cpigeon.app.modular.usercenter.presenter;

import android.os.Handler;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.usercenter.model.daoimpl.UserCenterDaoImpl;
import com.cpigeon.app.modular.usercenter.view.fragment.viewdao.IUserCenterView;
import com.cpigeon.app.modular.usercenter.model.dao.IUserCenterDao;

import java.util.Map;

/**
 * Created by Administrator on 2017/4/10.
 */

public class UserCenterPre extends BasePresenter<IUserCenterView,IUserCenterDao>{

    public UserCenterPre(IUserCenterView mView) {
        super(mView);

    }

    @Override
    protected IUserCenterDao initDao() {
        return new UserCenterDaoImpl();

    }

    public void loadBalance()
    {

        mDao.loadUserBalance(new IBaseDao.OnCompleteListener<Map<String, Object>>() {
            @Override
            public void onSuccess(final Map<String, Object> data) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mView.showUserInfo(data);
                    }
                });

            }

            @Override
            public void onFail(String msg) {

            }
        });

    }
    public void loadSignStatus(){
        mDao.getUserSignStatus(new IBaseDao.OnCompleteListener<Boolean>() {
            @Override
            public void onSuccess(final Boolean data) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mView.isSign(data);
                    }
                });
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }
}
