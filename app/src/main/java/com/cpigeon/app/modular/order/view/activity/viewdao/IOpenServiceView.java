package com.cpigeon.app.modular.order.view.activity.viewdao;

import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.order.model.bean.CpigeonOrderInfo;
import com.cpigeon.app.modular.order.model.bean.CpigeonServicesInfo;

import java.util.List;

/**
 * Created by chenshuai on 2017/4/14.
 */

public interface IOpenServiceView extends IView {
    int TAG_LoadServiceInfo = 123;

    String getOpenServiceName();

    void showServicesInfo(List<CpigeonServicesInfo> infoList);

    void createServiceOrderSuccess(CpigeonOrderInfo orderInfo);
}
