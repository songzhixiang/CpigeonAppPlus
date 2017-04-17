package com.cpigeon.app.modular.matchlive.view.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseLazyLoadFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 插组指定Fragment
 * Created by Administrator on 2017/4/15.
 */

public class ChaZuZhiDingFragment extends BaseLazyLoadFragment {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;
    @BindView(R.id.viewstub_empty)
    ViewStub viewstubEmpty;
    Unbinder unbinder;

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.layout_com_swiperefreshlayout_recyclerview;
    }

    @Override
    protected void lazyLoad() {

    }

}
