package com.cpigeon.app.modular.usercenter.view.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.utils.NetUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenshuai on 2017/4/20.
 * 我的关注
 */

public class MyFollowActivity extends BaseActivity {

    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.smlv_myFollowList)
    RecyclerView smlvMyFollowList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_myfollow;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        toolbar.setTitle("我的关注");
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }
}
