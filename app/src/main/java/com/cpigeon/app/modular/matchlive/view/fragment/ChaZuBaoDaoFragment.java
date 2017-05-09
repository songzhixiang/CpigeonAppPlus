package com.cpigeon.app.modular.matchlive.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseLazyLoadFragment;
import com.cpigeon.app.modular.matchlive.model.bean.Bulletin;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.presenter.ChaZuReportPre;
import com.cpigeon.app.modular.matchlive.view.activity.RaceChaZuBaoDaoActivity;
import com.cpigeon.app.modular.matchlive.view.activity.RaceReportActivity;
import com.cpigeon.app.modular.matchlive.view.adapter.ChaZuAdapter;
import com.cpigeon.app.modular.matchlive.view.fragment.viewdao.IChaZuReport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 插组报到Fragment
 * Created by Administrator on 2017/4/15.
 */

public class ChaZuBaoDaoFragment extends BaseLazyLoadFragment<ChaZuReportPre> implements IChaZuReport {
    private int CURRENT_DATA_TYPE;//当前数据类型
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.viewstub_empty)
    ViewStub viewstubEmpty;
    private MatchInfo matchInfo;
    private ChaZuAdapter mAdapter;
    private Bulletin mBulletin;
    private String loadType;
    View mEmptyTip;
    TextView mEmptyTipTextView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initMatchinfo();
    }

    private void initMatchinfo() {
        if (matchInfo == null) {
            this.matchInfo = ((RaceReportActivity) getActivity()).getMatchInfo();
            this.loadType = ((RaceReportActivity) getActivity()).getLoadType();
        }
    }

    @Override
    protected ChaZuReportPre initPresenter() {
        return new ChaZuReportPre(this);
    }

    @Override
    protected boolean isCanDettach() {
        return true;
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
        mAdapter = new ChaZuAdapter(list, 0);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
//                if (((Map<String, Integer>) baseQuickAdapter.getItem(i)).get("gcys") > 0) {
                Intent intent = new Intent(getActivity(), RaceChaZuBaoDaoActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("matchinfo", matchInfo);
                b.putInt("czindex", i);//组别
                b.putString("loadType", loadType);
                b.putSerializable("czmap", (ArrayList) baseQuickAdapter.getData());//插组统计数据
                b.putInt("czposition", i);//指定数量
                if (((RaceReportActivity) getActivity()).getBulletin() != null) {
                    b.putString("bulletin", ((RaceReportActivity) getActivity()).getBulletin().getContent());
                }
                intent.putExtras(b);
                startActivity(intent);
//                }
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        if (mEmptyTip != null) mEmptyTip.setVisibility(View.GONE);
    }

    @Override
    public String getLx() {
        initMatchinfo();
        return matchInfo.getLx();
    }

    @Override
    public String getSsid() {
        initMatchinfo();
        return matchInfo.getSsid();
    }

    @Override
    public boolean showTips(String tip, TipType tipType) {
        if (tipType == TipType.View) {
            if (mEmptyTip == null) mEmptyTip = viewstubEmpty.inflate();
            if (mEmptyTipTextView == null)
                mEmptyTipTextView = (TextView) mEmptyTip.findViewById(R.id.tv_empty_tips);
            mEmptyTipTextView.setText(tip);
            mEmptyTip.setVisibility(View.VISIBLE);
            return true;
        }
        return super.showTips(tip, tipType);
    }

}
