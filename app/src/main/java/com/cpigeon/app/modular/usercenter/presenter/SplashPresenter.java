package com.cpigeon.app.modular.usercenter.presenter;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.modular.usercenter.model.dao.ISplashDao;
import com.cpigeon.app.modular.usercenter.model.daoimpl.SplashDaoImpl;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.ISplashView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chenshuai on 2017/4/10.
 */

public class SplashPresenter extends BasePresenter<ISplashView, ISplashDao> {
    public SplashPresenter(ISplashView mView) {
        super(mView);
        mModel = new SplashDaoImpl();
    }

    ISplashDao.OnLoadCompleteListener onLoadCompleteListener = new ISplashDao.OnLoadCompleteListener() {
        @Override
        public void onLoadComplete(final String url) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mView.showAd(url);
                }
            });
        }
    };

    public void welcome() {
        mModel.getSplashADFromServer();
        mModel.loadSplashAdUrl(onLoadCompleteListener);
        if (mView.checkLogin())
            mModel.autoiLogin();
        mView.countDown();
    }
}
