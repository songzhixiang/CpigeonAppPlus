package com.cpigeon.app.modular.order.model.daoimpl;

import android.content.Intent;
import android.telecom.Call;
import android.view.View;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.model.daoimpl.GetUserScoreAndBalanceImpl;
import com.cpigeon.app.modular.order.model.bean.CpigeonOrderInfo;
import com.cpigeon.app.modular.order.model.dao.IOrderPayDao;
import com.cpigeon.app.utils.CallAPI;
import com.cpigeon.app.utils.CpigeonData;
import com.orhanobut.logger.Logger;

import java.util.Map;

/**
 * Created by chenshuai on 2017/4/14.
 */

public class OrderPayDaoImpl extends GetUserScoreAndBalanceImpl implements IOrderPayDao {

    @Override
    public void getOrderInfoById(long orderId, OnCompleteListener<CpigeonOrderInfo> onCompleteListener) {
        throw new RuntimeException("no 'getOrderInfoById' server api");
    }

    @Override
    public void orderPayByScore(long orderId, String payPwd, final OnCompleteListener<Boolean> onCompleteListener) {
        CallAPI.orderPayByScore(MyApp.getInstance(), orderId, payPwd, new CallAPI.Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                Logger.d(String.valueOf(data));
                if (data) {
                    //支付成功
                    new Thread() {
                        @Override
                        public void run() {
                            CallAPI.getUserYuEAndJiFen(MyApp.getInstance(), new CallAPI.Callback<Map<String, Object>>() {
                                @Override
                                public void onSuccess(Map<String, Object> data) {
                                    CpigeonData.getInstance().setUserBalance((double) data.get("yue"));
                                    CpigeonData.getInstance().setUserScore((int) data.get("jifen"));
                                }

                                @Override
                                public void onError(int errorType, Object data) {

                                }
                            });
                        }
                    }.start();

                }
                onCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                Logger.i(String.valueOf(errorType));
                String msg = "支付失败，请稍后再试";
                switch (errorType) {
                    case CallAPI.Callback.ERROR_TYPE_API_RETURN:
                        switch ((int) data) {
                            case 20001:
                                msg = "未找到此订单";
                                break;
                            case 20002:
                            case 20003:
                                msg = "订单状态异常";
                                break;
                            case 20004:
                                msg = "订单不能使用鸽币支付";
                                break;
                            case 20005:
                                msg = "订单已过期,请重新下订单";
                                break;
                            case 20006:
                                msg = "鸽币不足";
                                break;
                            case 20007:
                                msg = "未设置支付密码";
                                break;
                            case 20008:
                                msg = "支付密码错误";
                                break;
                        }
                        break;
                }
                onCompleteListener.onFail(msg);
            }
        });
    }

    @Override
    public void orderPayByBalance(long orderId, String payPwd, final OnCompleteListener<Boolean> onCompleteListener) {
        CallAPI.orderPayByBalance(MyApp.getInstance(), orderId, payPwd, new CallAPI.Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                Logger.d(String.valueOf(data));
                if (data) {
                    //支付成功
                    new Thread() {
                        @Override
                        public void run() {
                            CallAPI.getUserYuEAndJiFen(MyApp.getInstance(), new CallAPI.Callback<Map<String, Object>>() {
                                @Override
                                public void onSuccess(Map<String, Object> data) {
                                    CpigeonData.getInstance().setUserBalance((double) data.get("yue"));
                                    CpigeonData.getInstance().setUserScore((int) data.get("jifen"));
                                }

                                @Override
                                public void onError(int errorType, Object data) {

                                }
                            });
                        }
                    }.start();

                }
                onCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                Logger.i(String.valueOf(errorType));
                String msg = "支付失败，请稍后再试";
                switch (errorType) {
                    case CallAPI.Callback.ERROR_TYPE_API_RETURN:
                        switch ((int) data) {
                            case 20001:
                                msg = "未找到此订单";
                                break;
                            case 20002:
                            case 20003:
                                msg = "订单状态异常";
                                break;
                            case 20004:
                                msg = "订单不能使用余额支付";
                                break;
                            case 20005:
                                msg = "订单已过期,请重新下订单";
                                break;
                            case 20006:
                                msg = "余额不足";
                                break;
                            case 20007:
                                msg = "未设置支付密码";
                                break;
                            case 20008:
                                msg = "支付密码错误";
                                break;
                        }
                        break;
                }
                onCompleteListener.onFail(msg);
            }
        });
    }

}
