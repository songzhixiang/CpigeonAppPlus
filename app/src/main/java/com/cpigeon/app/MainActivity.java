package com.cpigeon.app;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.cpigeon.app.commonstandard.AppManager;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.adapter.ContentFragmentAdapter;
import com.cpigeon.app.modular.footsearch.view.fragment.FootSearchFragment;
import com.cpigeon.app.modular.home.view.fragment.HomeFragment;
import com.cpigeon.app.modular.matchlive.view.fragment.MatchLiveFragment;
import com.cpigeon.app.modular.usercenter.view.activity.LoginActivity;
import com.cpigeon.app.modular.usercenter.view.fragment.UserCenterFragment;
import com.cpigeon.app.service.MainActivityService;
import com.cpigeon.app.utils.Const;
import com.cpigeon.app.utils.DateTool;
import com.cpigeon.app.utils.NetUtils;
import com.cpigeon.app.utils.StatusBarTool;
import com.cpigeon.app.utils.UpdateManager;
import com.cpigeon.app.utils.app.ControlManager;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    private static boolean isExit = false;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;
    private UpdateManager mUpdateManager;
    private ControlManager mControlManager;
    private OnMatchTypeChangeListener onMatchTypeChangeListener;
    private int lastTabIndex = 0;//当前页面索引
    private String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE};
    //主页
    private HomeFragment homeFragment;
    //直播
    private MatchLiveFragment matchLiveFragment;
    //个人中心
    private UserCenterFragment userCenterFragment;
    //足环查询
    private FootSearchFragment footSearchFragment;
    //鸽友圈
    private List<Fragment> mFragments = new ArrayList<>();
    private ContentFragmentAdapter mContentFragmentAdapter;
    private BadgeItem numberBadgeItem;
    private int laseSelectedPosition = 0;
    private boolean mHasUpdata = false;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    MainActivityService mainActivityService;
    MainActivityService.OnDeviceLoginCheckListener onDeviceLoginCheckListener = new MainActivityService.OnDeviceLoginCheckListener() {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        @Override
        public boolean onOtherDeviceLogin(final MainActivityService.UseDevInfo useDevInfo) {
            if (useDevInfo == null) return false;

            Logger.d(useDevInfo.getString());
            Date time = DateTool.strToDateTime(useDevInfo.getTime());
            StringBuilder builder = new StringBuilder();
            builder.append("您的账号于")
                    .append(dateTimeFormat.format(time))
                    .append("在另一台")
                    .append(useDevInfo.getType())
                    .append(TextUtils.isEmpty(useDevInfo.getDevinfo()) ? "" : "(" + useDevInfo.getDevinfo() + ")")
                    .append("设备登录。如非本人操作，则密码可能已泄漏，建议尽快修改密码。");
            SweetAlertDialog dialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
//                    dialog.getWindow().setType(WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW);
            dialog.setTitleText("下线通知")
                    .setContentText(builder.toString())
                    .setConfirmText("确定")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
                        }
                    });
            dialog.setCancelable(false);
            dialog.show();
            clearLoginInfo();
            return true;
        }
    };
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logger.d(name);
            Logger.d(service);
            mainActivityService = ((MainActivityService.ThisBinder) service).getService();
            mainActivityService.setOnDeviceLoginCheckListener(onDeviceLoginCheckListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.d(name);
            mainActivityService = null;
        }
    };

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE})
    void sysytemAlertWindow() {

    }

    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE})
    void systemAlertWindowOnShowRationale(final PermissionRequest request) {
        showRequest(request);
    }

    private void showRequest(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(true)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        request.cancel();
                    }
                })
                .setMessage("我们需要一些权限，以便您更好的体验")
                .show();
    }

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE})
    void systemAlertWindowOnPermissionDenied() {
        showTips("权限被拒绝了", TipType.ToastShort);
    }

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE})
    void systemAlertWindowOnNeverAskAgain() {
        showTips("权限不再提示", TipType.ToastShort);
    }


    public void setOnMatchTypeChangeListener(OnMatchTypeChangeListener onMatchTypeChangeListener) {
        this.onMatchTypeChangeListener = onMatchTypeChangeListener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }


    public void checkUpData() {
        //更新检查
        mUpdateManager = new UpdateManager(mContext);
        mUpdateManager.setOnInstallAppListener(new UpdateManager.OnInstallAppListener() {
            @Override
            public void onInstallApp() {
                mHasUpdata = true;
            }
        });
        mUpdateManager.checkUpdate();
    }

    /**
     * 检查当前版本是否可用
     */
    public void checkAvailableVersion() {
        mControlManager = new ControlManager(mContext);
        mControlManager.checkIsAvailableVersion(new ControlManager.OnCheckedListener() {
            @Override
            public void onChecked(boolean isAvailableVersion) {
                if (!isAvailableVersion) {
                    SweetAlertDialog dialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示")
                            .setContentText("当前版本已停止服务\n请您到中鸽网下载最新版本.")
                            .setConfirmText("去中鸽网下载")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(Const.URL_APP_DOWNLOAD + "?autostart=false"));
                                    startActivity(intent);

                                    sweetAlertDialog.dismiss();
                                    AppManager.getAppManager().AppExit();
                                }
                            })
                            .setCancelText("退出程序")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    AppManager.getAppManager().AppExit();
                                }
                            });
                    dialog.setCancelable(false);
                    dialog.show();
                }
            }
        });
    }

    public void initView() {
        MainActivityPermissionsDispatcher.sysytemAlertWindowWithCheck(this);
        homeFragment = new HomeFragment();
        matchLiveFragment = new MatchLiveFragment();
        userCenterFragment = new UserCenterFragment();
        footSearchFragment = new FootSearchFragment();
//        mCpigeonGroupFragment = new CpigeonGroupFragment();
        mFragments = new ArrayList<>();
        mFragments.add(homeFragment);
        mFragments.add(matchLiveFragment);
//        mFragments.add(mCpigeonGroupFragment);
        mFragments.add(footSearchFragment);
        mFragments.add(userCenterFragment);
        mContentFragmentAdapter = new ContentFragmentAdapter(getSupportFragmentManager(), mFragments);
        //设置limit
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mContentFragmentAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (lastTabIndex == position) return;
                mBottomNavigationBar.selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.svg_ic_menu_home_stroke, "首页").setActiveColorResource(R.color.colorPrimary).setBadgeItem(numberBadgeItem))
                    .addItem(new BottomNavigationItem(R.drawable.svg_ic_menu_racelive_stroke, "直播").setActiveColorResource(R.color.colorPrimary))
