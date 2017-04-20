package com.cpigeon.app.modular.matchlive.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.presenter.MatchLiveSubPre;
import com.cpigeon.app.modular.matchlive.view.activity.RaceReportActivity;
import com.cpigeon.app.modular.matchlive.view.activity.SearchActivity;
import com.cpigeon.app.modular.matchlive.view.adapter.MatchLiveExpandAdapter;
import com.cpigeon.app.modular.matchlive.view.fragment.viewdao.IMatchSubView;
import com.cpigeon.app.utils.Const;
import com.cpigeon.app.utils.customview.SaActionSheetDialog;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/4/7.
 */

public class MatchLiveSubFragment extends BaseFragment implements IMatchSubView, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.recyclerview_matchlive)
    RecyclerView mRecyclerView;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.viewstub_empty)
    ViewStub viewstubEmpty;

    View mEmptyTip;
    TextView mEmptyTipTextView;

    private MatchLiveExpandAdapter matchLiveAdapter;
    private List<MatchInfo> matchInfos;
    private int delayMillis = 1000;

    private MatchLiveSubPre pre = new MatchLiveSubPre(this);
    String currMatchType = "";
    private OnRefreshListener onRefreshListener;

    private int lastExpandItemPosition = -1;

    @Override
    protected void initView(View view) {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        matchLiveAdapter = new MatchLiveExpandAdapter(MatchLiveExpandAdapter.get(null));
        matchLiveAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        matchLiveAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Object item = ((MatchLiveExpandAdapter) adapter).getData().get(position);
                Logger.d(item.getClass().getName());
                if (item instanceof MatchLiveExpandAdapter.MatchTitleItem) {
                    if (!"bs".equals(((MatchLiveExpandAdapter.MatchTitleItem) item).getMatchInfo().getDt()))
                        return;
                    if (((MatchLiveExpandAdapter.MatchTitleItem) item).isExpanded()) {
                        adapter.collapse(position);
                    } else {
                        adapter.expand(position);
                        lastExpandItemPosition = position;
                        Logger.e("当前被展开的项的postion" + lastExpandItemPosition);
                    }
                } else if (item instanceof MatchLiveExpandAdapter.MatchDetialItem) {
                    MatchInfo mi = ((MatchLiveExpandAdapter.MatchDetialItem) item).getSubItem(0);
                    if (mi != null && !"jg".equals(mi.getDt())) {
                        Intent intent = new Intent(getActivity(), RaceReportActivity.class);
                        Bundle bundle = new Bundle();                //创建Bundle对象
                        bundle.putSerializable("matchinfo", mi);     //装入数据
                        intent.putExtras(bundle);
                        startActivity(intent);
                        return;
                    }
                }
            }
        });
        mRecyclerView.addOnItemTouchListener(new OnItemLongClickListener() {
            @Override
            public void onSimpleItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                Object item = ((MatchLiveExpandAdapter) adapter).getData().get(position);
                if (item instanceof MatchLiveExpandAdapter.MatchTitleItem) {
                    final String s = ((MatchLiveExpandAdapter.MatchTitleItem) item).getMatchInfo().getMc();
                    new SaActionSheetDialog(getActivity())
                            .builder()
                            .addSheetItem(String.format(getString(R.string.search_prompt_has_key), s), new SaActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                                    Bundle bundle = new Bundle();                           //创建Bundle对象
                                    bundle.putSerializable(SearchActivity.INTENT_KEY_SEARCHKEY, s);     //装入数据
                                    bundle.putSerializable(SearchActivity.INTENT_KEY_SEARCH_HINT_TEXT, "比赛名称、" + (currMatchType.equals(Const.MATCHLIVE_TYPE_GP) ? "公棚名称" : "协会名称"));     //装入数据
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            })
                            .show();
                }
            }
        });
        mRecyclerView.setAdapter(matchLiveAdapter);
        onRefresh();
    }


    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_matchlive_sub;
    }


    @Override
    public void showGPData(List<MatchInfo> matchInfoList, int type) {
        this.matchInfos = matchInfoList;
//        if (type == 1) {
//            matchLiveAdapter.setNewData(MatchLiveExpandAdapter.get(matchInfos));
//        }

        matchLiveAdapter.setNewData(MatchLiveExpandAdapter.get(matchInfoList));
        if (onRefreshListener != null)
            onRefreshListener.onRefreshFinished(OnRefreshListener.DATA_Type_GP, matchInfoList.size());
    }

    @Override
    public void showXHData(List<MatchInfo> matchInfoList, int type) {
        this.matchInfos = matchInfoList;
//        if (type == 1) {
//            matchLiveAdapter.setNewData(MatchLiveExpandAdapter.get(matchInfos));
//        }
        matchLiveAdapter.setNewData(MatchLiveExpandAdapter.get(matchInfoList));
        if (onRefreshListener != null)
            onRefreshListener.onRefreshFinished(OnRefreshListener.DATA_Type_XH, matchInfoList.size());
    }

    @Override
    public void setLoadType(int type) {

    }

    @Override
    public boolean hasDataList() {
        return matchLiveAdapter == null ? false : matchLiveAdapter.getData() != null && matchLiveAdapter.getData().size() > 0;
    }

    public void setMatchType(String matchType) {
        currMatchType = matchType;
    }

    @Override
    public void onRefresh() {
        if (onRefreshListener != null)
            onRefreshListener.onStartRefresh(MatchLiveSubFragment.this);
        matchLiveAdapter.setEnableLoadMore(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isNetworkConnected()) {
                    showTips("网络无法连接", TipType.View);
                } else if (Const.MATCHLIVE_TYPE_GP.equals(currMatchType)) {
                    pre.loadGPData(0);
                } else if (Const.MATCHLIVE_TYPE_XH.equals(currMatchType)) {
                    pre.loadXHData(0);
                }
//                mSwipeRefreshLayout.setRefreshing(false);
                matchLiveAdapter.setEnableLoadMore(true);
            }
        }, delayMillis);
    }


    @Override
    public void showRefreshLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideRefreshLoading() {
        if (mEmptyTip != null)
            mEmptyTip.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showEmptyData() {
        showTips("暂无比赛", TipType.View);
    }

    @Override
    public boolean showTips(String tip, TipType tipType) {
        if (tipType == TipType.View) {
            if (mEmptyTip == null) mEmptyTip = viewstubEmpty.inflate();
            mEmptyTip.setVisibility(View.VISIBLE);
            if (mEmptyTipTextView == null)
                mEmptyTipTextView = (TextView) mEmptyTip.findViewById(R.id.tv_empty_tips);
            mEmptyTipTextView.setText(TextUtils.isEmpty(tip) ? "非常抱歉，发生了未知错误" : tip);
            return true;
        }
        return super.showTips(tip, tipType);
    }

    public interface OnRefreshListener {
        int DATA_Type_GP = 1;
        int DATA_Type_XH = 2;

        void onStartRefresh(MatchLiveSubFragment fragment);

        void onRefreshFinished(int type, int loadCount);
    }
}
