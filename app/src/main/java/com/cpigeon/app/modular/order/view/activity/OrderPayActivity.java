package com.cpigeon.app.modular.order.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.modular.home.view.activity.WebActivity;
import com.cpigeon.app.modular.order.model.bean.CpigeonOrderInfo;
import com.cpigeon.app.modular.order.presenter.OrderPayPresenter;
import com.cpigeon.app.modular.order.view.activity.viewdao.IOrderPayView;
import com.cpigeon.app.modular.order.view.fragment.PayPwdInputFragment;
import com.cpigeon.app.utils.CPigeonApiUrl;
import com.cpigeon.app.utils.Const;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.NetUtils;
import com.cpigeon.app.wxapi.WXPayEntryActivity;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by chenshuai on 2017/4/14.
 */

public class OrderPayActivity extends BaseActivity<OrderPayPresenter> implements IOrderPayView {

    public static final String INTENT_DATA_KEY_ORDERINFO = "orderInfo";
    private static final String INTENT_DATA_KEY_ORDERID = "orderID";
    private static final int ACTIVITY_RESULT_CODE = 65465;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_order_number_title)
    TextView tvOrderNumberTitle;
    @BindView(R.id.tv_order_number_content)
    TextView tvOrderNumberContent;
    @BindView(R.id.tv_order_name_title)
    TextView tvOrderNameTitle;
    @BindView(R.id.tv_order_name_content)
    TextView tvOrderNameContent;
    @BindView(R.id.tv_order_time_title)
    TextView tvOrderTimeTitle;
    @BindView(R.id.tv_order_time_content)
    TextView tvOrderTimeContent;
    @BindView(R.id.tv_order_price_title)
    TextView tvOrderPriceTitle;
    @BindView(R.id.tv_order_price_content)
    TextView tvOrderPriceContent;
    @BindView(R.id.tv_order_explain)
    TextView tvOrderExplain;
    @BindView(R.id.cb_order_protocol)
    CheckBox cbOrderProtocol;
    @BindView(R.id.tv_order_protocol)
    TextView tvOrderProtocol;
    @BindView(R.id.layout_order_pay_way)
    LinearLayout layoutOrderPayWay;

    private CpigeonOrderInfo mcpigeoCpigeonOrderInfo;
    private IWXAPI mWxApi = null;
    private int mCpigeonOrderId = 0;
    private String payType;
    PayReq payReq;

    CpigeonData.OnWxPayListener onWxPayListener = new CpigeonData.OnWxPayListener() {
        @Override
        public void onPayFinished(int wxPayReturnCode) {
            if (wxPayReturnCode == ERR_OK)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 500);
            else
                showTips("支付失败", TipType.ToastShort);
        }
    };

    PayPwdInputFragment payFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_pay;
    }

    @Override
    public OrderPayPresenter initPresenter() {
        return new OrderPayPresenter(this);
    }

    @Override
    public void initView() {
        toolbar.setTitle("订单支付");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (mWxApi == null) {
            mWxApi = WXAPIFactory.createWXAPI(mContext, WXPayEntryActivity.APP_ID, true);
            mWxApi.registerApp(WXPayEntryActivity.APP_ID);
        }
        CpigeonData.getInstance().addOnWxPayListener(onWxPayListener);
        mPresenter.loadUserScoreAndBalance();
        mcpigeoCpigeonOrderInfo = (CpigeonOrderInfo) getIntent().getSerializableExtra(INTENT_DATA_KEY_ORDERINFO);
        mCpigeonOrderId = getIntent().getIntExtra(INTENT_DATA_KEY_ORDERID, 0);
        if (mCpigeonOrderId != 0) {
            // TODO: 2017/1/7  加载订单信息（后台加载，新开线程）
            mPresenter.getOrderInfoById(mCpigeonOrderId);
        } else if (mcpigeoCpigeonOrderInfo == null) {
            throw new NullPointerException("intent parameter " + INTENT_DATA_KEY_ORDERINFO + " is null");
        } else {
            showOrderInfo(mcpigeoCpigeonOrderInfo);
        }
    }


    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    public void showOrderInfo(final CpigeonOrderInfo orderInfo) {
        if (orderInfo == null) {
            Logger.e("orderInfo is NULL");
            return;
        }
        if (Looper.getMainLooper() != Looper.myLooper()) {
            ((BaseActivity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showOrderInfo(orderInfo);
                }
            });
            return;
        }
        loadPayWay();
        CpigeonData.DataHelper.getInstance().updateUserBalanceAndScoreFromServer();
        //加载信息
        if (tvOrderNumberContent != null)
            tvOrderNumberContent.setText(orderInfo.getOrderNumber());
        if (tvOrderNameContent != null)
            tvOrderNameContent.setText(orderInfo.getOrderName());
        if (tvOrderTimeContent != null)
            tvOrderTimeContent.setText(orderInfo.getOrderTime());
        if (tvOrderPriceContent != null) {
            tvOrderPriceContent.setText(String.format("%.2f元/%d鸽币", orderInfo.getPrice(), orderInfo.getScores()));
        }
    }

    @Override
    public String getPayType() {
        return payType;
    }

    @Override
    public long getOrderId() {
        return mcpigeoCpigeonOrderInfo == null ? mCpigeonOrderId : mcpigeoCpigeonOrderInfo.getId();
    }

    @Override
    public void showPayResult(Boolean result) {
        SweetAlertDialog dialog;
        if (result) {
            if (payFragment != null) payFragment.dismiss();
            dialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("支付成功").setConfirmText("确认").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            finish();
                        }
                    });
            dialog.setCancelable(false);
            dialog.show();
        } else {
            dialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("支付失败").setConfirmText("确认");
        }
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void entryWXPay(PayReq payReq) {
        this.payReq = payReq;
        if (mWxApi != null) {
            mWxApi.sendReq(payReq);
            Logger.d("发起微信支付");
        }
    }

    @Override
    public PayReq getPayReqCache() {
        return payReq;
    }

    private void loadPayWay() {
        if (layoutOrderPayWay == null || mcpigeoCpigeonOrderInfo == null)
            return;
        layoutOrderPayWay.removeAllViews();
        View splitLine;
        //分割线布局
        ViewGroup.LayoutParams splitLineLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.split_line_width));
        //创建支付方式
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_item_pay_way, null);
        ((TextView) v.findViewById(R.id.tv_pay_way_name)).setText("余额支付");
        ((ImageView) v.findViewById(R.id.iv_pay_icon)).setImageResource(R.drawable.ic_launcher);
        layoutOrderPayWay.addView(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cbOrderProtocol.isChecked()) {
                    showTips(getString(R.string.sentence_not_watch_pay_agreement_prompt), TipType.DialogError);
                    return;
                }
                if (CpigeonData.getInstance().getUserBalance() < mcpigeoCpigeonOrderInfo.getPrice()) {
                    SweetAlertDialog dialogPrompt = new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE);
                    dialogPrompt.setCancelable(false);
                    dialogPrompt.setTitleText("余额不足")
                            .setContentText(String.format("您的当前余额：%.2f元", CpigeonData.getInstance().getUserBalance()))
                            .setConfirmText("确定")
                            .show();
                } else {
                    loadPayFragment(PAY_TYPE_YUE);
                }
            }
        });

        //判断是否可以用鸽币兑换
        if (mcpigeoCpigeonOrderInfo.getScores() != 0) {
            //分割线
            splitLine = new View(mContext);
            splitLine.setLayoutParams(splitLineLayoutParams);
            splitLine.setBackgroundColor(getResources().getColor(R.color.colorLayoutSplitLineGray));
            layoutOrderPayWay.addView(splitLine);

            v = LayoutInflater.from(mContext).inflate(R.layout.layout_item_pay_way, null);
            ((TextView) v.findViewById(R.id.tv_pay_way_name)).setText("鸽币兑换");
            ((ImageView) v.findViewById(R.id.iv_pay_icon)).setImageResource(R.drawable.svg_ic_pay_score);
            layoutOrderPayWay.addView(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!cbOrderProtocol.isChecked()) {
                        showTips(getString(R.string.sentence_not_watch_pay_agreement_prompt), TipType.DialogError);
                        return;
                    }
                    if (CpigeonData.getInstance().getUserScore() < mcpigeoCpigeonOrderInfo.getScores()) {
                        SweetAlertDialog dialogPrompt = new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE);
                        dialogPrompt.setCancelable(false);
                        dialogPrompt.setTitleText("鸽币不足")
                                .setContentText(String.format("您的当前鸽币：%d", CpigeonData.getInstance().getUserScore()))
                                .setConfirmText("如何获取鸽币")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        Intent intent = new Intent(mContext, WebActivity.class);
                                        intent.putExtra(WebActivity.INTENT_DATA_KEY_URL, Const.URL_HELP_GETSCORE);
                                        intent.putExtra(WebActivity.INTENT_DATA_KEY_BACKNAME, "订单支付");
                                        startActivity(intent);
                                        sweetAlertDialog.dismiss();
                                    }
                                })
                                .setCancelText("关闭")
                                .show();
                    } else {
                        loadPayFragment(PAY_TYPE_JIFEN);
                    }
                }
            });
        }

        //分割线
        splitLine = new View(mContext);
        splitLine.setLayoutParams(splitLineLayoutParams);
        splitLine.setBackgroundColor(getResources().getColor(R.color.colorLayoutSplitLineGray));
        layoutOrderPayWay.addView(splitLine);

        v = LayoutInflater.from(mContext).inflate(R.layout.layout_item_pay_way, null);
        ((TextView) v.findViewById(R.id.tv_pay_way_name)).setText("微信支付");
        ((ImageView) v.findViewById(R.id.iv_pay_icon)).setImageResource(R.drawable.ic_launcher);
        layoutOrderPayWay.addView(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cbOrderProtocol.isChecked()) {
                    showTips(getString(R.string.sentence_not_watch_pay_agreement_prompt), TipType.DialogError);
                    return;
                }
                mPresenter.wxPay();
            }
        });

        //分割线
