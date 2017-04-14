package com.cpigeon.app.modular.order.view.activity.viewdao;

import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.order.model.bean.CpigeonOrderInfo;

/**
 * Created by chenshuai on 2017/4/14.
 */

public interface IOrderPayView extends IView {
    void showOrderInfo(CpigeonOrderInfo orderInfo);
}
