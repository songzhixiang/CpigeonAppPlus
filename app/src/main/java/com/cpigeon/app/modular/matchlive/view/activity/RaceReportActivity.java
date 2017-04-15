package com.cpigeon.app.modular.matchlive.view.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.utils.NetUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/10.
 */

public class RaceReportActivity extends BaseActivity implements IRaceReport {
    private final static int DATA_TYPE_BDSJ = 1;//数据类型-报道数据
    private final static int DATA_TYPE_CZBD = 2;//数据类型-插组报道
    private final static int DATA_TYPE_SLQD = 3;//数据类型-上笼清单
    private final static int DATA_TYPE_CZZD = 4;//数据类型-插组指定
    private final static int mLoadMoreSize = 100;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview_race_details)
    RecyclerView recyclerviewRaceDetails;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private MatchInfo matchInfo;//赛事信息

    @Override
    public int getLayoutId() {
        return R.layout.activity_race_details;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }


    @Override
    public void initView() {

    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    public void showReportData(List list) {

    }

    @OnClick(R.id.fab)
    public void onViewClicked() {
    }
}
