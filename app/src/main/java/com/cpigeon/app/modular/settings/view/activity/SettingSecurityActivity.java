package com.cpigeon.app.modular.settings.view.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.modular.usercenter.view.activity.SetPayPwdActivity;
import com.cpigeon.app.modular.usercenter.view.activity.SetUserPwdActivity;
import com.cpigeon.app.utils.NetUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by chenshuai on 2017/4/12.
 */

public class SettingSecurityActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rl_loginpwd)
    RelativeLayout rlLoginpwd;
    @BindView(R.id.rl_paypwd)
    RelativeLayout rlPaypwd;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting_security;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }


    @Override
    public void initView() {
        toolbar.setTitle("安全设置");
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

    @OnClick({R.id.rl_loginpwd, R.id.rl_paypwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_loginpwd:
                startActivity(SetUserPwdActivity.class);
                break;
            case R.id.rl_paypwd:
                startActivity(SetPayPwdActivity.class);
                break;
        }
    }
}
