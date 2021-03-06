package com.cpigeon.app.modular.matchlive.presenter;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.matchlive.model.dao.IRaceDao;
import com.cpigeon.app.modular.matchlive.model.daoimpl.RaceDaoImpl;
import com.cpigeon.app.modular.matchlive.view.adapter.RaceReportAdapter;
import com.cpigeon.app.modular.matchlive.view.adapter.RaceXunFangAdapter;
import com.cpigeon.app.modular.matchlive.view.fragment.viewdao.IReportData;

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

    public void loadRaceData(final int loadType) {
        if (mView.getPageIndex() == 1) mView.showRefreshLoading();
        mDao.showReprotData(mView.getMatchType(), mView.getSsid(), mView.getFoot(), mView.getName(),
                mView.hascz(), mView.getPageIndex(), mView.getPageSize(), mView.czIndex(), mView.sKey(), new IBaseDao.OnCompleteListener<List>() {
                    @Override
                    public void onSuccess(final List data) {
                        if (isAttached()) {
                            if (loadType == 0) {
                                final List d = isDetached() ? null : "xh".equals(mView.getMatchType()) ? RaceReportAdapter.getXH(data) : RaceReportAdapter.getGP(data);
                                postDelayed(new CheckAttachRunnable() {
                                    @Override
                                    protected void runAttached() {
                                        if (mView.isMoreDataLoading()) {
                                            mView.loadMoreComplete();
                                        } else {
                                            mView.hideRefreshLoading();
                                        }
                                        mView.showMoreData(d);
                                    }
                                }, 300);

                            } else {
                                final List d = isDetached() ? null : RaceXunFangAdapter.getGP(data);
                                postDelayed(new CheckAttachRunnable() {
                                    @Override
                                    protected void runAttached() {
                                        if (mView.isMoreDataLoading()) {
                                            mView.loadMoreComplete();
                                        } else {
                                            mView.hideRefreshLoading();
                                        }
                                        mView.showMoreData(d);
                                    }
                                }, 300);
                            }

                        }
                    }

                    @Override
                    public void onFail(String msg) {
                        postDelayed(new CheckAttachRunnable() {
                            @Override
                            protected void runAttached() {
                                if (mView.isMoreDataLoading()) {
                                    mView.loadMoreFail();
                                } else {
                                    mView.hideRefreshLoading();
                                    mView.showTips("获取报到记录失败", IView.TipType.View);
                                }
                            }
                        }, 300);
                    }
                });
    }
}
