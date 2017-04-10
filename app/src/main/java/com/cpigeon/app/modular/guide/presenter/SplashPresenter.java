package com.cpigeon.app.modular.guide.presenter;

import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.modular.guide.model.dao.ISplashDao;
import com.cpigeon.app.modular.guide.model.daoimpl.SplashDaoImpl;
import com.cpigeon.app.modular.guide.view.viewdao.ISplashView;

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
