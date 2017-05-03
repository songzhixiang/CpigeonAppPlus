package com.cpigeon.app.modular.usercenter.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.modular.home.view.activity.WebActivity;
import com.cpigeon.app.utils.CPigeonApiUrl;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.NetUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/10.
 */

public class BalanceActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_user_balance)
    TextView tvUserBalance;
    @BindView(R.id.btn_money_recharge)
    Button btnMoneyRecharge;
    @BindView(R.id.btn_money_details)
    Button btnMoneyDetails;
    @BindView(R.id.tv_question)
    TextView tvQuestion;

    private CpigeonData.OnDataChangedListener onDataChangedLisenter = new CpigeonData.OnDataChangedListener() {
        @Override
        public void OnDataChanged(CpigeonData cpigeonData) {
            if (tvUserBalance != null)
                tvUserBalance.setText(String.format("￥%.2f", cpigeonData.getUserBalance()));
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_balance;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        toolbar.setTitle("账户余额");
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvUserBalance.setText(String.format("￥%s", String.valueOf(CpigeonData.getInstance().getUserBalance())));
        CpigeonData.getInstance().addOnDataChangedListener(onDataChangedLisenter);
        CpigeonData.DataHelper.getInstance().updateUserBalanceAndScoreFromServer();
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @OnClick({R.id.btn_money_recharge, R.id.btn_money_details, R.id.tv_question})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_money_recharge:
                startActivity(new Intent(BalanceActivity.this, UserBalanceRechargeActivity.class));
                break;
            case R.id.btn_money_details:
                startActivity(new Intent(BalanceActivity.this, UserBalanceListActivity.class));
                break;
            case R.id.tv_question:
                String url = CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.APP_HELP_URL;// web address
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra(WebActivity.INTENT_DATA_KEY_URL, url);
                intent.putExtra(WebActivity.INTENT_DATA_KEY_BACKNAME, "账户余额");
                startActivity(intent);
                break;
        }
    }
}
