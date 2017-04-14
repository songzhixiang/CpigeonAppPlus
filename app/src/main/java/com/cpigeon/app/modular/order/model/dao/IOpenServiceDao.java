package com.cpigeon.app.modular.order.model.dao;

import android.support.annotation.NonNull;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.order.model.bean.CpigeonOrderInfo;
import com.cpigeon.app.modular.order.model.bean.CpigeonServicesInfo;

import java.util.List;

/**
 * Created by chenshuai on 2017/4/14.
 */

public interface IOpenServiceDao extends IBaseDao {

    void loadAllServicesInfo(String serviceName, @NonNull OnCompleteListener<List<CpigeonServicesInfo>> onCompleteListener);


    void createServiceOrder(int serviecId, @NonNull OnCompleteListener<CpigeonOrderInfo> onCompleteListener);
}
