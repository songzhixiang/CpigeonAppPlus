package com.cpigeon.app.modular.matchlive.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpigeon.app.MainActivity;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.adapter.ContentFragmentAdapter;
import com.cpigeon.app.utils.Const;
import com.cpigeon.app.utils.customview.SearchEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.badgeview.BGABadgeTextView;

/**
 * Created by Administrator on 2017/4/6.
 */

public class MatchLiveFragment extends Fragment {

    @BindView(R.id.tv_actionbar_matchtype_xh)
    BGABadgeTextView tvActionbarMatchtypeXh;
    @BindView(R.id.tv_actionbar_matchtype_gp)
    BGABadgeTextView tvActionbarMatchtypeGp;
    @BindView(R.id.search_edittext)
    SearchEditText searchEdittext;
    @BindView(R.id.viewpager_matchlive)
    ViewPager mViewPager;
    private View mView;
    private String currMatchType = Const.MATCHLIVE_TYPE_XH;
    private MatchLiveSubFragment matchLiveSubFragment_GP;
    private MatchLiveSubFragment matchLiveSubFragment_XH;
    private MatchLiveSubFragment currMatchLiveSubFragment;
    private ContentFragmentAdapter mContentFragmentAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_live, container, false);
        ButterKnife.bind(this, mView);
        initView();
        return mView;
    }

    private void initView() {
        matchLiveSubFragment_GP = new MatchLiveSubFragment();
        matchLiveSubFragment_GP.setMatchType(Const.MATCHLIVE_TYPE_GP);
        matchLiveSubFragment_XH = new MatchLiveSubFragment();

        matchLiveSubFragment_GP.setMatchType(Const.MATCHLIVE_TYPE_GP);
        currMatchLiveSubFragment = matchLiveSubFragment_XH;
        mContentFragmentAdapter = new ContentFragmentAdapter(getFragmentManager());
        mContentFragmentAdapter.appendData(matchLiveSubFragment_XH);
        mContentFragmentAdapter.appendData(matchLiveSubFragment_GP);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mContentFragmentAdapter);
        mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCurrMatchType(position == 0 ? Const.MATCHLIVE_TYPE_XH : Const.MATCHLIVE_TYPE_GP);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                ((MainActivity) getActivity()).setOnMatchTypeChangeListener(new MainActivity.OnMatchTypeChangeListener() {
                    @Override
                    public void onChanged(String lastType, String currType) {
                        setCurrMatchType(currType);
                    }
                });

                mViewPager.setCurrentItem(0);
            }


        });
    }

    private MatchLiveSubFragment.OnRefreshListener onRefreshListener = new MatchLiveSubFragment.OnRefreshListener() {
        @Override
        public void onStartRefresh(MatchLiveSubFragment fragment) {
        }

        @Override
        public void onRefreshFinished(MatchLiveSubFragment fragment, int loadCount) {
            if (fragment.currMatchType.equals(Const.MATCHLIVE_TYPE_XH)) {
                tvActionbarMatchtypeXh.getBadgeViewHelper().showTextBadge(String.valueOf(loadCount));
            }
            if (fragment.currMatchType.equals(Const.MATCHLIVE_TYPE_GP)) {
                tvActionbarMatchtypeGp.getBadgeViewHelper().showTextBadge(String.valueOf(loadCount));
            }
        }
    };

    public String getCurrMatchType() {
        return currMatchType;
    }


    private void setCurrMatchType(String matchType) {
        this.currMatchType = matchType;
        changeRaceTypeViewStarus();
        currMatchLiveSubFragment = Const.MATCHLIVE_TYPE_XH.equals(matchType) ? matchLiveSubFragment_XH : matchLiveSubFragment_GP;
        searchEdittext.setHint(Const.MATCHLIVE_TYPE_XH.equals(matchType) ? "比赛/协会名称" : "比赛/公棚名称");
        if (mViewPager != null)
            mViewPager.setCurrentItem(Const.MATCHLIVE_TYPE_XH.equals(matchType) ? 0 : 1);
    }

    private void changeRaceTypeViewStarus() {
        if (tvActionbarMatchtypeXh != null && Const.MATCHLIVE_TYPE_XH.equals(currMatchType)) {
            tvActionbarMatchtypeXh.setBackgroundResource(R.drawable.background_round_left_focus);
            tvActionbarMatchtypeXh.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvActionbarMatchtypeGp.setBackgroundResource(R.drawable.background_round_right_default);
            tvActionbarMatchtypeGp.setTextColor(getResources().getColor(R.color.colorWhite));
        } else if (tvActionbarMatchtypeGp != null && Const.MATCHLIVE_TYPE_GP.equals(currMatchType)) {
            tvActionbarMatchtypeGp.setBackgroundResource(R.drawable.background_round_right_focus);
            tvActionbarMatchtypeGp.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvActionbarMatchtypeXh.setBackgroundResource(R.drawable.background_round_left_default);
            tvActionbarMatchtypeXh.setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    @OnClick({R.id.tv_actionbar_matchtype_xh, R.id.tv_actionbar_matchtype_gp, R.id.search_edittext})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_actionbar_matchtype_xh:
                setCurrMatchType(Const.MATCHLIVE_TYPE_XH);
                break;
            case R.id.tv_actionbar_matchtype_gp:
                setCurrMatchType(Const.MATCHLIVE_TYPE_GP);
                break;
            case R.id.search_edittext:
                break;
        }
        changeRaceTypeViewStarus();
    }
}