//        splitLine = new View(mContext);
//        splitLine.setLayoutParams(splitLineLayoutParams);
//        splitLine.setBackgroundColor(getResources().getColor(R.color.colorLayoutSplitLineGray));
//        layoutOrderPayWay.addView(splitLine);
//
//        v = LayoutInflater.from(mContext).inflate(R.layout.layout_item_pay_way, null);
//        ((TextView) v.findViewById(R.id.tv_pay_way_name)).setText("支付宝支付");
//        ((ImageView) v.findViewById(R.id.iv_pay_icon)).setImageResource(R.drawable.svg_ic_pay_alipay);
//        layoutOrderPayWay.addView(v);
//        v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (BuildConfig.DEBUG)
//                    aliPay();
//                else
//                    CommonTool.toastShort(mContext, "支付宝支付正在开发中...");
//            }
//        });
    }

    /**
     * 加载支付密码输入Fragment
     *
     * @param type
     */
    private void loadPayFragment(String type) {
        payType = type;
        payFragment = new PayPwdInputFragment();
        payFragment.setOnPayListener(new PayPwdInputFragment.OnPayListener() {
            @Override
            public void onPay(Dialog dialog, String payPwd) {
                mPresenter.payOrder(payPwd);
            }
        });

        //设置信息提示（余额鸽币提示）
        String stringFormat = PAY_TYPE_JIFEN.equals(payType) ? getString(R.string.format_pay_account_score_tips) :
                PAY_TYPE_YUE.equals(payType) ? getString(R.string.format_pay_account_balance_tips) : "";
        if (PAY_TYPE_JIFEN.equals(payType)) {
            payFragment.setPromptInfo(String.format(stringFormat, String.format("%d", CpigeonData.getInstance().getUserScore()),
                    mcpigeoCpigeonOrderInfo == null ? "（未知）" : String.format("%d", mcpigeoCpigeonOrderInfo.getScores())));
            payFragment.setOkText("兑换");
        } else if (PAY_TYPE_YUE.equals(payType)) {
            payFragment.setPromptInfo(String.format(stringFormat, String.format("%.2f", CpigeonData.getInstance().getUserBalance()),
                    mcpigeoCpigeonOrderInfo == null ? "（未知）" : String.format("%.2f", mcpigeoCpigeonOrderInfo.getPrice())));
            payFragment.setOkText("付款");
        }
        payFragment.show(getFragmentManager(), "payFragment");
    }


    @OnClick(R.id.tv_order_protocol)
    public void onViewClicked() {
        Intent intent = new Intent(mContext, WebActivity.class);
        intent.putExtra(WebActivity.INTENT_DATA_KEY_URL, CPigeonApiUrl.getInstance().getServer() + "/APP/Protocol?type=pay");
        intent.putExtra(WebActivity.INTENT_DATA_KEY_BACKNAME, "订单支付");
        startActivity(intent);
    }
}
