package com.cpigeon.app.modular.matchlive.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemChildLongClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.presenter.MatchLiveSubPre;
import com.cpigeon.app.utils.Const;
import com.cpigeon.app.utils.customview.SaActionSheetDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by Administrator on 2017/4/7.
 */

public class MatchLiveSubFragment extends BaseFragment implements IMatchSubView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recyclerview_matchlive)
    RecyclerView mRecyclerView;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private MatchLiveAdapter matchLiveAdapter;
    private List<MatchInfo> matchInfos;
    private int delayMillis = 1000;
    private MatchLiveSubPre pre = new MatchLiveSubPre(this);
    String currMatchType = "";
    private OnRefreshListener onRefreshListener;

    @Override
    protected void initView(View view) {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        matchLiveAdapter = new MatchLiveAdapter(matchInfoList);
        matchLiveAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);

        mRecyclerView.setAdapter(matchLiveAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemLongClickListener() {
            @Override
            public void onSimpleItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                if (matchLiveAdapter != null && adapter != null && position >= 0) {
                    String mc = matchLiveAdapter.getData().get(position).getMc();
                    new SaActionSheetDialog(getActivity()).builder()
                            .addSheetItem(String.format(getString(R.string.search_prompt_has_key), mc), new SaActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    showTips("搜素+++",TipType.ToastShort);
                                }
                            }).show();
                }
            }
        });
        if (onRefreshListener != null)
            onRefreshListener.onRefreshFinished(OnRefreshListener.DATA_Type_GP, matchInfoList.size());
    }

    @Override
    public void showXHData(List<MatchInfo> matchInfoList, int type) {
        this.matchInfos = matchInfoList;
        matchLiveAdapter = new MatchLiveAdapter(matchInfoList);
        matchLiveAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRecyclerView.setAdapter(matchLiveAdapter);
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
                matchLiveAdapter.setNewData(matchInfos);
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
