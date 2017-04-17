package com.cpigeon.app.modular.matchlive.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseLazyLoadFragment;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.model.bean.MatchReportXH;
import com.cpigeon.app.modular.matchlive.presenter.RaceReportPre;
import com.cpigeon.app.modular.matchlive.view.activity.RaceReportActivity;
import com.cpigeon.app.modular.matchlive.view.activity.SearchActivity;
import com.cpigeon.app.modular.matchlive.view.adapter.RaceReportAdapter;
import com.cpigeon.app.utils.customview.SaActionSheetDialog;
import com.cpigeon.app.utils.customview.SearchEditText;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/15.
 */

public class ReportDataFragment extends BaseLazyLoadFragment<RaceReportPre> implements IReportData, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.searchEditText_race_detial)
    SearchEditText searchEditTextRaceDetial;
    @BindView(R.id.list_header_race_detial_table_header_1)
    TextView listHeaderRaceDetialTableHeader1;
    @BindView(R.id.list_header_race_detial_table_header_2)
    TextView listHeaderRaceDetialTableHeader2;
    @BindView(R.id.list_header_race_detial_table_header_3)
    TextView listHeaderRaceDetialTableHeader3;
    @BindView(R.id.layout_list_table_header)
    LinearLayout layoutListTableHeader;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;
    @BindView(R.id.viewstub_empty)
    ViewStub viewstubEmpty;
    private MatchInfo matchInfo;
    private String sKey = "";//当前搜索关键字
    private int pagesize = 100;
    private RaceReportAdapter mAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.matchInfo = ((RaceReportActivity) context).getMatchInfo();
    }

    @Override
    protected void initView(View view) {
        mPresenter.loadRaceData();
        swiperefreshlayout.setOnRefreshListener(this);
        swiperefreshlayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new RaceReportAdapter(null);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Object item = ((RaceReportAdapter) adapter).getData().get(position);
                Logger.d(item.getClass().getName());
                if (item instanceof RaceReportAdapter.MatchTitleXHItem) {
//                    if (!"bs".equals(((RaceReportAdapter.MatchTitleXHItem) item).getMatchReportXH().getDt()))
//                        return;
                    if (((RaceReportAdapter.MatchTitleXHItem) item).isExpanded()) {
                        adapter.collapse(position);
                    } else {
                        adapter.expand(position);
                    }
                } else if (item instanceof RaceReportAdapter.MatchDetialXHItem) {
                    MatchReportXH mi = ((RaceReportAdapter.MatchDetialXHItem) item).getSubItem(0);
//                    if (mi != null && !"jg".equals(mi.getDt())) {
//                        Intent intent = new Intent(getActivity(), RaceReportActivity.class);
//                        Bundle bundle = new Bundle();                           //创建Bundle对象
//                        bundle.putSerializable("matchinfo", mi);     //装入数据
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                        return;
//                    }
                }
            }
        });
        recyclerview.addOnItemTouchListener(new OnItemLongClickListener() {
            @Override
            public void onSimpleItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                Object item = ((RaceReportAdapter) adapter).getData().get(position);
                if (item instanceof RaceReportAdapter.MatchTitleXHItem) {
//                    final String s = ((RaceReportAdapter.MatchTitleXHItem) item).getMatchReportXH().getMc();

                }
            }
        });
        recyclerview.setAdapter(mAdapter);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_race_data;
    }


    @Override
    protected RaceReportPre initPresenter() {
        return new RaceReportPre(this);
    }

    @Override
    protected boolean isCanDettach() {
        return true;
    }

    @Override
    protected void lazyLoad() {

    }

    @OnClick(R.id.searchEditText_race_detial)
    public void onViewClicked() {
    }


    @Override
    public void showData(List list) {
        Logger.e(list.size() + "");
//        mAdapter = new RaceReportAdapter(RaceReportAdapter.getXH(list));
//        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.addData(RaceReportAdapter.getXH(list));

    }

    @Override
    public String getMatchType() {
        return matchInfo.getLx();
    }

    @Override
    public String getSsid() {
        return matchInfo.getSsid();
    }

    @Override
    public String getFoot() {
        return "";
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public boolean hascz() {
        return matchInfo.isMatch();
    }

    @Override
    public int getPager() {
        return 1;
    }

    @Override
    public int getPagerSize() {
        return pagesize;
    }

    @Override
    public int czIndex() {
        return -1;
    }

    @Override
    public String sKey() {
        return sKey;
    }

    @Override
    public void onRefresh() {

    }
}
