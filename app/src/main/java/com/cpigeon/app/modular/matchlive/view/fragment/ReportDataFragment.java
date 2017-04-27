package com.cpigeon.app.modular.matchlive.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BasePageTurnFragment;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.model.bean.MatchReportGP;
import com.cpigeon.app.modular.matchlive.model.bean.MatchReportXH;
import com.cpigeon.app.modular.matchlive.presenter.RacePre;
import com.cpigeon.app.modular.matchlive.view.activity.RaceReportActivity;
import com.cpigeon.app.modular.matchlive.view.adapter.RaceReportAdapter;
import com.cpigeon.app.modular.matchlive.view.fragment.viewdao.IReportData;
import com.cpigeon.app.utils.customview.SaActionSheetDialog;
import com.cpigeon.app.utils.customview.SearchEditText;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/4/15.
 */

public class ReportDataFragment extends BasePageTurnFragment<RacePre, RaceReportAdapter, MultiItemEntity> implements IReportData {

    @BindView(R.id.list_header_race_detial_table_header_1)
    TextView listHeaderRaceDetialTableHeader1;
    @BindView(R.id.list_header_race_detial_table_header_2)
    TextView listHeaderRaceDetialTableHeader2;
    @BindView(R.id.list_header_race_detial_table_header_3)
    TextView listHeaderRaceDetialTableHeader3;
    @BindView(R.id.layout_list_table_header)
    LinearLayout layoutListTableHeader;
    @BindView(R.id.searchEditText)
    SearchEditText searchEditText;
    private MatchInfo matchInfo;
    private String sKey = "";//当前搜索关键字
    boolean isSearch = false;

    @Override
    public void onRefresh() {
        if (!isSearch)
            sKey = "";
        super.onRefresh();
        isSearch = false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.matchInfo = ((RaceReportActivity) context).getMatchInfo();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_race_data;
    }

    @Override
    protected RacePre initPresenter() {
        return new RacePre(this);
    }

    @Override
    protected boolean isCanDettach() {
        return true;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        initSearch();
        if ("gp".equals(this.matchInfo.getLx())) {
            listHeaderRaceDetialTableHeader1.setText("名次");
        }
    }

    private void initSearch() {
        searchEditText.setOnSearchClickListener(new SearchEditText.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view, String keyword) {
                search(keyword);
                searchEditText.setText(keyword);
            }
        });
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
        return true;
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
    protected int getDefaultPageSize() {
        return 100;
    }

    @Override
    protected String getEmptyDataTips() {
        return "暂时没有报道数据";
    }

    @Override
    public RaceReportAdapter getNewAdapterWithNoData() {
        RaceReportAdapter adapter = new RaceReportAdapter(getMatchType());

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Object item = ((RaceReportAdapter) adapter).getData().get(position);
                Logger.d(item.getClass().getName());
                if ("xh".equals(getMatchType())) {
                    if (item instanceof RaceReportAdapter.MatchTitleXHItem) {
                        if (((RaceReportAdapter.MatchTitleXHItem) item).isExpanded()) {
                            adapter.collapse(position);
                        } else {
                            adapter.expand(position);
                        }
                    } else if (item instanceof RaceReportAdapter.MatchDetialXHItem) {
                        MatchReportXH mi = ((RaceReportAdapter.MatchDetialXHItem) item).getSubItem(0);

                    }
                } else if ("gp".equals(getMatchType())) {
                    if (item instanceof RaceReportAdapter.MatchTitleGPItem) {

                        if (((RaceReportAdapter.MatchTitleGPItem) item).isExpanded()) {
                            adapter.collapse(position);
                        } else {
                            adapter.expand(position);
                        }
                    } else if (item instanceof RaceReportAdapter.MatchDetialGPItem) {
                        MatchReportGP mi = ((RaceReportAdapter.MatchDetialGPItem) item).getSubItem(0);

                    }
                }

            }
        });
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                if ("xh".equals(getMatchType())) {
                    if (!"".equals(sKey)) {
                        sKey = "";
                    }
                    Object item = ((RaceReportAdapter) baseQuickAdapter).getData().get(i);
                    if (item instanceof RaceReportAdapter.MatchTitleXHItem) {
                        sKey = ((RaceReportAdapter.MatchTitleXHItem) item).getMatchReportXH().getName();
                        new SaActionSheetDialog(getActivity())
                                .builder()
                                .addSheetItem(String.format(getString(R.string.search_prompt_has_key), sKey), new SaActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        isSearch = true;
                                        onRefresh();
                                    }
                                })
                                .setCancelable(true)
                                .show();
                    }
                } else if ("gp".equals(getMatchType())) {
                    if (!"".equals(sKey)) {
                        sKey = "";
                    }
                    Object item = ((RaceReportAdapter) baseQuickAdapter).getData().get(i);
                    if (item instanceof RaceReportAdapter.MatchTitleGPItem) {
                        sKey = ((RaceReportAdapter.MatchTitleGPItem) item).getMatchReportGP().getName();
                        new SaActionSheetDialog(getActivity())
                                .builder()
                                .addSheetItem(String.format(getString(R.string.search_prompt_has_key), sKey), new SaActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        isSearch = true;
                                        onRefresh();
                                    }
                                })
                                .setCancelable(true)
                                .show();
                    }
                }
                return true;
            }
        });

        return adapter;
    }

    @Override
    protected void loadDataByPresenter() {
        mPresenter.loadRaceData(0);
    }

    public void search(String keyword) {
        this.sKey = keyword;
        if (TextUtils.isEmpty(keyword))
            return;
        isSearch = true;
        onRefresh();
    }

}
