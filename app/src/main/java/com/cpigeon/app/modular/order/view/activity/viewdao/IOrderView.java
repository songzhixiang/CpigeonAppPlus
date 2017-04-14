package com.cpigeon.app.modular.order.view.activity.viewdao;

import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.order.model.bean.CpigeonOrderInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */

public interface IOrderView extends IView{
    void showOrder(List<CpigeonOrderInfo> orderInfos);
    int getPs();
    int getPi();
    String getQuery();

}
