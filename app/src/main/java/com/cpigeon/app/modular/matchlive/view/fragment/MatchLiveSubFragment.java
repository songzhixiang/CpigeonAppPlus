package com.cpigeon.app.modular.matchlive.view.fragment;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.presenter.MatchLiveSubPre;
import com.cpigeon.app.modular.matchlive.view.adapter.MatchLiveExpandAdapter;
import com.cpigeon.app.utils.Const;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/4/7.
 */

public class MatchLiveSubFragment extends BaseFragment implements IMatchSubView, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.recyclerview_matchlive)
    RecyclerView mRecyclerView;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
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
                if (item instanceof MatchLiveExpandAdapter.MatchTitleItem) {
                    if (((MatchLiveExpandAdapter.MatchTitleItem) item).isExpanded()) {
                        ((MatchLiveExpandAdapter) adapter).collapse(position);
                    } else {
//                        if (lastExpandItemPosition >= 0) {
//                            ((MatchLiveExpandAdapter) adapter).collapse(lastExpandItemPosition);
//                        }
                        ((MatchLiveExpandAdapter) adapter).expand(position);
                        lastExpandItemPosition = position;
                    }
                }
            }
        });

        mRecyclerView.setAdapter(matchLiveAdapter);
        if (Const.MATCHLIVE_TYPE_GP.equals(currMatchType)) {
            pre.loadGPData(0);
        } else if (Const.MATCHLIVE_TYPE_XH.equals(currMatchType)) {
            pre.loadXHData(0);
        }
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
        matchLiveAdapter.setNewData(MatchLiveExpandAdapter.get(matchInfoList));
        if (onRefreshListener != null)
            onRefreshListener.onRefreshFinished(OnRefreshListener.DATA_Type_GP, matchInfoList.size());
    }

    @Override
    public void showXHData(List<MatchInfo> matchInfoList, int type) {
        this.matchInfos = matchInfoList;
        matchLiveAdapter.setNewData(MatchLiveExpandAdapter.get(matchInfoList));
        if (onRefreshListener != null)
            onRefreshListener.onRefreshFinished(OnRefreshListener.DATA_Type_XH, matchInfoList.size());
    }

    @Override
    public void setLoadType(int type) {

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
                matchLiveAdapter.setNewData(MatchLiveExpandAdapter.get(matchInfos));
                mSwipeRefreshLayout.setRefreshing(false);
                matchLiveAdapter.setEnableLoadMore(true);
            }
        }, delayMillis);
    }

    public interface OnRefreshListener {
        int DATA_Type_GP = 1;
        int DATA_Type_XH = 2;

        void onStartRefresh(MatchLiveSubFragment fragment);

        void onRefreshFinished(int type, int loadCount);
    }
}
