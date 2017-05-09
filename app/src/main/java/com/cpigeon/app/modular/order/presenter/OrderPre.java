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
 * Created by Administrator on 2017/4/11.
 */

public class OrderPre extends BasePresenter<IOrderView, OrderDao> {


    public OrderPre(IOrderView mView) {
        super(mView);
    }

    public void loadOrder() {
        if (isDetached()) return;
        if (mView.getPageIndex() == 1)
            mView.showRefreshLoading();
        mDao.getUserAllOrder(mView.getPageSize(), mView.getPageIndex(), mView.getQuery(), new IBaseDao.OnCompleteListener<List<CpigeonOrderInfo>>() {

            @Override
            public void onSuccess(final List<CpigeonOrderInfo> data) {
                postDelayed(new CheckAttachRunnable() {
                    @Override
                    public void runAttached() {
                        if (mView.isMoreDataLoading()) {
                            mView.loadMoreComplete();
                        } else {
                            mView.hideRefreshLoading();
                        }
                        mView.showMoreData(data);
                    }
                }, 300);
            }

            @Override
            public void onFail(String msg) {
                postDelayed(new CheckAttachRunnable() {
                    @Override
                    public void runAttached() {
                        if (mView.isMoreDataLoading()) {
                            mView.loadMoreFail();
                        } else {
                            mView.hideRefreshLoading();
                            mView.showTips("订单记录加载失败", IView.TipType.View);
                        }
                    }
                }, 300);
            }
        });
    }

    @Override
    protected OrderDao initDao() {
        return new OrderDaoImpl();
    }
}
