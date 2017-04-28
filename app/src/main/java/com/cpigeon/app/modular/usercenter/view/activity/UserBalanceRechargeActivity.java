package com.cpigeon.app.modular.usercenter.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.modular.home.view.activity.WebActivity;
import com.cpigeon.app.modular.usercenter.model.bean.CpigeonRechargeInfo;
import com.cpigeon.app.modular.usercenter.presenter.UserBalanceRechargePresenter;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.IUserBalanceRechargeView;
import com.cpigeon.app.utils.CPigeonApiUrl;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.NetUtils;
import com.cpigeon.app.utils.inputfilter.CashierInputFilter;
import com.cpigeon.app.wxapi.WXPayEntryActivity;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by chenshuai on 2017/4/15.
 */

public class UserBalanceRechargeActivity extends BaseActivity<UserBalanceRechargePresenter> implements IUserBalanceRechargeView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_money_income_number)
    EditText etMoneyIncomeNumber;
    @BindView(R.id.tv_pay_way_tips)
    TextView tvPayWayTips;
    @BindView(R.id.iv_wxpay_ok)
    AppCompatImageView ivWxpayOk;
    @BindView(R.id.rl_wxpay)
    RelativeLayout rlWxpay;
    @BindView(R.id.iv_alipay_ok)
    AppCompatImageView ivAlipayOk;
    @BindView(R.id.rl_alipay)
    RelativeLayout rlAlipay;
    @BindView(R.id.cb_order_protocol_income)
    CheckBox cbOrderProtocolIncome;
    @BindView(R.id.tv_order_protocol_income)
    TextView tvOrderProtocolIncome;
    @BindView(R.id.btn_ok_income)
    Button btnOkIncome;
    private int currPayway = -1;
    double mInputFee = 0;
    private IWXAPI mWxApi = null;

    CpigeonData.OnWxPayListener onWxPayListener = new CpigeonData.OnWxPayListener() {
        @Override
        public void onPayFinished(int wxPayReturnCode) {
            if (wxPayReturnCode == 0) {
                finish();
            } else {
                SweetAlertDialog dialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
                dialog.setCancelable(false);
                dialog.setTitleText("提示").setConfirmText("确定").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        finish();
                    }
                }).setContentText("支付失败");
            }
        }
    };
    private PayReq payReq;

    @Override
    public int getLayoutId() {
        return R.layout.activity_balance_recharge;
    }

    @Override
    public UserBalanceRechargePresenter initPresenter() {
        return new UserBalanceRechargePresenter(this);
    }

    @Override
    public void initView() {
        toolbar.setTitle("账户充值");
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etMoneyIncomeNumber.setFilters(new InputFilter[]{new CashierInputFilter()});
        etMoneyIncomeNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                chosePayWayMoney(currPayway);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        CpigeonData.getInstance().addOnWxPayListener(onWxPayListener);

        if (mWxApi == null) {
            mWxApi = WXAPIFactory.createWXAPI(mContext, WXPayEntryActivity.APP_ID, true);
            mWxApi.registerApp(WXPayEntryActivity.APP_ID);
        }
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    /**
     * 添加手续费
     *
     * @param fee  金额
     * @param rate 费率
     * @return
     */
    public double getTotalFee(double fee, double rate) {
        double f = fee * rate;//手续费
        return fee + (f <= 0.01 ? 0.01f : f);
    }

    private void chosePayWayMoney(int type) {
        if (currPayway != type)
            currPayway = type;
        showTip();
    }

    private void showTip() {
        double fee = 0;
        ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources().getColor(R.color.light_red2));
        try {
            fee = Double.valueOf(etMoneyIncomeNumber.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            fee = 0;
        }
        if (fee == 0) {
            tvPayWayTips.setVisibility(View.INVISIBLE);
        } else if (currPayway == TYPE_PAY_WAY_WXPAY) {
            SpannableStringBuilder builder = new SpannableStringBuilder(String.format("需要收取1%%的手续费，实际支付%.2f元", getTotalFee(fee, 0.01)));
            builder.setSpan(redSpan, 15,
                    15 + String.format("%.2f", getTotalFee(fee, 0.01)).length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvPayWayTips.setText(builder);
            tvPayWayTips.setVisibility(View.VISIBLE);
        } else if (currPayway == TYPE_PAY_WAY_ALIPAY) {
            SpannableStringBuilder builder = new SpannableStringBuilder(String.format("需要收取0.6%%的手续费，实际支付%.2f元", getTotalFee(fee, 0.006)));
            builder.setSpan(redSpan, 17,
                    17 + String.format("%.2f", getTotalFee(fee, 0.006)).length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvPayWayTips.setText(builder);
            tvPayWayTips.setVisibility(View.VISIBLE);
        } else {
            tvPayWayTips.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick({R.id.rl_wxpay, R.id.rl_alipay, R.id.tv_order_protocol_income, R.id.btn_ok_income})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_wxpay:
                ivAlipayOk.setVisibility(View.INVISIBLE);
                ivWxpayOk.setVisibility(View.VISIBLE);
                chosePayWayMoney(TYPE_PAY_WAY_WXPAY);
                break;
            case R.id.rl_alipay:
                ivAlipayOk.setVisibility(View.VISIBLE);
                ivWxpayOk.setVisibility(View.INVISIBLE);
                chosePayWayMoney(TYPE_PAY_WAY_ALIPAY);
                break;
            case R.id.tv_order_protocol_income:
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra(WebActivity.INTENT_DATA_KEY_URL, CPigeonApiUrl.getInstance().getServer() + "/APP/Protocol?type=pay");
                intent.putExtra(WebActivity.INTENT_DATA_KEY_BACKNAME, "账户充值");
                startActivity(intent);
                break;
            case R.id.btn_ok_income:
                if (mWxApi.isWXAppInstalled())
                    mPresenter.recharge();
                else {
                    showTips("未安装微信", TipType.ToastShort);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        CpigeonData.getInstance().removeOnWxPayListener(onWxPayListener);
        super.onDestroy();
    }

    @Override
    public double getPayTotalFee() {
        getInputFee();
        double rate = getPayway() == TYPE_PAY_WAY_ALIPAY ? 0.006 : 0.01;
        return getTotalFee(mInputFee, rate);
    }

    @Override
    public double getInputFee() {
        try {
            if (etMoneyIncomeNumber != null)
                mInputFee = Double.valueOf(etMoneyIncomeNumber.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            mInputFee = 0;
        }
        return mInputFee;
    }

    @Override
    public int getPayway() {
        return this.currPayway;
    }

    @Override
    public boolean isAgreePayProtocol() {
        if (cbOrderProtocolIncome != null)
            return cbOrderProtocolIncome.isChecked();
        return false;
    }

    @Override
    public void onWXPay(PayReq payReq) {
        this.payReq = payReq;
        if (mWxApi != null) {
            boolean result = mWxApi.sendReq(payReq);
            if (!result)
                showTips("支付失败", TipType.ToastShort);
            Logger.d("发起微信支付");
        }
    }

    @Override
    public PayReq getWxPayReqCache() {
        return this.payReq;
    }

    @Override
    public void onAliPay(CpigeonRechargeInfo.DataBean data) {
        String url = CPigeonApiUrl.getInstance().getServer() + "/APPPay/alipayForRecharge?did=" + data.getId() + "&uid=" + CpigeonData.getInstance().getUserId(mContext); // web address
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
        finish();
    }
}
