package com.cpigeon.app.modular.matchlive.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItemAdapter;
import com.cpigeon.app.commonstandard.view.adapter.fragmentpager.FragmentPagerItems;
import com.cpigeon.app.modular.matchlive.model.bean.Bulletin;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.presenter.RaceReportPre;
import com.cpigeon.app.modular.matchlive.view.activity.viewdao.IRaceReportView;
import com.cpigeon.app.modular.matchlive.view.fragment.ChaZuBaoDaoFragment;
import com.cpigeon.app.modular.matchlive.view.fragment.ChaZuZhiDingFragment;
import com.cpigeon.app.modular.matchlive.view.fragment.JiGeDataFragment;
import com.cpigeon.app.modular.matchlive.view.fragment.RaceDetailsFragment;
import com.cpigeon.app.modular.matchlive.view.fragment.ReportDataFragment;
import com.cpigeon.app.utils.NetUtils;
import com.cpigeon.app.utils.customview.MarqueeTextView;
import com.cpigeon.app.utils.customview.smarttab.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2017/4/10.
 */

public class RaceReportActivity extends BaseActivity<RaceReportPre> implements IRaceReportView, RaceDetailsFragment.DialogFragmentDataImpl {
    @BindView(R.id.race_details_marqueetv)
    MarqueeTextView raceDetailsMarqueetv;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.race_report_smartTabLayout)
    SmartTabLayout mSmartTabLayout;
    @BindView(R.id.list_header_race_detial_gg)
    MarqueeTextView listHeaderRaceDetialGg;
    @BindView(R.id.layout_gg)
    LinearLayout layoutGg;
    @BindView(R.id.race_report_viewpager)
    ViewPager mViewPager;

    ///////////////////////////////////////////////////////////////////////////
    // 视图
    ///////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////
    // 适配器和数据
    ///////////////////////////////////////////////////////////////////////////
    private MatchInfo matchInfo;//赛事信息
    private Bundle bundle;
    private Intent intent;
    private FragmentPagerAdapter mFragmentPagerAdapter;

    public Bulletin getBulletin() {
        return bulletin;
    }

    private Bulletin bulletin;
    private String loadType;
    private String tablayout_seconde_name;

    @Override
    public int getLayoutId() {
        return R.layout.activity_race_details;
    }

    @Override
    public RaceReportPre initPresenter() {
        return new RaceReportPre(this);
    }

    @Override
    public void initView() {
        intent = this.getIntent();
        bundle = intent.getExtras();
        matchInfo = (MatchInfo) bundle.getSerializable("matchinfo");
        loadType = bundle.getString("loadType");
        if (TextUtils.isEmpty(loadType)) {
            loadType = matchInfo.getLx();
        }

//        Logger.e("当前页面加载的数据是：" + loadType + "类型的数据");
        if ("xh".equals(loadType)) {
            tablayout_seconde_name = "集鸽数据";
        } else if ("gp".equals(loadType)) {
            tablayout_seconde_name = "上笼数据";
        }
        mPresenter.showBulletin();
        mPresenter.addRaceClickCount();
        if (matchInfo != null) {
//            Logger.e("matchinfo" + matchInfo.getBsmc());
            if (bundle.getString("jigesuccess") != null) {
                mFragmentPagerAdapter = new FragmentPagerItemAdapter(
                        getSupportFragmentManager(), FragmentPagerItems.with(this)
                        .add(tablayout_seconde_name, JiGeDataFragment.class)
                        .add("插组指定", ChaZuZhiDingFragment.class)
                        .create());
            } else {
                mFragmentPagerAdapter = new FragmentPagerItemAdapter(
                        getSupportFragmentManager(), FragmentPagerItems.with(this)
                        .add("报到数据", ReportDataFragment.class)
                        .add("插组报到", ChaZuBaoDaoFragment.class)
                        .add(tablayout_seconde_name, JiGeDataFragment.class)
                        .add("插组指定", ChaZuZhiDingFragment.class)
                        .create());
            }

            mViewPager.setAdapter(mFragmentPagerAdapter);
            mSmartTabLayout.setViewPager(mViewPager);
        }
        if (mToolbar != null) {
            mToolbar.setTitle("");
            raceDetailsMarqueetv.setText(matchInfo.getMc());
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 1 || i == 3) {

                } else {

                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
//        searchEditText.setOnSearchClickListener(this);


    }

    public String getLoadType() {
        return loadType;
    }

    @Override
    public MatchInfo getMatchInfo() {
        return matchInfo;
    }

    @Override
    public String getSsid() {
        return matchInfo.getSsid();
    }

    @Override
    public String getLx() {
        return matchInfo.getLx();
    }

    @Override
    public void showBulletin(Bulletin bulletin) {
        this.bulletin = bulletin;
        if (bulletin != null && !TextUtils.isEmpty(bulletin.getContent().trim())) {
            layoutGg.setVisibility(View.VISIBLE);
            listHeaderRaceDetialGg.setText("公告:" + bulletin.getContent());
        } else {
            layoutGg.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_race_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //android.R.id.home是Android内置home按钮的id
                finish();
                break;
            case R.id.action_save:

                break;
            case R.id.action_details:
                showDialogFragment();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {
        showTips("网络加载失败", TipType.DialogError);
    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @OnClick({R.id.layout_gg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_gg:
                if (bulletin == null || TextUtils.isEmpty(bulletin.getContent())) {
                    return;
                }
                SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
                dialog.setTitleText("公告");
                dialog.setContentText(bulletin.getContent());
                dialog.setCancelable(true);
                dialog.show();
                break;

        }
    }

    @Override
    public void showMessage(String msg) {

    }

    public void showDialogFragment() {
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();

        Fragment fragment = getSupportFragmentManager().findFragmentByTag("dialogFragment");
        if (fragment != null) {
            mFragmentTransaction.remove(fragment);
        }
        RaceDetailsFragment detailsFragment = RaceDetailsFragment.newInstance("直播数据");
        detailsFragment.show(mFragmentTransaction, "dialogFragment");

    }

}
