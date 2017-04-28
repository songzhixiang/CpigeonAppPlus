package com.cpigeon.app.modular.home.presenter;

import android.os.Handler;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.modular.home.model.bean.HomeAd;
import com.cpigeon.app.modular.home.model.dao.IHomeFragmentDao;
import com.cpigeon.app.modular.home.model.daoimpl.HomeFragmentDaoImpl;
import com.cpigeon.app.modular.home.view.fragment.viewdao.IHomeView;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/4/6.
 */

public class HomePresenter extends BasePresenter<IHomeView, IHomeFragmentDao> {


    public HomePresenter(IHomeView mView) {
        super(mView);
    }

    public void laodAd() {
        if (isAttached()) {
            mDao.loadHomeAd(new IBaseDao.OnCompleteListener<List<HomeAd>>() {
                @Override
                public void onSuccess(final List<HomeAd> data) {
                    post(new CheckAttachRunnable() {
                        @Override
                        protected void runAttached() {
                            mView.showAd(data);
                        }
                    });
                }

                @Override
                public void onFail(String msg) {

                }
            });
        }

    }

    public void loadMatchInfo(final int loadType) {
        if (isAttached()) {
            mDao.loadMatchInfo(loadType, new IBaseDao.OnCompleteListener<List<MatchInfo>>() {
                @Override
                public void onSuccess(final List<MatchInfo> data) {
                    post(new CheckAttachRunnable() {
                        @Override
                        protected void runAttached() {
                            switch (loadType) {
                                case 0:
                                    mView.showMatchGPLiveData(data, loadType);
                                    break;
                                case 1:
                                    mView.showMatchXhLiveData(data, loadType);
                                    break;
                            }
                        }
                    });

                }

                @Override
                public void onFail(String msg) {

                }
            });
        }

    }

    @Override
    protected IHomeFragmentDao initDao() {
        return new HomeFragmentDaoImpl();
    }
}
