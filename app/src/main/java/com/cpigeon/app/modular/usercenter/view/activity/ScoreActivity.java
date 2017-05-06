package com.cpigeon.app.modular.usercenter.view.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItemAdapter;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItems;
import com.cpigeon.app.modular.usercenter.view.fragment.UserScoreSub1Fragment;
import com.cpigeon.app.modular.usercenter.view.fragment.UserScoreSub2Fragment;
import com.cpigeon.app.modular.usercenter.view.fragment.UserScoreSub3Fragment;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.NetUtils;
import com.cpigeon.app.utils.customview.listener.AppBarStateChangeListener;
import com.cpigeon.app.utils.customview.smarttab.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/4/10.
 */

public class ScoreActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_user_score_count)
    TextView tvUserScoreCount;
    @BindView(R.id.viewpagertab)
    SmartTabLayout viewpagertab;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R.id.civ_score_bg)
    CircleImageView civScoreBg;
    @BindView(R.id.cv_score_bg)
    CardView cvScoreBg;
    private CpigeonData.OnDataChangedListener onDataChangedLisenter = new CpigeonData.OnDataChangedListener() {
        @Override
        public void OnDataChanged(CpigeonData cpigeonData) {
            if (tvUserScoreCount != null)
                tvUserScoreCount.setText(cpigeonData.getUserScore() + "");
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_score;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }


    @Override
    public void initView() {
//        toolbar.setTitle("我的鸽币");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示

        collapsingToolbarLayout.setTitle("我的鸽币(" + CpigeonData.getInstance().getUserScore() + ")");
        //通过CollapsingToolbarLayout修改字体颜色
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);//设置还没收缩时状态下字体颜色
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色
        appbarlayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //展开状态
                    collapsingToolbarLayout.setTitle(null);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    collapsingToolbarLayout.setTitle("我的鸽币(" + CpigeonData.getInstance().getUserScore() + ")");
                } else {
                    //中间状态
                    collapsingToolbarLayout.setTitle("我的鸽币");
                }
            }
        });
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            civScoreBg.setVisibility(View.VISIBLE);
            cvScoreBg.setVisibility(View.GONE);
        } else {
            civScoreBg.setVisibility(View.GONE);
            cvScoreBg.setVisibility(View.VISIBLE);
        }
        tvUserScoreCount.setText(CpigeonData.getInstance().getUserScore() + "");

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("鸽币兑换", UserScoreSub1Fragment.class)
                .add("鸽币记录", UserScoreSub2Fragment.class)
                .add("获取鸽币", UserScoreSub3Fragment.class)
                .create());
        viewPager.setAdapter(adapter);
        viewpagertab.setViewPager(viewPager);
        viewPager.setCurrentItem(1);

        CpigeonData.getInstance().addOnDataChangedListener(onDataChangedLisenter);
        CpigeonData.DataHelper.getInstance().updateUserBalanceAndScoreFromServer();
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }
}
