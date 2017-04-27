package com.cpigeon.app.modular.matchlive.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BasePageTurnActivity;
import com.cpigeon.app.modular.matchlive.model.bean.Bulletin;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.model.bean.MatchReportGP;
import com.cpigeon.app.modular.matchlive.model.bean.MatchReportXH;
import com.cpigeon.app.modular.matchlive.presenter.RacePre;
import com.cpigeon.app.modular.matchlive.view.adapter.RaceReportAdapter;
import com.cpigeon.app.modular.matchlive.view.adapter.RaceXunFangAdapter;
import com.cpigeon.app.modular.matchlive.view.fragment.viewdao.IReportData;
import com.cpigeon.app.utils.NetUtils;
import com.cpigeon.app.utils.ViewExpandAnimation;
import com.cpigeon.app.utils.customview.MarqueeTextView;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 家飞测试和训放
 * Created by Administrator on 2017/4/23.
 */

public class RaceXunFangActivity extends BasePageTurnActivity<RacePre, RaceXunFangAdapter, MultiItemEntity> implements IReportData {

    @BindView(R.id.race_details_marqueetv)
    MarqueeTextView raceDetailsMarqueetv;
    @BindView(R.id.race_detial_info_detial_show)
    AppCompatImageView raceDetialInfoDetialShow;
    @BindView(R.id.race_detial_info_textview_racename)
    MarqueeTextView raceDetialInfoTextviewRacename;
    @BindView(R.id.race_detial_info_match_name_layout)
    RelativeLayout raceDetialInfoMatchNameLayout;
    @BindView(R.id.race_detial_match_info_title_area)
    TextView raceDetialMatchInfoTitleArea;
    @BindView(R.id.race_detial_match_info_content_area)
    TextView raceDetialMatchInfoContentArea;
    @BindView(R.id.layout_area)
    LinearLayout layoutArea;
    @BindView(R.id.race_detial_match_info_title_kj)
    TextView raceDetialMatchInfoTitleKj;
    @BindView(R.id.race_detial_match_info_content_kj)
    TextView raceDetialMatchInfoContentKj;
    @BindView(R.id.layout_kj)
    LinearLayout layoutKj;
    @BindView(R.id.race_detial_match_info_title_st)
    TextView raceDetialMatchInfoTitleSt;
    @BindView(R.id.race_detial_match_info_content_st)
    TextView raceDetialMatchInfoContentSt;
    @BindView(R.id.list_header_race_detial_gg)
    MarqueeTextView listHeaderRaceDetialGg;
    @BindView(R.id.layout_gg)
    LinearLayout layoutGg;
    @BindView(R.id.list_header_race_detial_table_header_1)
    TextView listHeaderRaceDetialTableHeader1;
    @BindView(R.id.list_header_race_detial_table_header_2)
    TextView listHeaderRaceDetialTableHeader2;
    @BindView(R.id.list_header_race_detial_table_header_3)
    TextView listHeaderRaceDetialTableHeader3;
    @BindView(R.id.layout_list_table_header)
    LinearLayout layoutListTableHeader;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private MatchInfo matchInfo;//赛事信息
    private Bundle bundle;
    private Intent intent;
    private String sKey = "";//当前搜索关键字

    public Bulletin getBulletin() {
        return bulletin;
    }

    private Bulletin bulletin;
    private String loadType;

    @Override
    public int getLayoutId() {
        return R.layout.activity_race_xunfang_details;
    }

    @Override
    public RacePre initPresenter() {
        return new RacePre(this);
    }


    @Override
    public void initView() {
        initData();
        initToolbar();
        initInfo();
        super.initView();
    }

    private void initInfo() {
        raceDetialInfoTextviewRacename.setText(matchInfo.computerBSMC());
        raceDetialMatchInfoContentArea.setText(!"".equals(matchInfo.getArea()) ? matchInfo.getArea() : "无");
        raceDetialMatchInfoContentSt.setText(matchInfo.getSt());
        if (matchInfo.getBskj() == 0)//家飞
        {
            layoutKj.setVisibility(View.GONE);
        } else {
            layoutKj.setVisibility(View.VISIBLE);
            raceDetialMatchInfoContentKj.setText(matchInfo.getBskj() + "Km");
        }
        listHeaderRaceDetialTableHeader1.setText("名次");
    }

    private void initToolbar() {
        toolbar.setTitle("");
        raceDetailsMarqueetv.setText(matchInfo.getMc());
    }

    private void initData() {
        intent = this.getIntent();
        bundle = intent.getExtras();
        matchInfo = (MatchInfo) bundle.getSerializable("matchinfo");
        loadType = bundle.getString("loadType");
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @NonNull
    @Override
    public String getTitleName() {
        return matchInfo.getMc();
    }

    @Override
    public int getDefaultPageSize() {
        return 100;
    }

    @Override
    protected String getEmptyDataTips() {
        return "没有报道数据";
    }

    @Override
    public RaceXunFangAdapter getNewAdapterWithNoData() {
        RaceXunFangAdapter adapter = new RaceXunFangAdapter(getMatchType());

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Object item = ((RaceXunFangAdapter) adapter).getData().get(position);
                Logger.d(item.getClass().getName());
                if (item instanceof RaceXunFangAdapter.MatchTitleGPItem) {
//                    if (!"bs".equals(((RaceReportAdapter.MatchTitleXHItem) item).getMatchReportXH().getDt()))
//                        return;
                    if (((RaceXunFangAdapter.MatchTitleGPItem) item).isExpanded()) {
                        adapter.collapse(position);
                    } else {
                        adapter.expand(position);
                    }
                } else if (item instanceof RaceXunFangAdapter.MatchDetialGPItem) {
                    MatchReportGP mi = ((RaceXunFangAdapter.MatchDetialGPItem) item).getSubItem(0);
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
        return adapter;
    }

    @Override
    protected void loadDataByPresenter() {
        mPresenter.loadRaceData(1);
    }

    @OnClick({R.id.race_detial_info_detial_show, R.id.race_detial_info_match_name_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.race_detial_info_detial_show:
                break;
            case R.id.race_detial_info_match_name_layout:

                break;
        }
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
    public int czIndex() {
        return -1;
    }

    @Override
    public String sKey() {
        return sKey;
    }


}
