package com.cpigeon.app.modular.matchlive.presenter;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.matchlive.model.dao.IRaceDao;
import com.cpigeon.app.modular.matchlive.model.daoimpl.RaceDaoImpl;
import com.cpigeon.app.modular.matchlive.view.adapter.RaceReportAdapter;
import com.cpigeon.app.modular.matchlive.view.fragment.IReportData;

import java.util.List;

/**
 * Created by Administrator on 2017/4/17.
 */

public class RacePre extends BasePresenter<IReportData, IRaceDao> {
    public RacePre(IReportData mView) {
        super(mView);
    }

    @Override
    protected IRaceDao initDao() {
        return new RaceDaoImpl();
    }

    public void loadRaceData() {

        mDao.showReprotData(mView.getMatchType(), mView.getSsid(), mView.getFoot(), mView.getName(),
                mView.hascz(), mView.getPageIndex(), mView.getPageSize(), mView.czIndex(), mView.sKey(), new IBaseDao.OnCompleteListener<List>() {
                    @Override
                    public void onSuccess(final List data) {
                        if (isAttached()) {
                            final List d = isDetached() ? null : "xh".equals(mView.getMatchType()) ? RaceReportAdapter.getXH(data) : RaceReportAdapter.getGP(data);
                            postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (isAttached()) {
                                        if (mView.isRefreshing())
                                            mView.hideRefreshLoading();
                                        else if (mView.isMoreDataLoading())
                                            mView.loadMoreComplete();
                                        mView.showMoreData(d);
                                    }
                                }
                            }, 300);
                        }
                    }

                    @Override
                    public void onFail(String msg) {
                        if (isAttached())
                            postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (isAttached()) {
                                        if (mView.isRefreshing()) {
                                            mView.hideRefreshLoading();
                                            mView.showTips("加载失败", IView.TipType.View);
                                        } else if (mView.isMoreDataLoading()) {
                                            mView.loadMoreFail();
                                        }
                                    }
                                }
                            }, 300);
                    }
                });
    }
}
