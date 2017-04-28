package com.cpigeon.app.modular.usercenter.model.dao;

import android.support.annotation.NonNull;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.model.dao.IGetWXPrepayOrder;
import com.cpigeon.app.modular.usercenter.model.bean.CpigeonRechargeInfo;
import com.tencent.mm.sdk.modelpay.PayReq;

import org.xutils.common.Callback;

import java.util.List;

/**
 * Created by chenshuai on 2017/4/15.
 */

public interface IUserBalanceListDao extends IBaseDao, IGetWXPrepayOrder {

    Callback.Cancelable getUserBalancePage(int pageIndex, int pageSize, @NonNull OnCompleteListener<List<CpigeonRechargeInfo.DataBean>> onCompleteListener);
}
