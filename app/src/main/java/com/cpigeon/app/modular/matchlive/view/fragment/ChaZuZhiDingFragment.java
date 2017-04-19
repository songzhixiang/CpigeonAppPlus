package com.cpigeon.app.modular.matchlive.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseLazyLoadFragment;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.presenter.ChaZuReportPre;
import com.cpigeon.app.modular.matchlive.view.activity.RaceReportActivity;
import com.cpigeon.app.modular.matchlive.view.adapter.ChaZuAdapter;
import com.cpigeon.app.modular.matchlive.view.fragment.viewdao.IChaZuReport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 插组指定Fragment
 * Created by Administrator on 2017/4/15.
 */

public class ChaZuZhiDingFragment extends BaseLazyLoadFragment<ChaZuReportPre> implements IChaZuReport {


    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.viewstub_empty)
    ViewStub viewstubEmpty;
    private MatchInfo matchInfo;
    private ChaZuAdapter mAdapter;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.matchInfo = ((RaceReportActivity) context).getMatchInfo();
    }

    @Override
    protected ChaZuReportPre initPresenter() {
        return new ChaZuReportPre(this);
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
        return R.layout.layout_com_recyclerview;
    }

    @Override
    protected void lazyLoad() {
        mPresenter.loadChaZuReport();
    }

    @Override
    public void showChaZuBaoDaoView(List list) {
        mAdapter = new ChaZuAdapter(list,1);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        recyclerview.setAdapter(mAdapter);
    }


    @Override
    public String getLx() {
        return matchInfo.getLx();
    }

    @Override
    public String getSsid() {
        return matchInfo.getSsid();
    }


}
