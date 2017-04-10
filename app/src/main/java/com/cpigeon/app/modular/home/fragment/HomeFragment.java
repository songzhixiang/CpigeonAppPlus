package com.cpigeon.app.modular.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cpigeon.app.R;
import com.cpigeon.app.modular.home.model.bean.AD;
import com.cpigeon.app.utils.customview.SearchEditText;
import com.youth.banner.Banner;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/6.
 */

public class HomeFragment extends Fragment {
    @BindView(R.id.search_edittext)
    SearchEditText searchEdittext;
    @BindView(R.id.home_banner)
    Banner homeBanner;
    @BindView(R.id.layout_gpzb)
    LinearLayout layoutGpzb;
    @BindView(R.id.layout_xhzb)
    LinearLayout layoutXhzb;
    @BindView(R.id.layout_zhcx)
    LinearLayout layoutZhcx;
    @BindView(R.id.layout_wdsc)
    LinearLayout layoutWdsc;
    private View mView;
    private List<AD> adList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, mView);
        return mView;
    }

    @OnClick({R.id.layout_gpzb, R.id.layout_xhzb, R.id.layout_zhcx, R.id.layout_wdsc})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_gpzb:
                break;
            case R.id.layout_xhzb:
                break;
            case R.id.layout_zhcx:
                break;
            case R.id.layout_wdsc:
                break;
        }
    }
}
