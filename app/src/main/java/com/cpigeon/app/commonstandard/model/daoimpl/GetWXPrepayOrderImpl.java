package com.cpigeon.app.commonstandard.model.daoimpl;

import android.support.annotation.NonNull;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.model.dao.IGetWXPrepayOrder;
import com.cpigeon.app.utils.CallAPI;
import com.tencent.mm.sdk.modelpay.PayReq;

/**
 * Created by chenshuai on 2017/4/14.
 */

public class GetWXPrepayOrderImpl implements IGetWXPrepayOrder {
    @Override
    public void getWXPrepayOrderForOrder(long orderId, @NonNull final IBaseDao.OnCompleteListener<PayReq> onCompleteListener) {
        CallAPI.getWXPrePayOrderForOrder(MyApp.getInstance(), orderId, new CallAPI.Callback<PayReq>() {
            @Override
            public void onSuccess(PayReq data) {
                onCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                String msg = "创建微信支付订单失败";
                if (errorType == CallAPI.Callback.ERROR_TYPE_API_RETURN) {
                    switch ((int) data) {
                        case 20001:
                            msg = "未找到此订单";
                            break;
                        case 20002:
                            msg = "订单不能被支付";
                            break;
                        case 20003:
                            msg = "订单状态异常";
                            break;
                        case 20004:
                            msg = "订单不能使用现金支付";
                            break;
                        case 20005:
                            msg = "订单已过期";
                            break;
                    }
                }
                onCompleteListener.onFail(msg);
            }
        });
    }

    @Override
    public void getWXPrePayOrderForRecharge(long depositId, @NonNull final IBaseDao.OnCompleteListener<PayReq> onCompleteListener) {
        CallAPI.getWXPrePayOrderForRecharge(MyApp.getInstance(), depositId, new CallAPI.Callback<PayReq>() {
            @Override
            public void onSuccess(PayReq data) {
                onCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                String msg = "创建微信支付订单失败";
                if (errorType == CallAPI.Callback.ERROR_TYPE_API_RETURN) {
                    switch ((int) data) {
                        case 20001:
                            msg = "未找到此订单";
                            break;
                        case 20002:
                            msg = "订单不能被支付";
                            break;
                        case 20003:
                            msg = "订单状态异常";
                            break;
                        case 20004:
                            msg = "订单不能使用现金支付";
                            break;
                        case 20005:
                            msg = "订单已过期";
                            break;
                    }
                }
                onCompleteListener.onFail(msg);
            }
        });
    }
}
