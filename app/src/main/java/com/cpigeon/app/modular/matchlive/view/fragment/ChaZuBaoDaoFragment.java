package com.cpigeon.app.modular.matchlive.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseLazyLoadFragment;
import com.cpigeon.app.modular.matchlive.model.bean.Bulletin;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.presenter.ChaZuReportPre;
import com.cpigeon.app.modular.matchlive.view.activity.RacePigeonsActivity;
import com.cpigeon.app.modular.matchlive.view.activity.RaceReportActivity;
import com.cpigeon.app.modular.matchlive.view.adapter.ChaZuAdapter;
import com.cpigeon.app.modular.matchlive.view.fragment.viewdao.IChaZuReport;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 插组报道Fragment
 * Created by Administrator on 2017/4/15.
 */

public class ChaZuBaoDaoFragment extends BaseLazyLoadFragment<ChaZuReportPre> implements IChaZuReport {
    private int CURRENT_DATA_TYPE;//当前数据类型
    private final static int DATA_TYPE_CZBD = 2;//数据类型-插组报道
    private final static int DATA_TYPE_BDSJ = 1;//数据类型-报道数据
    private final static int DATA_TYPE_SLQD = 3;//数据类型-上笼清单
    private final static int DATA_TYPE_CZZD = 4;//数据类型-插组指定
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.viewstub_empty)
    ViewStub viewstubEmpty;
    private MatchInfo matchInfo;
    private ChaZuAdapter mAdapter;
    private Bulletin mBulletin;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.matchInfo = ((RaceReportActivity) context).getMatchInfo();
       // this.bulletin = ((RaceReportActivity)context).getBulletin();
        ((RaceReportActivity)context).setOnLoadComplete(new RaceReportActivity.onLoadComplete() {
            @Override
            public void onSuccess(Bulletin bulletin) {
                mBulletin = bulletin;
            }
        });
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
        CURRENT_DATA_TYPE = 2;
        mAdapter = new ChaZuAdapter(list,0);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), RacePigeonsActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("matchinfo", matchInfo);
                b.putInt("datatype", CURRENT_DATA_TYPE == DATA_TYPE_CZBD ? RacePigeonsActivity.DATA_TYPE_REPORT : RacePigeonsActivity.DATA_TYPE_PIGEONS);
                b.putInt("czindex", position);//组别
                b.putSerializable("czmap", (ArrayList) adapter.getData());//插组统计数据
                b.putInt("czposition", position);//指定数量
                b.putString("bulletin",mBulletin.getContent());
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
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
