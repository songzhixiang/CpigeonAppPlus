package com.cpigeon.app.modular.order.view.activity;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.modular.order.model.bean.CpigeonOrderInfo;
import com.cpigeon.app.modular.order.presenter.OrderPre;
import com.cpigeon.app.modular.order.view.activity.viewdao.IOrderView;
import com.cpigeon.app.modular.order.view.adapter.OrderAdapter;
import com.cpigeon.app.utils.NetUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/4/11.
 */

public class OrderActivity extends BaseActivity implements IOrderView, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private OrderAdapter mAdapter;

    @BindView(R.id.order_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.order_swiperefreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private OrderPre pre;
    private List<CpigeonOrderInfo> orderInfos;
    private int delayMillis = 1000;
    private int mCurrentCounter;
    private int mTotalCount = 8;
    private int pi = 1;
    private List<CpigeonOrderInfo> newOrderInfo = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_order;
    }

    @Override
    public void initPresenter() {
        pre = new OrderPre(this);
    }

    @Override
    public void initView() {
        mToolbar.setTitle("我的订单");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pre.loadOrder(0);
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    public void showOrder(List<CpigeonOrderInfo> orderInfos, int type) {

        this.orderInfos = orderInfos;
        mCurrentCounter = orderInfos.size();
        switch (type) {
            case 1:
                mAdapter.addData(orderInfos);
                break;
            case 0:
                mAdapter = new OrderAdapter(orderInfos);
                mCurrentCounter = mAdapter.getData().size();
                mAdapter.setOnLoadMoreListener(this, mRecyclerView);
                mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
                mRecyclerView.setAdapter(mAdapter);
                break;
        }

        for (CpigeonOrderInfo cpigeonOrderInfo : orderInfos) {
            newOrderInfo.add(cpigeonOrderInfo);
        }


    }

    @Override
    public int getPs() {
        return mTotalCount;
    }

    @Override
    public int getPi() {
        return pi;
    }

    @Override
    public String getQuery() {
        return null;
    }

    @Override
    public void onRefresh() {
        mAdapter.setEnableLoadMore(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.setNewData(orderInfos);
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter.setEnableLoadMore(true);
            }
        }, delayMillis);
    }

    @Override
    public void onLoadMoreRequested() {
        mSwipeRefreshLayout.setEnabled(false);
        if (mCurrentCounter < mTotalCount) {
            mAdapter.loadMoreEnd(true);

        } else {
            pi++;
            Logger.e("pi:" + pi);
            mAdapter.loadMoreEnd(false);
            pre.loadOrder(1);
            mAdapter.loadMoreComplete();

        }

    }
}


