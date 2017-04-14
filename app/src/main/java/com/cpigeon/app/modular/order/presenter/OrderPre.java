package com.cpigeon.app.modular.order.presenter;

import android.os.Handler;

import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.order.model.bean.CpigeonOrderInfo;
import com.cpigeon.app.modular.order.model.dao.OrderDao;
import com.cpigeon.app.modular.order.model.daoimpl.OrderDaoImpl;
import com.cpigeon.app.modular.order.view.activity.viewdao.IOrderView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */

public class OrderPre {
    private IOrderView iOrderView;
    private OrderDao orderDao;
    private Handler mHanlder = new Handler();

    public OrderPre(IOrderView iOrderView) {
        this.iOrderView = iOrderView;
        this.orderDao = new OrderDaoImpl();
    }

    public void loadOrder() {
        orderDao.getUserAllOrder(iOrderView.getPs(), iOrderView.getPi(), iOrderView.getQuery(), new OrderDao.OnLoadCompleteListener() {
            @Override
            public void loadSuccess(final List<CpigeonOrderInfo> orderInfos) {
                mHanlder.post(new Runnable() {
                    @Override
                    public void run() {
                        iOrderView.showOrder(orderInfos);
                    }
                });
            }

            @Override
            public void loadFailed() {

            }
        });
    }
}
