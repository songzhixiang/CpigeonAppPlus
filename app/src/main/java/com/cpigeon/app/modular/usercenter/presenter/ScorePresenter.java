package com.cpigeon.app.modular.usercenter.presenter;

import android.support.annotation.NonNull;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.usercenter.model.bean.UserScore;
import com.cpigeon.app.modular.usercenter.model.dao.IScoreDao;
import com.cpigeon.app.modular.usercenter.model.daoimpl.ScoreDaoImpl;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.IScoreView;

import java.util.List;

/**
 * Created by chenshuai on 2017/4/13.
 */

public class ScorePresenter extends BasePresenter<IScoreView, IScoreDao> {

    IScoreView.IScoreSub2View mScoreSub2View;

    public ScorePresenter(IScoreView mView) {
        super(mView);

    }

    @Override
    public boolean isAttached() {
        return super.isAttached() || mScoreSub2View != null;
    }

    @Override
    public boolean isDettached() {
        return super.isDettached() && mScoreSub2View == null;
    }

    @Override
    public void dettach() {
        super.dettach();
        mScoreSub2View = null;
    }

    @Override
    protected IScoreDao initDao() {
        return new ScoreDaoImpl();
    }

    public void attachScoreSub2View(@NonNull IScoreView.IScoreSub2View scoreSub2View) {
        this.mScoreSub2View = scoreSub2View;
    }

    IBaseDao.OnCompleteListener<List<UserScore>> onCompleteListener = new IBaseDao.OnCompleteListener<List<UserScore>>() {
        @Override
        public void onSuccess(final List<UserScore> data) {
            if (mScoreSub2View == null) return;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScoreSub2View.hideRefreshLoading();
                    mScoreSub2View.showMoreData(data);
                }
            }, 500);
        }

        @Override
        public void onFail(String msg) {
            if (mScoreSub2View == null) return;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScoreSub2View.hideRefreshLoading();
                    mScoreSub2View.showTips("获取积分记录失败", IView.TipType.View);
                }
            }, 500);
        }
    };

    public void loadScoreRecord() {
        if (mScoreSub2View == null) return;
//        mScoreSub2View.showTips("", IView.TipType.LoadingShow);
        mDao.loadScoreRecord(mScoreSub2View.getPageIndex(), mScoreSub2View.getPageSize(), onCompleteListener);
    }
}
