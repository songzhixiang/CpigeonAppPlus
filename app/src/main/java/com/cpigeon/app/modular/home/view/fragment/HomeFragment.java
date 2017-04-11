package com.cpigeon.app.modular.home.view.fragment;

import android.view.View;
import android.widget.LinearLayout;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;
import com.cpigeon.app.modular.home.model.bean.AD;
import com.cpigeon.app.utils.customview.SearchEditText;
import com.youth.banner.Banner;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/6.
 */

public class HomeFragment extends BaseFragment {
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

    @Override
    protected void initView(View view) {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home;
    }

    @Override
    protected void lazyLoad() {

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

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


}
