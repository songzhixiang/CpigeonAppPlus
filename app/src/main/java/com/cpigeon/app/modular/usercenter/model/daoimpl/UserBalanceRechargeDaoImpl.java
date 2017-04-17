package com.cpigeon.app.modular.usercenter.model.daoimpl;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.daoimpl.GetWXPrepayOrderImpl;
import com.cpigeon.app.modular.usercenter.model.bean.CpigeonRechargeInfo;
import com.cpigeon.app.modular.usercenter.model.dao.IUserBalanceRechargeDao;
import com.cpigeon.app.utils.CallAPI;

/**
 * Created by chenshuai on 2017/4/15.
 */

public class UserBalanceRechargeDaoImpl extends GetWXPrepayOrderImpl implements IUserBalanceRechargeDao {


    @Override
    public void createRechargeOrder(double money, int method, final OnCompleteListener<CpigeonRechargeInfo.DataBean> onCompleteListener) {
        CallAPI.createRechargeOrder(MyApp.getInstance(), money, method, new CallAPI.Callback<CpigeonRechargeInfo.DataBean>() {
            @Override
            public void onSuccess(CpigeonRechargeInfo.DataBean data) {
                onCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                onCompleteListener.onFail("支付失败");
            }
        });
    }
}
