package com.cpigeon.app.modular.matchlive.presenter;

import android.os.Handler;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.model.dao.IMatchInfo;
import com.cpigeon.app.modular.matchlive.model.daoimpl.MatchInfoImpl;
import com.cpigeon.app.modular.matchlive.view.fragment.IMatchSubView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/7.
 */

public class MatchLiveSubPre extends BasePresenter<IMatchSubView,IMatchInfo>{



    public MatchLiveSubPre(IMatchSubView mView) {
        super(mView);
    }

    public void loadXHData(final int type) {
        mView.showRefreshLoading();
        // 0 加载协会数据，显示
        // 1 加载协会数据，不显示
        mDao.loadXHDatas(new IBaseDao.OnCompleteListener<List<MatchInfo>>() {
            @Override
            public void onSuccess(final List<MatchInfo> data) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        mView.hideRefreshLoading();
                        mView.showXHData(data,type);
                    }
                });
            }

            @Override
            public void onFail(String msg) {

            }
        });


    }

    public void loadGPData(final int type) {
        mView.showRefreshLoading();
        // 0 加载公棚数据，显示
        // 1 加载公棚数据，不显示
        mDao.loadGPDatas(new IBaseDao.OnCompleteListener<List<MatchInfo>>() {
            @Override
            public void onSuccess(final List<MatchInfo> data) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        mView.hideRefreshLoading();
                        mView.showGPData(data,type);
                    }
                });

            }

            @Override
            public void onFail(String msg) {

            }
        });


    }

    @Override
    protected IMatchInfo initDao() {
        return new MatchInfoImpl();
    }
}
