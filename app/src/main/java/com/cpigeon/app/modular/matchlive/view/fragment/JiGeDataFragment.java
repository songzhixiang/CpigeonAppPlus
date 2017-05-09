package com.cpigeon.app.modular.matchlive.view.fragment;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.cpigeon.app.utils.customview.SaActionSheetDialog;
import com.cpigeon.app.utils.customview.SearchEditText;
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
    @BindView(R.id.searchEditText)
    SearchEditText searchEditText;
    private MatchInfo matchInfo;
    private String sKey = "";
    private int lastExpandItemPosition = -1;//最后一个索引

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initMatchinfo();
    }

    private void initMatchinfo() {
        if (matchInfo == null)
            this.matchInfo = ((RaceReportActivity) getActivity()).getMatchInfo();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        lastExpandItemPosition = -1;
        if (searchEditText != null) {
            searchEditText.setText(this.sKey);
            searchEditText.setSelection(this.sKey.length());
        }
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        initSearch();
        listHeaderRaceDetialTableHeader1.setText("序号");
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
        return "暂时没有数据，请您稍后再试";
    }

    @Override
    public JiGeDataAdapter getNewAdapterWithNoData() {
        JiGeDataAdapter adapter = new JiGeDataAdapter(getMatchType());
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Object item = ((JiGeDataAdapter) adapter).getData().get(position);
                Logger.d(item.getClass().getName());
                if ("xh".equals(getMatchType())) {
                    if (item instanceof JiGeDataAdapter.JiGeTitleItem_XH) {

                        if (((JiGeDataAdapter.JiGeTitleItem_XH) item).isExpanded()) {
                            if (lastExpandItemPosition == position) {
                                lastExpandItemPosition = -1;
                            }
                            adapter.collapse(position);
                        } else {
                            if (lastExpandItemPosition >= 0) {
                                adapter.collapse(lastExpandItemPosition);
                                Logger.e("上一个关闭的项的postion" + lastExpandItemPosition);
                                if (lastExpandItemPosition > position) {//展开上面的项
                                    adapter.expand(position);
                                    lastExpandItemPosition = position;
                                } else if (lastExpandItemPosition < position) {//展开下面的项
                                    adapter.expand(position - 1);
                                    lastExpandItemPosition = position - 1;
                                }

                            } else {
                                lastExpandItemPosition = position;
                                adapter.expand(lastExpandItemPosition);
                                Logger.e("当前被展开的项的lastExpandItemPosition" + lastExpandItemPosition);
                            }
                        }
                    } else if (item instanceof JiGeDataAdapter.JiGeDetialItem_XH) {
                        MatchPigeonsXH mi = ((JiGeDataAdapter.JiGeDetialItem_XH) item).getSubItem(0);

                    }
                } else if ("gp".equals(getMatchType())) {
                    if (item instanceof JiGeDataAdapter.JiGeTitleItem_GP) {

                        if (((JiGeDataAdapter.JiGeTitleItem_GP) item).isExpanded()) {
                            if (lastExpandItemPosition == position) {
                                lastExpandItemPosition = -1;
                            }
                            adapter.collapse(position);
                        } else {
                            if (lastExpandItemPosition >= 0) {
                                adapter.collapse(lastExpandItemPosition);
                                Logger.e("上一个关闭的项的postion" + lastExpandItemPosition);
                                if (lastExpandItemPosition > position) {//展开上面的项
                                    adapter.expand(position);
                                    lastExpandItemPosition = position;
                                } else if (lastExpandItemPosition < position) {//展开下面的项
                                    adapter.expand(position - 1);
                                    lastExpandItemPosition = position - 1;
                                }

                            } else {
                                lastExpandItemPosition = position;
                                adapter.expand(lastExpandItemPosition);
                                Logger.e("当前被展开的项的lastExpandItemPosition" + lastExpandItemPosition);
                            }
                        }
                    } else if (item instanceof JiGeDataAdapter.JiGeDetialItem_GP) {
                        MatchPigeonsGP mi = ((JiGeDataAdapter.JiGeDetialItem_GP) item).getSubItem(0);

                    }
                }


            }
        });

        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                String key = sKey;
                boolean show = false;
                if (!TextUtils.isEmpty(key)) {
                    new SaActionSheetDialog(getActivity())
                            .builder()
                            .addSheetItem(getString(R.string.search_prompt_clear_key), new SaActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    search("");
                                }
                            })
                            .setCancelable(true)
                            .show();
                } else {
                    if ("xh".equals(getMatchType())) {
                        Object item = ((JiGeDataAdapter) baseQuickAdapter).getData().get(i);
                        if (item instanceof JiGeDataAdapter.JiGeTitleItem_XH) {
                            key = ((JiGeDataAdapter.JiGeTitleItem_XH) item).getMatchPigeonsXH().getName();
                            show = true;
                        }
                    } else if ("gp".equals(getMatchType())) {
                        Object item = ((JiGeDataAdapter) baseQuickAdapter).getData().get(i);
                        if (item instanceof JiGeDataAdapter.JiGeTitleItem_GP) {
                            key = ((JiGeDataAdapter.JiGeTitleItem_GP) item).getMatchPigeonsGP().getName();
                            show = true;
                        }
                    }
                    final String finalKey = key;
                    if (show)
                        new SaActionSheetDialog(getActivity())
                                .builder()
                                .addSheetItem(String.format(getString(R.string.search_prompt_has_key), key), new SaActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        search(finalKey);
                                    }
                                })
                                .setCancelable(true)
                                .show();
                }
                return true;
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
        initMatchinfo();
        return matchInfo.getLx();
    }

    @Override
    public String getSsid() {
        initMatchinfo();
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
        return sKey;
    }

    public void search(String keyword) {
        this.sKey = keyword;
        onRefresh();
    }
}
