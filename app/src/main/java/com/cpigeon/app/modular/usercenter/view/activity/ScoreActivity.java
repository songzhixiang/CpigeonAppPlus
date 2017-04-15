package com.cpigeon.app.modular.usercenter.view.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItemAdapter;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItems;
import com.cpigeon.app.modular.usercenter.presenter.ScorePresenter;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.IScoreView;
import com.cpigeon.app.modular.usercenter.view.fragment.UserScoreSub1Fragment;
import com.cpigeon.app.modular.usercenter.view.fragment.UserScoreSub2Fragment;
import com.cpigeon.app.modular.usercenter.view.fragment.UserScoreSub3Fragment;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.NetUtils;
import com.cpigeon.app.utils.customview.smarttab.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/10.
 */

public class ScoreActivity extends BaseActivity<ScorePresenter> implements IScoreView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_user_score_count)
    TextView tvUserScoreCount;
    @BindView(R.id.viewpagertab)
    SmartTabLayout viewpagertab;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_score;
    }

    @Override
    public ScorePresenter initPresenter() {
        return new ScorePresenter(this);
    }

    public ScorePresenter getPresenter() {
        return this.mPresenter;
    }

    @Override
    public void initView() {
        toolbar.setTitle("我的积分");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvUserScoreCount.setText(CpigeonData.getInstance().getUserScore() + "");

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("积分兑换", UserScoreSub1Fragment.class)
                .add("积分记录", UserScoreSub2Fragment.class)
                .add("获取积分", UserScoreSub3Fragment.class)
                .create());

        viewPager.setAdapter(adapter);

        viewpagertab.setViewPager(viewPager);
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }
}
