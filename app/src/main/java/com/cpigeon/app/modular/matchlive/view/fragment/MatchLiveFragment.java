package com.cpigeon.app.modular.matchlive.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpigeon.app.R;
import com.cpigeon.app.utils.Const;
import com.cpigeon.app.utils.customview.SearchEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private View mView;
    private String currMatchType = Const.MATCHLIVE_TYPE_XH;
    private MatchLiveSubFragment matchLiveSubFragment_GP;
    private MatchLiveSubFragment matchLiveSubFragment_XH;
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
        matchLiveSubFragment_GP.setLoadType(0);

    }
}