//                    .addItem(new BottomNavigationItem(R.drawable.svg_ic_cpigeon_group, "微鸽圈").setActiveColorResource(R.color.colorPrimary))
                    .addItem(new BottomNavigationItem(R.drawable.svg_ic_menu_search_stroke, "查询").setActiveColorResource(R.color.colorPrimary))
                    .addItem(new BottomNavigationItem(R.drawable.svg_ic_menu_user_stroke, "我的").setActiveColorResource(R.color.colorPrimary))
                    .setFirstSelectedPosition(laseSelectedPosition > 4 ? 4 : laseSelectedPosition)
                    .initialise();
        } else {
            mBottomNavigationBar
                    .addItem(new BottomNavigationItem(R.mipmap.ic_home_selected, "首页").setBadgeItem(numberBadgeItem))
                    .addItem(new BottomNavigationItem(R.mipmap.ic_matchlive_selected, "直播"))
                    .addItem(new BottomNavigationItem(R.mipmap.ic_search_selected, "查询"))
                    .addItem(new BottomNavigationItem(R.mipmap.ic_user_selected, "我的"))
                    .setFirstSelectedPosition(laseSelectedPosition > 4 ? 4 : laseSelectedPosition)
                    .initialise();
        }


        mBottomNavigationBar.setTabSelectedListener(this);
        if (!BuildConfig.DEBUG) {
            checkUpData();
        }
        checkAvailableVersion();
        // startService(new Intent(mContext.getApplicationContext(), CoreService.class));
        bindService(new Intent(mContext.getApplicationContext(), MainActivityService.class), conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }


    @Override
    public void onTabSelected(int position) {
        laseSelectedPosition = position;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        hideFragment(transaction);
        switch (position) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.viewpager, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }

                break;
            case 1:
                if (matchLiveFragment == null) {
                    matchLiveFragment = new MatchLiveFragment();
                    transaction.add(R.id.viewpager, matchLiveFragment);
                } else {
                    transaction.show(matchLiveFragment);
                }
                break;
            case 2:
                if (footSearchFragment == null) {
                    footSearchFragment = new FootSearchFragment();
                    transaction.add(R.id.viewpager, footSearchFragment);
                } else {
                    transaction.show(footSearchFragment);
                }
                break;
            case 3:
                if (userCenterFragment == null) {
                    userCenterFragment = new UserCenterFragment();
                    transaction.add(R.id.viewpager, userCenterFragment);
                } else {
                    transaction.show(userCenterFragment);
                }
                break;
        }
        transaction.commit();
        setCurrIndex(position);
    }

    private void setCurrIndex(int index) {
        if (index == lastTabIndex) return;
        StatusBarTool.setWindowStatusBarColor(this, getResources().getColor(index == 4 ? R.color.user_center_header_top : R.color.colorPrimary));
        lastTabIndex = index;
        mViewPager.setCurrentItem(index);
        onTabSelected(index);
    }


    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mHasUpdata) {
            AppManager.getAppManager().AppExit();
        }
        StatusBarTool.setWindowStatusBarColor(this, getResources().getColor(lastTabIndex == 3 ? R.color.user_center_header_top : R.color.colorPrimary));
    }


    /**
     * 比赛类型切换的监听器
     */
    public interface OnMatchTypeChangeListener {
        void onChanged(String lastType, String currType);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    public void onHomeItemClick(View v) {
        switch (v.getId()) {
            case R.id.layout_gpzb:
                if (onMatchTypeChangeListener != null)
                    onMatchTypeChangeListener.onChanged(matchLiveFragment.getCurrMatchType(), Const.MATCHLIVE_TYPE_GP);

                setCurrIndex(1);
                mBottomNavigationBar.selectTab(1);
                break;
            case R.id.layout_xhzb:
                if (onMatchTypeChangeListener != null)
                    onMatchTypeChangeListener.onChanged(matchLiveFragment.getCurrMatchType(), Const.MATCHLIVE_TYPE_XH);
                setCurrIndex(1);
                mBottomNavigationBar.selectTab(1);
                break;
            case R.id.layout_zhcx:
                onTabReselected(2);
                setCurrIndex(2);
                mBottomNavigationBar.selectTab(2);
                break;
            case R.id.layout_wdsc:
//                startActivity(new Intent(HomeActivity.this, CollectionActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    @Override
    public void onBackPressed() {
        if (lastTabIndex != 0) {
            onTabReselected(0);
            setCurrIndex(0);
            mBottomNavigationBar.selectTab(0);
        } else {
            if (!isExit) {
                isExit = true;
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Then_click_one_exit_procedure),
                        Toast.LENGTH_SHORT).show();
                // 利用handler延迟发送更改状态信息
                mHandler.sendEmptyMessageDelayed(0, 2000);
            } else {
                AppManager.getAppManager().AppExit();
            }
        }


    }
}
