package com.cpigeon.app.modular.guide.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpigeon.app.MainActivity;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.modular.guide.presenter.SplashPresenter;
import com.cpigeon.app.modular.usercenter.view.activity.LoginActivity;
import com.cpigeon.app.modular.guide.view.viewdao.ISplashView;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.NetUtils;
import com.cpigeon.app.utils.StatusBarSetting;
import com.cpigeon.app.utils.StatusBarTool;
import com.squareup.picasso.Picasso;

import java.sql.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenshuai on 2017/4/10.
 */

public class SplashActivity extends BaseActivity implements ISplashView {


    @BindView(R.id.tv_app_version)
    TextView tvAppVersion;
    @BindView(R.id.tv_copyright)
    TextView tvCopyright;
    @BindView(R.id.iv_ad_image)
    ImageView ivAdImage;
    @BindView(R.id.btn_ad_spik)
    Button btnAdSpik;
    @BindView(R.id.rl_ad)
    RelativeLayout rlAd;

    protected SplashPresenter mPresenter;
    CountDownTimer countDownTimer;
    boolean isEntry = false;

    //最短显示时间
    private static final long SHOW_TIME_MIN = 3400;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initPresenter() {
        mPresenter = new SplashPresenter(this);
    }

    @Override
    public void initView() {
        StatusBarTool.hideStatusBar(this);
        tvAppVersion.setText("V " + CommonTool.getVersionName(SplashActivity.this));
        tvCopyright.setText("中鸽科技版权所有©Copyright " + (new Date(System.currentTimeMillis()).getYear() + 1900));
        mPresenter.welcome();
    }


    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @OnClick(R.id.btn_ad_spik)
    public void onViewClicked() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (btnAdSpik != null) {
            btnAdSpik.setText("正在进入");
            btnAdSpik.setEnabled(false);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                entryApp();
            }
        }, 500);
    }


    @Override
    public void countDown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(SHOW_TIME_MIN, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (btnAdSpik != null)
                    btnAdSpik.setText(String.format("跳过(%ds)", millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                if (btnAdSpik != null)
                    btnAdSpik.setText("跳过");
                entryApp();
            }
        };
        countDownTimer.start();
    }

    @Override
    public void entryApp() {
        if (isEntry) return;
        Intent intent;
        if (checkLogin())
            intent = new Intent(SplashActivity.this, MainActivity.class);
        else
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        isEntry = true;
        finish();
    }

    @Override
    public void showAd(String url) {
        Picasso.with(this).load(url).into(ivAdImage);
        rlAd.setVisibility(View.VISIBLE);
    }
}
