package com.cpigeon.app.modular.order.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.cpigeon.app.modular.order.view.activity.viewdao.IOrderPayView;
import com.cpigeon.app.utils.CPigeonApiUrl;
import com.cpigeon.app.utils.CallAPI;
import com.cpigeon.app.utils.Const;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.NetUtils;
import com.cpigeon.app.wxapi.WXPayEntryActivity;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by chenshuai on 2017/4/14.
 */

public class OrderPayActivity extends BaseActivity implements IOrderPayView {

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
    CpigeonData.OnWxPayListener onWxPayListener = new CpigeonData.OnWxPayListener() {
        @Override
        public void onPayFinished(int wxPayReturnCode) {
            finish();
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_pay;
    }

    @Override
    public void initPresenter() {

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
        mcpigeoCpigeonOrderInfo = (CpigeonOrderInfo) getIntent().getSerializableExtra(INTENT_DATA_KEY_ORDERINFO);
        mCpigeonOrderId = getIntent().getIntExtra(INTENT_DATA_KEY_ORDERID, 0);
        if (mCpigeonOrderId != 0) {
            // TODO: 2017/1/7  加载订单信息（后台加载，新开线程）
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
        //加载信息
        if (tvOrderNumberContent != null)
            tvOrderNumberContent.setText(orderInfo.getOrderNumber());
        if (tvOrderNameContent != null)
            tvOrderNameContent.setText(orderInfo.getOrderName());
        if (tvOrderTimeContent != null)
            tvOrderTimeContent.setText(orderInfo.getOrderTime());
        if (tvOrderPriceContent != null) {
            tvOrderPriceContent.setText(String.format("%.2f元/%d积分", orderInfo.getPrice(), orderInfo.getScores()));
        }
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
        ((ImageView) v.findViewById(R.id.iv_pay_icon)).setImageResource(R.drawable.svg_ic_pay_balance);
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
//                    loadPayFragment(PAY_TYPE_YUE);
                }
            }
        });

        //判断是否可以用积分兑换
        if (mcpigeoCpigeonOrderInfo.getScores() != 0) {
            //分割线
            splitLine = new View(mContext);
            splitLine.setLayoutParams(splitLineLayoutParams);
            splitLine.setBackgroundColor(getResources().getColor(R.color.colorLayoutSplitLineGray));
            layoutOrderPayWay.addView(splitLine);

            v = LayoutInflater.from(mContext).inflate(R.layout.layout_item_pay_way, null);
            ((TextView) v.findViewById(R.id.tv_pay_way_name)).setText("积分兑换");
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
                        dialogPrompt.setTitleText("积分不足")
                                .setContentText(String.format("您的当前积分：%d", CpigeonData.getInstance().getUserScore()))
                                .setConfirmText("如何获取积分")
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
//                        loadPayFragment(PAY_TYPE_JIFEN);
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
        ((ImageView) v.findViewById(R.id.iv_pay_icon)).setImageResource(R.drawable.svg_ic_pay_weixin);
        layoutOrderPayWay.addView(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wxPrePay();
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
     * 微信支付(获取微信预支付订单)
     */
    private void wxPrePay() {
        if (!cbOrderProtocol.isChecked()) {
            showTips(getString(R.string.sentence_not_watch_pay_agreement_prompt), TipType.DialogError);
            return;
        }

        if (mcpigeoCpigeonOrderInfo == null) {
//            CommonTool.toastShort(mContext, "无法识别当前订单，请稍后再试...");
            return;
        }
//        mJumpDialog.show();
        //获取微信预支付订单

//        RequestParams requestParams = new RequestParams(CPigeonApiUrl.getInstance().getServer() + CPigeonApiUrl.GET_WX_PREPAY_ORDER_URL);
//        CallAPI.pretreatmentParams(requestParams);
//        requestParams.addParameter("oid", mcpigeoCpigeonOrderInfo.getId());
//        requestParams.addParameter("u", CpigeonData.getInstance().getUserId(mContext));
//        requestParams.addHeader("u", CommonTool.getUserToken(mContext));
//        x.http().get(requestParams, new Callback.CommonCallback<JSONObject>() {
//
//            @Override
//            public void onSuccess(JSONObject result) {
//                Logger.d(result.toString());
//                if (!result.has("status") || result.isNull("data")) {
//                    CommonTool.toastShort(mContext, "发起支付失败，请稍后再试...");
//                    mJumpDialog.dismiss();
//                    return;
//                }
//                PayReq req = new PayReq();
//                try {
//                    JSONObject obj = result.getJSONObject("data");
//                    req.appId = obj.getString("appid");// 微信开放平台审核通过的应用APPID
//                    req.partnerId = obj.getString("partnerid");// 微信支付分配的商户号
//                    req.prepayId = obj.getString("prepayid");// 预支付订单号，app服务器调用“统一下单”接口获取
//                    req.nonceStr = obj.getString("noncestr");// 随机字符串，不长于32位，服务器小哥会给咱生成
//                    req.timeStamp = obj.getString("timestamp");// 时间戳，app服务器小哥给出
//                    req.packageValue = obj.getString("package");// 固定值Sign=WXPay，可以直接写死，服务器返回的也是这个固定值
//                    req.sign = obj.getString("sign");// 签名，服务器小哥给出，他会根据：https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_3指导得到这个
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                if (mWxApi != null) {
//                    mWxApi.sendReq(req);
//                    Logger.d("发起微信支付");
//                } else {
//                    CommonTool.toastShort(mContext, "发起支付失败，请稍后再试...");
//                }
//                mJumpDialog.dismiss();
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                CommonTool.toastShort(mContext, "发起支付失败，请稍后再试...");
//                mJumpDialog.dismiss();
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//            }
//        });

    }

    public interface OnPayFinishListener {
        void onFinish(boolean isSuccess);
    }
}
