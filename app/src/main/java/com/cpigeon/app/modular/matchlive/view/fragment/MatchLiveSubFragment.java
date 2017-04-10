package com.cpigeon.app.modular.matchlive.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.presenter.MatchLiveSubPre;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/7.
 */

public class MatchLiveSubFragment extends BaseFragment implements IMatchSubView, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recyclerview_matchlive)
    RecyclerView mRecyclerView;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private MatchLiveAdapter matchLiveAdapter;
    private View mView;
    private List<MatchInfo> matchInfos;
    private int delayMillis = 1000;
    private static final int TOTAL_COUNTER = 18;
    private static final int PAGE_SIZE = 6;
    private MatchLiveSubPre pre = new MatchLiveSubPre(this);
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_matchlive_sub, container, false);
        ButterKnife.bind(this, mView);
        initView();
        pre.loadGPData(0);
        return mView;
    }


    private void initView() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void showGPData(List<MatchInfo> matchInfoList,int type) {
        this.matchInfos = new ArrayList<>();
        this.matchInfos = matchInfoList;
        matchLiveAdapter = new MatchLiveAdapter(matchInfoList);
        matchLiveAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(matchLiveAdapter);
    }

    @Override
    public void showXHData(List<MatchInfo> matchInfoList,int type) {

    }

    @Override
    public void setLoadType(int type) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }



    @Override
    public void onRefresh() {
        matchLiveAdapter.setEnableLoadMore(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                matchLiveAdapter.setNewData(matchInfos);
                mSwipeRefreshLayout.setRefreshing(false);
                matchLiveAdapter.setEnableLoadMore(true);
            }
        }, delayMillis);
    }
}
