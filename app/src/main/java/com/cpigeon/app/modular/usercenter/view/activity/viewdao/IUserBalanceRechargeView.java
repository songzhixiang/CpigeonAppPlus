package com.cpigeon.app.modular.usercenter.view.activity.viewdao;

import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.usercenter.model.bean.CpigeonRechargeInfo;
import com.tencent.mm.sdk.modelpay.PayReq;

/**
 * Created by chenshuai on 2017/4/15.
 */

public interface IUserBalanceRechargeView extends IView {

    int TYPE_PAY_WAY_WXPAY = 1;
    int TYPE_PAY_WAY_ALIPAY = 2;

    /**
     * 获取支付总金额
     *
     * @return
     */
    double getPayTotalFee();

    /**
     * 获取输入的金额
     *
     * @return
     */
    double getInputFee();

    /**
     * 获取支付方式
     *
     * @return
     */
    int getPayway();

    /**
     * 获取是否同意支付协议
     *
     * @return
     */
    boolean isAgreePayProtocol();

    /**
     * 微信支付跳转
     *
     * @param payReq
     */
    void onWXPay(PayReq payReq);

    /**
     * 获取微信支付订单缓存
     *
     * @return
     */
    PayReq getWxPayReqCache();

    /**
     * 支付宝支付跳转
     *
     * @param dataBean
     */
    void onAliPay(CpigeonRechargeInfo.DataBean dataBean);
}
