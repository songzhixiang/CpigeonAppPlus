package com.cpigeon.app.modular.usercenter.view.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.adapter.ContentFragmentAdapter;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItemAdapter;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItems;
import com.cpigeon.app.modular.usercenter.view.fragment.MessageFragment;
import com.cpigeon.app.modular.usercenter.view.fragment.UserScoreSub1Fragment;
import com.cpigeon.app.modular.usercenter.view.fragment.UserScoreSub2Fragment;
import com.cpigeon.app.modular.usercenter.view.fragment.UserScoreSub3Fragment;
import com.cpigeon.app.utils.NetUtils;
import com.cpigeon.app.utils.customview.smarttab.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/10.
 */

public class MessageActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewpagertab)
    SmartTabLayout viewpagertab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        toolbar.setTitle("消息通知");
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("通知", MessageFragment.class)
                .add("赞", MessageFragment.class)
                .add("评论", MessageFragment.class)
                .create());
        viewpager.setOffscreenPageLimit(3);
        //设置transformer
        //mOrientedViewPager.setPageTransformer(true, new VerticalStackTransformer(mContext));
        viewpager.setAdapter(adapter);
        viewpager.setOverScrollMode(View.OVER_SCROLL_NEVER);

        viewpagertab.setViewPager(viewpager);
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
