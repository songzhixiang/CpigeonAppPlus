package com.cpigeon.app.modular.usercenter.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.modular.home.view.activity.WebActivity;
import com.cpigeon.app.utils.CPigeonApiUrl;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.NetUtils;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/10.
 */

public class AboutActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_version_name)
    TextView tvVersionName;
    @BindView(R.id.tv_copyright)
    TextView tvCopyright;
    @BindView(R.id.tvCompanyName)
    TextView tvCompanyName;

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }


    @Override
    public void initView() {
        toolbar.setTitle("关于");
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvVersionName.setText("V " + CommonTool.getVersionName(this));
        tvCopyright.setText(String.format("Copyright © 2006- %d All rights reserved.", new Date(System.currentTimeMillis()).getYear() + 1900));
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @OnClick({R.id.tv_function_intro, R.id.tv_user_protocol})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_function_intro:
                intent = new Intent(this, WebActivity.class);
                intent.putExtra(WebActivity.INTENT_DATA_KEY_URL, CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.APP_INTRO_URL);

                break;
            case R.id.tv_user_protocol:
                intent = new Intent(this, WebActivity.class);
                intent.putExtra(WebActivity.INTENT_DATA_KEY_URL, CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.APP_USER_PROTOCOL_URL);
                break;
        }
        if (intent != null) {
            intent.putExtra(WebActivity.INTENT_DATA_KEY_BACKNAME, "关于");
            startActivity(intent);
        }
    }
}
