package com.cpigeon.app.modular.matchlive.presenter;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.matchlive.model.dao.IChaZuDao;
import com.cpigeon.app.modular.matchlive.model.daoimpl.IChaZuDaoImpl;
import com.cpigeon.app.modular.matchlive.view.activity.viewdao.IRacePigeonsView;
import com.cpigeon.app.modular.matchlive.view.adapter.ChaZuBaoDaoDetailsAdapter;
import com.cpigeon.app.modular.matchlive.view.adapter.ChaZuZhiDingDetailsAdapter;
import com.cpigeon.app.modular.matchlive.view.adapter.RaceReportAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */

public class ChaZuBaoDaoDetailsPre extends BasePresenter<IRacePigeonsView, IChaZuDao> {
    public ChaZuBaoDaoDetailsPre(IRacePigeonsView mView) {
        super(mView);
    }

    @Override
    protected IChaZuDao initDao() {
        return new IChaZuDaoImpl();
    }

    public void loadChaZuBaoDaoDetails() {
        if (isAttached()) {
            if (mView.getPageIndex() == 1) mView.showRefreshLoading();
            mDao.loadChaZuBaoDaoDetails(mView.getMatchType(), mView.getSsid(), mView.getFoot(),
                    mView.getName(), mView.isHascz(), mView.getPageIndex(), mView.getPageSize(),
                    mView.getCzIndex(), mView.getSkey(), new IBaseDao.OnCompleteListener<List>() {
                        @Override
                        public void onSuccess(final List data) {
                            final List d = isDetached() ? null : "xh".equals(mView.getMatchType()) ? ChaZuBaoDaoDetailsAdapter.getXH(data) : ChaZuBaoDaoDetailsAdapter.getGP(data);
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

    public void loadChaZuZhiding() {
        if (isAttached()) {
            if (mView.getPageIndex() == 1) mView.showRefreshLoading();
            mDao.loadChaZhiDingDaoDetails(mView.getMatchType(), mView.getSsid(), mView.getFoot(),
                    mView.getName(), mView.isHascz(), mView.getPageIndex(), mView.getPageSize(),
                    mView.getCzIndex(), mView.getSkey(), new IBaseDao.OnCompleteListener<List>() {
                        @Override
                        public void onSuccess(List data) {
                            Logger.e(data.size() + "");
                            final List d = isDetached() ? null : "xh".equals(mView.getMatchType()) ? ChaZuZhiDingDetailsAdapter.getXH(data) : ChaZuZhiDingDetailsAdapter.getGP(data);
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
}
