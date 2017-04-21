package com.cpigeon.app.modular.usercenter.view.fragment;

import com.cpigeon.app.commonstandard.view.fragment.BasePageTurnFragment;
import com.cpigeon.app.modular.usercenter.model.bean.UserScore;
import com.cpigeon.app.modular.usercenter.presenter.ScorePresenter;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.IScoreView;
import com.cpigeon.app.modular.usercenter.view.adapter.ScoreAdapter;

/**
 * Created by chenshuai on 2017/4/13.
 */


public class UserScoreSub2Fragment extends BasePageTurnFragment<ScorePresenter, ScoreAdapter, UserScore> implements IScoreView {

    @Override
    protected ScorePresenter initPresenter() {
        return new ScorePresenter(this);
    }

    @Override
    protected boolean isCanDettach() {
        return true;
    }

    @Override
    protected int getDefaultPageSize() {
        return 10;
    }

    @Override
    protected String getEmptyDataTips() {
        return "还没有积分";
    }

    @Override
    public ScoreAdapter getNewAdapterWithNoData() {
        return new ScoreAdapter(null);
    }

    @Override
    protected void loadDataByPresenter() {
        mPresenter.loadScoreRecord();
    }
}
