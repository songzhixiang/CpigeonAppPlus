package com.cpigeon.app.modular.usercenter.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseLazyLoadFragment;
import com.cpigeon.app.modular.order.view.activity.OpenServiceActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by chenshuai on 2017/4/13.
 */

public class UserScoreSub1Fragment extends BaseLazyLoadFragment {
    @BindView(R.id.btn_zhcx_dh)
    Button btnZhcxDh;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @Override
    protected void initView(View view) {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_user_score_s1;
    }

    @Override
    protected void lazyLoad() {

    }

    @OnClick(R.id.btn_zhcx_dh)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), OpenServiceActivity.class);
        intent.putExtra(OpenServiceActivity.INTENT_DATA_KEY_SERVICENAME, "足环查询服务");
        startActivity(intent);
    }
}
