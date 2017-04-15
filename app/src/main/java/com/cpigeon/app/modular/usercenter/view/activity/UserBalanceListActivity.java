package com.cpigeon.app.modular.usercenter.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.modular.usercenter.model.bean.CpigeonRechargeInfo;
import com.cpigeon.app.modular.usercenter.presenter.UserBalanceListPresenter;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.IUserBalanceListView;
import com.cpigeon.app.modular.usercenter.view.adapter.ScoreAdapter;
import com.cpigeon.app.modular.usercenter.view.adapter.UserBalanceAdapter;
import com.cpigeon.app.utils.NetUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/10.
 */

public class UserBalanceListActivity extends BaseActivity<UserBalanceListPresenter> implements IUserBalanceListView, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;

    int pageindex = 1, pagesize = 7;

    boolean canLoadMore = true;
    UserBalanceAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.layout_toolbar_swiperefreshlayout_recyclerview;
    }

    @Override
    public UserBalanceListPresenter initPresenter() {
        return new UserBalanceListPresenter(this);
    }

    @Override
    public void initView() {
        toolbar.setTitle("充值记录");
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        swiperefreshlayout.setOnRefreshListener(this);
        swiperefreshlayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        swiperefreshlayout.setEnabled(false);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        iniPageAndAdapter();
        mPresenter.loadUserBalancePage();
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    public void showRefreshLoading() {
        if (swiperefreshlayout.isRefreshing()) return;
        swiperefreshlayout.setRefreshing(true);
    }

    @Override
    public void hideRefreshLoading() {
        swiperefreshlayout.setRefreshing(true);
    }

    @Override
    public void showEmptyData() {

    }

    @Override
    public int getPageIndex() {
        return pageindex;
    }

    @Override
    public int getPageSize() {
        return pagesize;
    }

    @Override
    public void iniPageAndAdapter() {
        mAdapter = new UserBalanceAdapter(null);
        mAdapter.setOnLoadMoreListener(this, recyclerview);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        recyclerview.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(true);
        pageindex = 1;
    }

    @Override
    public void showMoreData(List<CpigeonRechargeInfo.DataBean> dataBeen) {
        swiperefreshlayout.setRefreshing(false);//停止刷新
        swiperefreshlayout.setEnabled(true);

        mAdapter.addData(dataBeen);
        mAdapter.loadMoreComplete();//完成加载

        canLoadMore = dataBeen != null && dataBeen.size() == getPageSize();
        Logger.d("canLoadMore=" + canLoadMore);
        if (canLoadMore) {
            pageindex++;
        } else {
            mAdapter.loadMoreEnd(false);
        }
        mAdapter.setEnableLoadMore(canLoadMore);
    }

    @Override
    public boolean canLoadMoreData() {
        return canLoadMore;
    }

    @Override
    public void onLoadMoreRequested() {
        if (canLoadMore) {
            swiperefreshlayout.setEnabled(false);
            mPresenter.loadUserBalancePage();
        } else {
            mAdapter.setEnableLoadMore(false);
        }
    }

    @Override
    public void onRefresh() {
        iniPageAndAdapter();
        mPresenter.loadUserBalancePage();
    }
}
