package com.cpigeon.app.modular.matchlive.view.fragment;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BasePageTurnFragment;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.model.bean.MatchPigeonsGP;
import com.cpigeon.app.modular.matchlive.model.bean.MatchPigeonsXH;
import com.cpigeon.app.modular.matchlive.model.bean.MatchReportXH;
import com.cpigeon.app.modular.matchlive.model.dao.IRaceReport;
import com.cpigeon.app.modular.matchlive.presenter.JiGePre;
import com.cpigeon.app.modular.matchlive.view.activity.RaceReportActivity;
import com.cpigeon.app.modular.matchlive.view.adapter.JiGeDataAdapter;
import com.cpigeon.app.modular.matchlive.view.adapter.RaceReportAdapter;
import com.cpigeon.app.modular.matchlive.view.fragment.viewdao.IReportData;
import com.orhanobut.logger.Logger;

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
        return 100;
    }


    @Override
    protected String getEmptyDataTips() {
        return "对不起，暂时无法获取数据，请您稍后再试";
    }

    @Override
    public JiGeDataAdapter getNewAdapterWithNoData() {
        JiGeDataAdapter adapter=  new JiGeDataAdapter(getMatchType());
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Object item = ((JiGeDataAdapter) adapter).getData().get(position);
                Logger.d(item.getClass().getName());
                if ("xh".equals(getMatchType()))
                {
                    if (item instanceof JiGeDataAdapter.JiGeTitleItem_XH) {

                        if (((JiGeDataAdapter.JiGeTitleItem_XH) item).isExpanded()) {
                            adapter.collapse(position);
                        } else {
                            adapter.expand(position);
                        }
                    } else if (item instanceof JiGeDataAdapter.JiGeDetialItem_XH) {
                        MatchPigeonsXH mi = ((JiGeDataAdapter.JiGeDetialItem_XH) item).getSubItem(0);

                    }
                }else if ("gp".equals(getMatchType()))
                {
                    if (item instanceof JiGeDataAdapter.JiGeTitleItem_GP) {

                        if (((JiGeDataAdapter.JiGeTitleItem_GP) item).isExpanded()) {
                            adapter.collapse(position);
                        } else {
                            adapter.expand(position);
                        }
                    } else if (item instanceof JiGeDataAdapter.JiGeDetialItem_GP) {
                        MatchPigeonsGP mi = ((JiGeDataAdapter.JiGeDetialItem_GP) item).getSubItem(0);

                    }
                }


            }
        });
        return adapter;
    }

    @Override
    protected void loadDataByPresenter() {
        mPresenter.loadJiGe();
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
        return 0;
    }

    @Override
    public String sKey() {
        return "";
    }
}
