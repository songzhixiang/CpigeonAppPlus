package com.cpigeon.app.modular.guide.view;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.cpigeon.app.MainActivity;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.adapter.ContentFragmentAdapter;
import com.cpigeon.app.modular.usercenter.view.activity.LoginActivity;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.NetUtils;
import com.cpigeon.app.utils.SharedPreferencesTool;

import butterknife.BindView;

/**
 * Created by chenshuai on 2017/4/19.
 */

public class AppIntroActivity extends BaseActivity {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private ContentFragmentAdapter mContentFragmentAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.layout_com_viewpager;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        mContentFragmentAdapter = new ContentFragmentAdapter(getSupportFragmentManager());

        AppIntroFragment fragment = new AppIntroFragment();
        fragment.setBackgroundResource(R.mipmap.guide_1);
        mContentFragmentAdapter.appendData(fragment);
        fragment = new AppIntroFragment();
        fragment.setBackgroundResource(R.mipmap.guide_2);
        mContentFragmentAdapter.appendData(fragment);
        fragment = new AppIntroFragment();
        fragment.setIsEnd(true);
        mContentFragmentAdapter.appendData(fragment);
        viewPager.setOffscreenPageLimit(3);
        //设置transformer
        //mOrientedViewPager.setPageTransformer(true, new VerticalStackTransformer(mContext));
        viewPager.setAdapter(mContentFragmentAdapter);
        viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    public void entryApp() {
        Intent intent;
        if (checkLogin())
            intent = new Intent(this, MainActivity.class);
        else
            intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
        SharedPreferencesTool.Save(mContext, "guide_version", CommonTool.getVersionName(mContext));
    }
}
