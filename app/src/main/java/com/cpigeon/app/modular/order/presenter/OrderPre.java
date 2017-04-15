package com.cpigeon.app.modular.order.presenter;

import android.os.Handler;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.order.model.bean.CpigeonOrderInfo;
import com.cpigeon.app.modular.order.model.dao.OrderDao;
import com.cpigeon.app.modular.order.model.daoimpl.OrderDaoImpl;
import com.cpigeon.app.modular.order.view.activity.viewdao.IOrderView;

import java.util.List;

/**
 *
 * Created by Administrator on 2017/4/11.
 */

public class OrderPre extends BasePresenter<IOrderView,OrderDao>{


    public OrderPre(IOrderView mView) {
        super(mView);
    }

    public void loadOrder() {
        mView.showRefreshLoading();
        mDao.getUserAllOrder(mView.getPs(), mView.getPi(), mView.getQuery(), new IBaseDao.OnCompleteListener<List<CpigeonOrderInfo>>() {

            @Override
            public void onSuccess(final List<CpigeonOrderInfo> data) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mView.hideRefreshLoading();
                        if (data == null ||data.size()<=0)
                        {
                            mView.showEmptyData();
                        }else {
                            mView.showOrder(data);
                        }


                    }
                });
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    @Override
    protected OrderDao initDao() {
        return new OrderDaoImpl();
    }
}
