package com.cpigeon.app.commonstandard.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.utils.NetUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;

/**
 * Created by chenshuai on 2017/4/15.
 * <p>
 * 可刷新，可翻页的Activity
 */

public abstract class BasePageTurnActivity<Pre extends BasePresenter, Adapter extends BaseQuickAdapter<DataBean, BaseViewHolder>, DataBean>
        extends BaseActivity<Pre>
        implements IPageTurn<DataBean>, IRefresh, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.recyclerview)
    protected RecyclerView recyclerview;
    @BindView(R.id.swiperefreshlayout)
    protected SwipeRefreshLayout swiperefreshlayout;
    @BindView(R.id.viewstub_empty)
    ViewStub viewstubEmpty;
    View mEmptyTip;
    TextView mEmptyTipTextView;

    boolean canLoadMore = true, isRefreshing = false, isMoreDateLoading = false;
    int pageindex = 1, pagesize = 10;

    @Override
    public boolean isMoreDataLoading() {
        return isMoreDateLoading;
    }

    @Override
    public boolean isRefreshing() {
        return isRefreshing;
    }

    Adapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.layout_com_toolbar_swiperefreshlayout_recyclerview;
    }

    @Override
    public void initView() {
        toolbar.setTitle(getTitleName());
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
        pagesize = getDefaultPageSize();
        if (isNetworkConnected()) onRefresh();
        else
            showTips("网络连接已断开", TipType.View);
    }


    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {
        if (mAdapter == null || mAdapter.getData().size() == 0) {
            loadDataByPresenter();
        }
    }

    @Override
    protected void onNetworkDisConnected() {
        hideRefreshLoading();
        showTips("网络连接断开,请打开网络连接", TipType.SnackbarLong);
    }

    @Override
    public void showRefreshLoading() {
        if (isNetworkConnected()) {
            if (swiperefreshlayout == null || swiperefreshlayout.isRefreshing()) return;
            swiperefreshlayout.setRefreshing(true);
        } else {
            onNetworkDisConnected();
        }
    }

    @Override
    public void hideRefreshLoading() {
        isRefreshing = false;
        if (swiperefreshlayout != null) {
            if (swiperefreshlayout.isRefreshing())
                swiperefreshlayout.setRefreshing(false);
            swiperefreshlayout.setEnabled(true);
        }
    }

    @Override
    public void showEmptyData() {
        showTips(getEmptyDataTips(), TipType.View);
    }

    @Override
    public boolean showTips(String tip, TipType tipType) {
        if (tipType == TipType.View) {
            if (mEmptyTip == null) mEmptyTip = viewstubEmpty.inflate();
            if (mEmptyTipTextView == null)
                mEmptyTipTextView = (TextView) mEmptyTip.findViewById(R.id.tv_empty_tips);
            mEmptyTipTextView.setText(tip);
            return true;
        }
        switch (tipType) {
            case SnackbarShort:
                final Snackbar snackbar = Snackbar.make(getWindow().getDecorView(), tip, Snackbar.LENGTH_SHORT);
                snackbar.setAction("去设置", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                        // 跳转到系统的网络设置界面
                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivity(intent);
                    }
                }).show();
                return true;
            case SnackbarLong:
                final Snackbar snackbar1 = Snackbar.make(getWindow().getDecorView(), tip, Snackbar.LENGTH_LONG);
                snackbar1.setAction("去设置", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar1.dismiss();
                        // 跳转到系统的网络设置界面
                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivity(intent);
                    }
                }).show();
                return true;
        }
        return super.showTips(tip, tipType);
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
        mAdapter = getNewAdapterWithNoData();
        mAdapter.setOnLoadMoreListener(this, recyclerview);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        recyclerview.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(true);
        pageindex = 1;
    }

    @Override
    public void showMoreData(List<DataBean> dataBeen) {
        hideRefreshLoading();
        if (getPageIndex() == 1 && (dataBeen == null || dataBeen.size() == 0))
            showEmptyData();
        if (dataBeen != null)
            mAdapter.addData(dataBeen);

        canLoadMore = dataBeen != null && dataBeen.size() == getPageSize();
        Logger.d("canLoadMore=" + canLoadMore);
        if (canLoadMore) {
            pageindex++;
        } else {
            mAdapter.loadMoreEnd(false);
        }
//        mAdapter.setEnableLoadMore(canLoadMore);
        isMoreDateLoading = isRefreshing = false;
    }

    @Override
    public boolean canLoadMoreData() {
        return canLoadMore;
    }

    @Override
    public void loadMoreComplete() {
        isMoreDateLoading = false;
        mAdapter.loadMoreComplete();//完成加载
        mAdapter.setEnableLoadMore(true);
        if (mEmptyTip != null) mEmptyTip.setVisibility(View.GONE);
    }

    @Override
    public void loadMoreFail() {
        isMoreDateLoading = false;
        mAdapter.loadMoreFail();
        mAdapter.setEnableLoadMore(true);
    }

    @Override
    public void onRefresh() {
        iniPageAndAdapter();
        isRefreshing = true;
        loadDataByPresenter();
    }


    @Override
    public void onLoadMoreRequested() {
        if (canLoadMore) {
            swiperefreshlayout.setEnabled(false);
            isMoreDateLoading = true;
            loadDataByPresenter();
        } else {
            mAdapter.setEnableLoadMore(false);
        }
    }

    /**
     * 获取Activity Title
     *
     * @return
     */
    @NonNull
    public abstract String getTitleName();

    /**
     * 获取默认的页面大小
     *
     * @return
     */
    public abstract int getDefaultPageSize();

    /**
     * 获取空数据的提示文字
     *
     * @return
     */
    protected abstract String getEmptyDataTips();

    /**
     * 获取adapter
     * <p>return new Adapter(null);<p/>
     *
     * @return
     */
    public abstract Adapter getNewAdapterWithNoData();

    /**
     * 通过Presenter 加载数据
     */
    protected abstract void loadDataByPresenter();
}
