package com.cpigeon.app;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.adapter.ContentFragmentAdapter;
import com.cpigeon.app.modular.footsearch.view.fragment.FootSearchFragment;
import com.cpigeon.app.modular.home.view.fragment.HomeFragment;
import com.cpigeon.app.modular.home.view.fragment.viewdao.IHomeView;
import com.cpigeon.app.modular.home.model.bean.AD;
import com.cpigeon.app.modular.matchlive.view.fragment.MatchLiveFragment;
import com.cpigeon.app.modular.usercenter.view.fragment.UserCenterFragment;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.NetUtils;
import com.cpigeon.app.utils.StatusBarTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements IHomeView,  BottomNavigationBar.OnTabSelectedListener {


    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;
    private OnMatchTypeChangeListener onMatchTypeChangeListener;
    private int lastTabIndex = -1;//当前页面索引

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
    public void setOnMatchTypeChangeListener(OnMatchTypeChangeListener onMatchTypeChangeListener) {
        this.onMatchTypeChangeListener = onMatchTypeChangeListener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initPresenter() {

    }

    public void initView() {
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
            mBottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.ic_launcher, "首页").setActiveColorResource(R.color.colorPrimary).setBadgeItem(numberBadgeItem))
                    .addItem(new BottomNavigationItem(R.mipmap.ic_launcher, "直播").setActiveColorResource(R.color.colorPrimary))
//                    .addItem(new BottomNavigationItem(R.drawable.svg_ic_cpigeon_group, "微鸽圈").setActiveColorResource(R.color.colorPrimary))
                    .addItem(new BottomNavigationItem(R.mipmap.ic_launcher, "查询").setActiveColorResource(R.color.colorPrimary))
                    .addItem(new BottomNavigationItem(R.mipmap.ic_launcher, "我的").setActiveColorResource(R.color.colorPrimary))
                    .setFirstSelectedPosition(laseSelectedPosition > 4 ? 4 : laseSelectedPosition)
                    .initialise();
        }


        mBottomNavigationBar.setTabSelectedListener(this);
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
//            case 2:
//                if (mCpigeonGroupFragment == null)
//                {
//                    mCpigeonGroupFragment = new CpigeonGroupFragment();
//                    transaction.add(R.id.view_pager,mCpigeonGroupFragment);
//                }else {
//                    transaction.show(mCpigeonGroupFragment);
//                }
//                break;
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
        if (index == 0) {
//            homeFragment.loadMatchinfo();
        }
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
            CommonTool.exitApp(this);
        }

        StatusBarTool.setWindowStatusBarColor(this, getResources().getColor(lastTabIndex == 3 ? R.color.user_center_header_top : R.color.colorPrimary));
//        setAlias();
    }

    @Override
    public void showAd(List<AD> adList) {

    }

    @Override
    public void showMatchLiveData(List list) {

    }

    /**
     * 比赛类型切换的监听器
     */
    public interface OnMatchTypeChangeListener {
        void onChanged(String lastType, String currType);
    }

    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
//    private void setAlias() {
//
//        String alias = String.valueOf(CpigeonData.getInstance().getUserId(mContext));
//        if (TextUtils.isEmpty(alias)) {
//            return;
//        }
//        if (alias.equals(SharedPreferencesTool.Get(mContext, "jpush_alia", ""))) {
//            return;
//        }
//        // 调用 Handler 来异步设置别名
//        mJpushHandler.sendMessage(mJpushHandler.obtainMessage(MSG_SET_ALIAS, alias));
//    }
}
