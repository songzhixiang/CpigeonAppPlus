package com.cpigeon.app.modular.matchlive.view.fragment;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BasePageTurnFragment;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.model.bean.MatchReportXH;
import com.cpigeon.app.modular.matchlive.presenter.RacePre;
import com.cpigeon.app.modular.matchlive.view.activity.RaceReportActivity;
import com.cpigeon.app.modular.matchlive.view.adapter.RaceReportAdapter;
import com.orhanobut.logger.Logger;

import butterknife.BindView;

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
    private MatchInfo matchInfo;
    private String sKey = "";//当前搜索关键字

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.matchInfo = ((RaceReportActivity) context).getMatchInfo();
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        recyclerview.addOnItemTouchListener(new OnItemLongClickListener() {
            @Override
            public void onSimpleItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                Object item = ((RaceReportAdapter) adapter).getData().get(position);
                if (item instanceof RaceReportAdapter.MatchTitleXHItem) {
//                    final String s = ((RaceReportAdapter.MatchTitleXHItem) item).getMatchReportXH().getMc();

                }
            }
        });
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

    @Override
    protected int getDefaultPageSize() {
        return 100;
    }

    @Override
    protected String getEmptyDataTips() {
        return "没有报道数据";
    }

    @Override
    public RaceReportAdapter getNewAdapterWithNoData() {
        RaceReportAdapter adapter=  new RaceReportAdapter(getMatchType());

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
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
        return adapter;
    }

    @Override
    protected void loadDataByPresenter() {
        mPresenter.loadRaceData();
    }
}
