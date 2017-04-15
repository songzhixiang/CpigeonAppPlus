package com.cpigeon.app.modular.usercenter.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseLazyLoadFragment;
import com.cpigeon.app.commonstandard.view.fragment.BasePageTurnFragment;
import com.cpigeon.app.modular.order.view.adapter.OrderAdapter;
import com.cpigeon.app.modular.usercenter.model.bean.UserScore;
import com.cpigeon.app.modular.usercenter.presenter.ScorePresenter;
import com.cpigeon.app.modular.usercenter.view.activity.ScoreActivity;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.IScoreView;
import com.cpigeon.app.modular.usercenter.view.adapter.ScoreAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by chenshuai on 2017/4/13.
 */


public class UserScoreSub2Fragment extends BasePageTurnFragment<ScorePresenter, ScoreAdapter, UserScore> implements IScoreView.IScoreSub2View {

    @Override
    protected ScorePresenter initPresenter() {
        ScorePresenter presenter = ((ScoreActivity) getActivity()).getPresenter();
        presenter.attachScoreSub2View(this);
        return presenter;
    }

    @Override
    protected boolean isCanDettach() {
        return false;
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
