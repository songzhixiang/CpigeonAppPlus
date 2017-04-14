package com.cpigeon.app.modular.order.view.activity.viewdao;

import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.order.model.bean.CpigeonOrderInfo;
import com.tencent.mm.sdk.modelpay.PayReq;

/**
 * Created by chenshuai on 2017/4/14.
 */

public interface IOrderPayView extends IView {
    String PAY_TYPE_JIFEN = "jifen";
    String PAY_TYPE_YUE = "yue";

    void showOrderInfo(CpigeonOrderInfo orderInfo);

    String getPayType();

    long getOrderId();

    void showPayResult(Boolean result);

    void entryWXPay(PayReq payReq);

    PayReq getPayReqCache();
}
