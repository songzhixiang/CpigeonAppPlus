package com.cpigeon.app.modular.matchlive.view.fragment;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BasePageTurnFragment;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.model.dao.IRaceReport;
import com.cpigeon.app.modular.matchlive.presenter.JiGePre;
import com.cpigeon.app.modular.matchlive.view.activity.RaceReportActivity;
import com.cpigeon.app.modular.matchlive.view.adapter.JiGeDataAdapter;
import com.cpigeon.app.modular.matchlive.view.fragment.viewdao.IReportData;

import butterknife.BindView;

/**
 * 集鸽数据Fragment
 * Created by Administrator on 2017/4/15.
 */

public class JiGeDataFragment extends BasePageTurnFragment<JiGePre, JiGeDataAdapter, MultiItemEntity> implements IReportData {


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
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.matchInfo = ((RaceReportActivity) context).getMatchInfo();
    }

    @Override
    protected JiGePre initPresenter() {
        return new JiGePre(this);
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_race_data;
    }

    @Override
    protected int getDefaultPageSize() {
        return 0;
    }

    @Override
    protected String getEmptyDataTips() {
        return "1";
    }

    @Override
    public JiGeDataAdapter getNewAdapterWithNoData() {
        return new JiGeDataAdapter(getMatchType());
    }

    @Override
    protected void loadDataByPresenter() {
        mPresenter.loadJiGe();
    }


    @Override
    public String getMatchType() {
        return "xh";
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
        return 0;
    }

    @Override
    public String sKey() {
        return "";
    }
}
