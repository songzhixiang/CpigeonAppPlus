package com.cpigeon.app.modular.usercenter.model.daoimpl;

import android.support.annotation.NonNull;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.model.daoimpl.GetWXPrepayOrderImpl;
import com.cpigeon.app.modular.usercenter.model.bean.CpigeonRechargeInfo;
import com.cpigeon.app.modular.usercenter.model.dao.IUserBalanceListDao;
import com.cpigeon.app.utils.CallAPI;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelpay.PayReq;

import org.xutils.common.Callback;

import java.util.List;

/**
 * Created by chenshuai on 2017/4/15.
 */

public class UserBalanceListDaoImpl extends GetWXPrepayOrderImpl implements IUserBalanceListDao {
    @Override
    public Callback.Cancelable getUserBalancePage(int pageIndex, int pageSize, @NonNull final OnCompleteListener<List<CpigeonRechargeInfo.DataBean>> onCompleteListener) {
        return CallAPI.getRechargeList(MyApp.getInstance(), pageSize, pageIndex, new CallAPI.Callback<List<CpigeonRechargeInfo.DataBean>>() {
            @Override
            public void onSuccess(List<CpigeonRechargeInfo.DataBean> data) {
                onCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                Logger.d("加载充值订单列表失败");
                onCompleteListener.onFail("加载充值订单列表失败");
            }
        });
    }
}
