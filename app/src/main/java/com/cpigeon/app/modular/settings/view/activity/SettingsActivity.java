package com.cpigeon.app.modular.settings.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.AppManager;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.settings.view.activity.dao.ISettingView;
import com.cpigeon.app.modular.usercenter.view.activity.LoginActivity;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.CpigeonConfig;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.DateTool;
import com.cpigeon.app.utils.FileTool;
import com.cpigeon.app.utils.NetUtils;
import com.cpigeon.app.utils.SharedPreferencesTool;
import com.cpigeon.app.utils.UpdateManager;
import com.cpigeon.app.utils.cache.CacheManager;
import com.kyleduo.switchbutton.SwitchButton;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by chenshuai on 2017/4/12.
 */

public class SettingsActivity extends BaseActivity {
    public final static String SETTING_KEY_SEARCH_ONLINE = "search_online";
    public final static String SETTING_KEY_PUSH_NOTIFICATION = "push_notification";
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
    private UpdateManager mUpdateManager;
    boolean mEntryInstall = false;

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
        sbPushNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferencesTool.Save(SettingsActivity.this, SETTING_KEY_PUSH_NOTIFICATION, isChecked, SharedPreferencesTool.SP_FILE_APPSETTING);
            }
        });
        sbSearchOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferencesTool.Save(SettingsActivity.this, SETTING_KEY_SEARCH_ONLINE, isChecked, SharedPreferencesTool.SP_FILE_APPSETTING);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    protected void initData() {
        if (mEntryInstall)
            AppManager.getAppManager().AppExit();
        sbPushNotification.setChecked(SharedPreferencesTool.Get(SettingsActivity.this, SETTING_KEY_PUSH_NOTIFICATION, true, SharedPreferencesTool.SP_FILE_APPSETTING));
        sbSearchOnline.setChecked(SharedPreferencesTool.Get(SettingsActivity.this, SETTING_KEY_SEARCH_ONLINE, true, SharedPreferencesTool.SP_FILE_APPSETTING));
        tvCheckNewVersionVersionName.setText(CommonTool.getVersionName(this));
        String cacheCount = FileTool.getFileOrFilesSize(CpigeonConfig.CACHE_FOLDER);
        tvClearCacheCount.setText(cacheCount.equals("0B") ? getString(R.string.no_cache) : cacheCount);
        //logout.setVisibility(View.GONE);
        btnLogout.setText(checkLogin() ? "退出登录" : "登录");
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
                clearCache();
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
                checkNewVersion();
                break;
            case R.id.btn_logout:
                logout();
                break;
        }
    }

    /**
     * 退出登录
     */
    private void logout() {
        if (checkLogin()) {
            final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            dialog.setCancelable(false);
            dialog.setTitleText("提示")
                    .setContentText("确定要退出登录？")
                    .setConfirmText("确定")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
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
    }

    /**
     * 清理缓存
     */
    private void clearCache() {
        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("提示")
                .setContentText("清理所有缓存吗？")
                .setConfirmText("确定")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        showTips("清理中...", TipType.LoadingShow);
                        FileTool.DeleteFolder(CpigeonConfig.CACHE_FOLDER, false);
                        CacheManager.delete();
                        try {
                            DbManager db = x.getDb(CpigeonConfig.getDataDb());
                            db.delete(MatchInfo.class, WhereBuilder.b()
                                    .and("lx", "=", "gp")
                                    .and("st", "<", DateTool.dateTimeToStr(new Date(new Date().getTime() - 1000 * 60 * 60 * 24 * (1 + CpigeonConfig.LIVE_DAYS_GP)))));
                            db.delete(MatchInfo.class, WhereBuilder.b().and("lx", "=", "xh").and("st", "<", DateTool.dateTimeToStr(new Date(new Date().getTime() - 1000 * 60 * 60 * 24 * (1 + CpigeonConfig.LIVE_DAYS_XH)))));
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        showTips(null, TipType.LoadingHide);
                        showTips("清理完成", TipType.DialogSuccess);
                        initData();
                    }
                }).setCancelText("取消");
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * 检查新版本
     */
    private void checkNewVersion() {
        //更新检查
        if (mUpdateManager == null) {
            mUpdateManager = new UpdateManager(mContext);
            mUpdateManager.setOnInstallAppListener(new UpdateManager.OnInstallAppListener() {
                @Override
                public void onInstallApp() {
                    mEntryInstall = true;
                }
            });
            mUpdateManager.setOnCheckUpdateInfoListener(new UpdateManager.OnCheckUpdateInfoListener() {
                @Override
                public void onGetUpdateInfoStart() {
                    showTips("检查更新中...", TipType.LoadingShow);
                }

                @Override
                public boolean onGetUpdateInfoEnd(List<UpdateManager.UpdateInfo> updateInfos) {
                    showTips(null, TipType.LoadingHide);
                    return false;
                }

                @Override
                public void onNotFoundUpdate() {
                    showTips("暂无更新", TipType.ToastShort);
                }

                @Override
                public void onHasUpdate(UpdateManager.UpdateInfo updateInfo) {

                }

                @Override
                public void onDownloadStart() {

                }
            });
        }
        mUpdateManager.checkUpdate();
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
