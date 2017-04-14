package com.cpigeon.app.modular.order.view.activity;

import android.content.Intent;
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
import com.cpigeon.app.utils.CpigeonData;
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
    private int mTotalCount = 8;
    private int pi = 1;
    private boolean canLoadMore = true;

    BaseQuickAdapter.OnItemClickListener onItemClickListener = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            CpigeonOrderInfo orderInfo = mAdapter.getData().get(position);
            if (!orderInfo.ispaid() && "待支付".equals(orderInfo.getStatusName())) {
                Intent intent = new Intent(mContext, OrderPayActivity.class);
                intent.putExtra(OrderPayActivity.INTENT_DATA_KEY_ORDERINFO, orderInfo);
                startActivity(intent);
            }
        }
    };
    CpigeonData.OnWxPayListener onWxPayListener = new CpigeonData.OnWxPayListener() {
        @Override
        public void onPayFinished(int wxPayReturnCode) {
            if (wxPayReturnCode == ERR_OK)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initAdapterData();
                        pre.loadOrder();
                    }
                }, 400);
            else
                showTips("支付失败", TipType.ToastShort);
        }
    };

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
        CpigeonData.getInstance().addOnWxPayListener(onWxPayListener);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mSwipeRefreshLayout.setEnabled(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        initAdapterData();
        pre.loadOrder();
    }

    private void initAdapterData() {
        mAdapter = new OrderAdapter(null);
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(onItemClickListener);
        mAdapter.setEnableLoadMore(false);
        pi = 1;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    public void showOrder(List<CpigeonOrderInfo> orderInfos) {
        mSwipeRefreshLayout.setRefreshing(false);//停止刷新
        mSwipeRefreshLayout.setEnabled(true);

        mAdapter.addData(orderInfos);
        mAdapter.loadMoreComplete();//完成加载

        Logger.e("pi:" + pi);
        canLoadMore = orderInfos != null && orderInfos.size() == mTotalCount;
        if (canLoadMore) {
            pi++;
        } else {
            mAdapter.loadMoreEnd(false);
        }

        mAdapter.setEnableLoadMore(canLoadMore);
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
        initAdapterData();
        pre.loadOrder();
    }

    @Override
    public void onLoadMoreRequested() {
        if (canLoadMore) {
            mSwipeRefreshLayout.setEnabled(false);
            pre.loadOrder();
        } else {
            mAdapter.setEnableLoadMore(false);
        }
    }
}


