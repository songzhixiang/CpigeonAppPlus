package com.cpigeon.app.modular.matchlive.view.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseLazyLoadFragment;
import com.cpigeon.app.utils.customview.SearchEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 集鸽数据Fragment
 * Created by Administrator on 2017/4/15.
 */

public class JiGeDataFragment extends BaseLazyLoadFragment {


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
    Unbinder unbinder;

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_race_data;
    }

    @Override
    protected void lazyLoad() {

    }



}
