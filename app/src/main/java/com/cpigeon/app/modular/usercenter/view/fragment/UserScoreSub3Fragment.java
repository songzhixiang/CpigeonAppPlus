package com.cpigeon.app.modular.usercenter.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;
import com.cpigeon.app.commonstandard.view.fragment.BaseLazyLoadFragment;
import com.cpigeon.app.modular.home.view.activity.WebActivity;
import com.cpigeon.app.utils.CPigeonApiUrl;
import com.cpigeon.app.utils.CpigeonData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by chenshuai on 2017/4/13.
 */

public class UserScoreSub3Fragment extends BaseFragment {
    @BindView(R.id.btn_goto_sign)
    Button btnGotoSign;

    @Override
    protected void initView(View view) {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_user_score_s3;
    }

    @OnClick(R.id.btn_goto_sign)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra(WebActivity.INTENT_DATA_KEY_BACKNAME, "我的");
        intent.putExtra(WebActivity.INTENT_DATA_KEY_URL, CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.APP_SIGN_URL + "?uid=" + CpigeonData.getInstance().getUserId(getActivity()));
        startActivity(intent);
    }
}
