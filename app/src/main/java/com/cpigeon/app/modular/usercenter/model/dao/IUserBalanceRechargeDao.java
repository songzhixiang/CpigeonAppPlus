package com.cpigeon.app.modular.usercenter.model.dao;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.model.dao.IGetWXPrepayOrder;
import com.cpigeon.app.modular.usercenter.model.bean.CpigeonRechargeInfo;

/**
 * Created by chenshuai on 2017/4/15.
 */

public interface IUserBalanceRechargeDao extends IBaseDao, IGetWXPrepayOrder {
    void createRechargeOrder(double money, final int method, OnCompleteListener<CpigeonRechargeInfo.DataBean> onCompleteListener);
}
