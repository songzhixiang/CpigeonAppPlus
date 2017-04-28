package com.cpigeon.app.modular.matchlive.presenter;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.matchlive.model.dao.IJiGeDao;
import com.cpigeon.app.modular.matchlive.model.daoimpl.IJiGeDaoImpl;
import com.cpigeon.app.modular.matchlive.view.adapter.JiGeDataAdapter;
import com.cpigeon.app.modular.matchlive.view.fragment.viewdao.IReportData;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */

public class JiGePre extends BasePresenter<IReportData, IJiGeDao> {
    public JiGePre(IReportData mView) {
        super(mView);
    }

    @Override
    protected IJiGeDao initDao() {
        return new IJiGeDaoImpl();
    }

    public void loadJiGe() {
        if (isAttached()) {
            if (!mView.isMoreDataLoading()) mView.showRefreshLoading();
            mDao.laodJiGeData(mView.getMatchType(), mView.getSsid(), mView.getFoot(), mView.getName(),
                    mView.hascz(), mView.getPageIndex(), mView.getPageSize(), mView.czIndex(), mView.sKey(), new IBaseDao.OnCompleteListener<List>() {
                        @Override
                        public void onSuccess(final List data) {
                            Logger.d(data == null ? "null" : data.size() + "");
                            postDelayed(new CheckAttachRunnable() {
                                @Override
                                protected void runAttached() {
                                    final List d = "xh".equals(mView.getMatchType()) ? JiGeDataAdapter.getXH(data) : JiGeDataAdapter.getGP(data);

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
                                        mView.showTips("获取记录失败", IView.TipType.View);
                                    }
                                }
                            }, 300);
                        }
                    });
        }
    }
}
