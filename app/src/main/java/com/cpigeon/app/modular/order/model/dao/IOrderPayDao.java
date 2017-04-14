package com.cpigeon.app.modular.order.model.dao;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.model.dao.IGetUserScoreAndBalance;
import com.cpigeon.app.modular.order.model.bean.CpigeonOrderInfo;

/**
 * Created by chenshuai on 2017/4/14.
 */

public interface IOrderPayDao extends IBaseDao, IGetUserScoreAndBalance {
    void getOrderInfoById(long orderId, OnCompleteListener<CpigeonOrderInfo> onCompleteListener);

    void orderPayByScore(long orderId, String payPwd, OnCompleteListener<Boolean> onCompleteListener);

    void orderPayByBalance(long orderId, String payPwd, OnCompleteListener<Boolean> onCompleteListener);
}
