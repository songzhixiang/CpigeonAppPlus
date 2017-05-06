package com.cpigeon.app.modular.matchlive.presenter;

import android.widget.Toast;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.model.dao.IMatchInfo;
import com.cpigeon.app.modular.matchlive.model.daoimpl.MatchInfoImpl;
import com.cpigeon.app.modular.matchlive.view.fragment.viewdao.IMatchSubView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/7.
 */

public class MatchLiveSubPre extends BasePresenter<IMatchSubView, IMatchInfo> {


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
                post(new CheckAttachRunnable() {
                    @Override
                    protected void runAttached() {
                        mView.hideRefreshLoading();
                        mView.showXHData(data, type);
                    }
                });

            }

            @Override
            public void onFail(String msg) {
                post(new CheckAttachRunnable() {
                    @Override
                    protected void runAttached() {
                        mView.hideRefreshLoading();
                        if (mView.hasDataList())
                        {
                            mView.showTips("获取比赛列表失败",IView.TipType.ToastShort);
                        }else {
                            mView.showTips("获取失败，下拉重新加载",IView.TipType.View);
                        }
                    }
                });
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
                post(new CheckAttachRunnable() {
                    @Override
                    protected void runAttached() {
                        mView.hideRefreshLoading();
                        mView.showGPData(data, type);
                    }
                });
            }

            @Override
            public void onFail(String msg) {
                post(new CheckAttachRunnable() {
                    @Override
                    protected void runAttached() {
                        mView.hideRefreshLoading();
                        if (mView.hasDataList())
                        {
                            mView.showTips("获取比赛列表失败",IView.TipType.ToastShort);
                        }else {
                            mView.showTips("获取失败，下拉重新加载",IView.TipType.View);
                        }
                    }
                });
            }
        });


    }

    @Override
    protected IMatchInfo initDao() {
        return new MatchInfoImpl();
    }
}
