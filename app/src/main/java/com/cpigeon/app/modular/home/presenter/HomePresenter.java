package com.cpigeon.app.modular.home.presenter;

import android.os.Handler;

import com.cpigeon.app.modular.home.model.bean.HomeAd;
import com.cpigeon.app.modular.home.model.dao.IHomeFragmentDao;
import com.cpigeon.app.modular.home.model.daoimpl.HomeFragmentDaoImpl;
import com.cpigeon.app.modular.home.view.fragment.viewdao.IHomeView;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/4/6.
 */

public class HomePresenter {
    private IHomeFragmentDao iHomeFragmentDao;
    private IHomeView iHomeView;
    private Handler mHandler = new Handler();

    public HomePresenter(IHomeView iHomeView) {
        this.iHomeView = iHomeView;
        this.iHomeFragmentDao = new HomeFragmentDaoImpl();
    }
    public void laodAd()
    {
        iHomeFragmentDao.loadHomeAd(new IHomeFragmentDao.OnLoadCompleteListener() {
            @Override
            public void onLoadSuccess(final List<HomeAd> adList) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iHomeView.showAd(adList);
                    }
                });

            }

            @Override
            public void onLoadFailed(String msg) {

            }
        });
    }
    public void loadMatchInfo(final int loadType)
    {
        iHomeFragmentDao.loadMatchInfo(loadType, new IHomeFragmentDao.OnReadCompleteListenr() {
            @Override
            public void onLoadSuccess(final List<MatchInfo> matchInfos) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iHomeView.showMatchLiveData(matchInfos,loadType);
                    }
                });
            }

            @Override
            public void onLoadFailed() {

            }
        });
    }
}
