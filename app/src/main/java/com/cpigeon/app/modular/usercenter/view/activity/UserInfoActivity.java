package com.cpigeon.app.modular.usercenter.view.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.utils.NetUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/4/10.
 */

public class UserInfoActivity extends BaseActivity {
    @BindView(R.id.iv_user_head_img)
    CircleImageView ivUserHeadImg;
    @BindView(R.id.ll_user_head_img)
    LinearLayout llUserHeadImg;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.ll_nick_name)
    LinearLayout llNickName;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.ll_user_name)
    LinearLayout llUserName;
    @BindView(R.id.tv_user_brithday)
    TextView tvUserBrithday;
    @BindView(R.id.ll_user_birthday)
    LinearLayout llUserBirthday;
    @BindView(R.id.tv_user_sex)
    TextView tvUserSex;
    @BindView(R.id.ll_user_sex)
    LinearLayout llUserSex;
    @BindView(R.id.tv_user_sign)
    TextView tvUserSign;
    @BindView(R.id.ll_user_sign)
    LinearLayout llUserSign;
    @BindView(R.id.ll_change_pwd)
    LinearLayout llChangePwd;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_userinfo;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        toolbar.setTitle("个人信息");
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

}
