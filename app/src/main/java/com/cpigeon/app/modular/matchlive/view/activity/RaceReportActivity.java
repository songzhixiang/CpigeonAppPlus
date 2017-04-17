package com.cpigeon.app.modular.matchlive.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItemAdapter;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItems;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.view.fragment.ChaZuBaoDaoFragment;
import com.cpigeon.app.modular.matchlive.view.fragment.ChaZuZhiDingFragment;
import com.cpigeon.app.modular.matchlive.view.fragment.JiGeDataFragment;
import com.cpigeon.app.modular.matchlive.view.fragment.ReportDataFragment;
import com.cpigeon.app.utils.NetUtils;
import com.cpigeon.app.utils.customview.smarttab.SmartTabLayout;
import com.orhanobut.logger.Logger;

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
    ///////////////////////////////////////////////////////////////////////////
    // 视图
    ///////////////////////////////////////////////////////////////////////////
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.race_report_smartTabLayout)
    SmartTabLayout mSmartTabLayout;
    @BindView(R.id.race_report_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.fab)
    FloatingActionButton mFloatingActionButton;
    ///////////////////////////////////////////////////////////////////////////
    // 适配器和数据
    ///////////////////////////////////////////////////////////////////////////
    private MatchInfo matchInfo;//赛事信息
    private Bundle bundle;
    private Intent intent;
    private FragmentPagerAdapter mFragmentPagerAdapter;
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
        intent = this.getIntent();
        bundle = intent.getExtras();
        matchInfo = (MatchInfo) bundle.getSerializable("matchinfo");
        if (matchInfo!=null)
        {
            Logger.e("matchinfo"+matchInfo.getBsmc());
            mFragmentPagerAdapter = new FragmentPagerItemAdapter(
                    getSupportFragmentManager(), FragmentPagerItems.with(this)
                    .add("报道数据", ReportDataFragment.class)
                    .add("插组报道", ChaZuBaoDaoFragment.class)
                    .add("集鸽数据", JiGeDataFragment.class)
                    .add("插组指定", ChaZuZhiDingFragment.class)
                    .create());
            mViewPager.setAdapter(mFragmentPagerAdapter);
            mSmartTabLayout.setViewPager(mViewPager);
        }

    }


    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }


    @OnClick(R.id.fab)
    public void onViewClicked() {
    }

    /**
     * 获得传过来的直播数据
     *
     * @return
     */
    @Override
    public MatchInfo getMatchInfo() {
        return matchInfo;
    }

    @Override
    public void showReportData(List list) {

    }

    @Override
    public void showPigeonData(List list) {

    }

    @Override
    public void addRaceClickCount(Boolean data) {

    }


}
