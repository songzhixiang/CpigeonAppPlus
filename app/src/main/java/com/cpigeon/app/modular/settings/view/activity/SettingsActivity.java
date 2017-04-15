package com.cpigeon.app.modular.settings.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.AppManager;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.modular.usercenter.view.activity.LoginActivity;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.NetUtils;
import com.cpigeon.app.utils.SharedPreferencesTool;
import com.kyleduo.switchbutton.SwitchButton;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by chenshuai on 2017/4/12.
 */

public class SettingsActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_clear_cache_count)
    TextView tvClearCacheCount;
    @BindView(R.id.rl_clear_cache)
    RelativeLayout rlClearCache;
    @BindView(R.id.sb_push_notification)
    SwitchButton sbPushNotification;
    @BindView(R.id.rl_push_notification)
    RelativeLayout rlPushNotification;
    @BindView(R.id.sb_search_online)
    SwitchButton sbSearchOnline;
    @BindView(R.id.rl_search_online)
    RelativeLayout rlSearchOnline;
    @BindView(R.id.rl_security)
    RelativeLayout rlSecurity;
    @BindView(R.id.rl_market_score)
    RelativeLayout rlMarketScore;
    @BindView(R.id.tv_check_new_version_versionName)
    TextView tvCheckNewVersionVersionName;
    @BindView(R.id.rl_check_new_version)
    RelativeLayout rlCheckNewVersion;
    @BindView(R.id.btn_logout)
    Button btnLogout;

    @Override
    public int getLayoutId() {
        return R.layout.activity_settings;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        toolbar.setTitle("设置");
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

    @OnClick({R.id.rl_clear_cache, R.id.rl_security, R.id.rl_market_score, R.id.rl_check_new_version, R.id.btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_clear_cache:
                break;
            case R.id.rl_security:
                startActivity(new Intent(mContext, SettingSecurityActivity.class));
                break;
            case R.id.rl_market_score:
                String mAddress = "market://details?id=" + getPackageName();
                Intent marketIntent = new Intent("android.intent.action.VIEW");
                marketIntent.setData(Uri.parse(mAddress));
                startActivity(marketIntent);
                break;
            case R.id.rl_check_new_version:
                break;
            case R.id.btn_logout:
                if (checkLogin()) {
                    final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                    dialog.setCancelable(false);
                    dialog.setTitleText("提示")
                            .setContentText("确定要退出登录？")
                            .setConfirmText("确定")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("username", "");
                                    map.put("token", "");
                                    map.put("touxiang", "");
                                    map.put("touxiangurl", "");
                                    map.put("nicheng", "");
                                    map.put("logined", false);
                                    SharedPreferencesTool.Save(mContext, map, SharedPreferencesTool.SP_FILE_LOGIN);
                                    CpigeonData.getInstance().initialization();
                                    showTips("退出登录成功", TipType.ToastShort);
                                    dialog.dismiss();
                                    Intent intent = new Intent(mContext, LoginActivity.class);
                                    startActivity(intent);

                                }
                            })
                            .setCancelText("取消")
                            .show();
                } else {
                    finish();
                }
                break;
        }
    }
}
