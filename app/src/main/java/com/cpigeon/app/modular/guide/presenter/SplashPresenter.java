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

    }

    @Override
    protected ISplashDao initDao() {
        return new SplashDaoImpl();
    }

    ISplashDao.OnLoadCompleteListener onLoadCompleteListener = new ISplashDao.OnLoadCompleteListener() {
        @Override
        public void onLoadComplete(final String url) {
            post(new CheckAttachRunnable() {
                @Override
                protected void runAttached() {
                    mView.showAd(url);
                }
            });
        }
    };

    public void welcome() {
        mDao.getSplashADFromServer();
        mDao.loadSplashAdUrl(onLoadCompleteListener);
        if (mView.checkLogin())
            mDao.autoiLogin();
        mView.countDown();
    }
}
