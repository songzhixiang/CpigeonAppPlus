package com.cpigeon.app.modular.order.presenter;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.order.model.bean.CpigeonOrderInfo;
import com.cpigeon.app.modular.order.model.bean.CpigeonServicesInfo;
import com.cpigeon.app.modular.order.model.dao.IOpenServiceDao;
import com.cpigeon.app.modular.order.model.daoimpl.OpenServiceDaoImpl;
import com.cpigeon.app.modular.order.view.activity.viewdao.IOpenServiceView;

import java.util.List;

/**
 * Created by chenshuai on 2017/4/14.
 */

public class OpenServicePresenter extends BasePresenter<IOpenServiceView, IOpenServiceDao> {
    public OpenServicePresenter(IOpenServiceView mView) {
        super(mView);
    }

    @Override
    protected IOpenServiceDao initDao() {
        return new OpenServiceDaoImpl();
    }

    public void loadAllServicesInfo() {
        mView.showTips("加载中...", IView.TipType.LoadingShow, mView.TAG_LoadServiceInfo);
        mDao.loadAllServicesInfo(mView.getOpenServiceName(), new IBaseDao.OnCompleteListener<List<CpigeonServicesInfo>>() {
            @Override
            public void onSuccess(final List<CpigeonServicesInfo> data) {
                postDelayed(new CheckAttachRunnable() {
                    @Override
                    protected void runAttached() {
                        mView.showTips(null, IView.TipType.LoadingHide, mView.TAG_LoadServiceInfo);
                        mView.showServicesInfo(data);
                    }
                }, 100);
            }

            @Override
            public void onFail(final String msg) {
                postDelayed(new CheckAttachRunnable() {
                    @Override
                    public void runAttached() {
                        mView.showTips(null, IView.TipType.LoadingHide, mView.TAG_LoadServiceInfo);
                        mView.showTips(msg, IView.TipType.DialogError);
                    }
                }, 100);
            }
        });
    }

    public void openService(int serviceId) {
        mView.showTips("创建订单中...", IView.TipType.LoadingShow);
        mDao.createServiceOrder(serviceId, new IBaseDao.OnCompleteListener<CpigeonOrderInfo>() {
            @Override
            public void onSuccess(final CpigeonOrderInfo data) {
                postDelayed(new CheckAttachRunnable() {
                    @Override
                    public void runAttached() {
                        mView.showTips(null, IView.TipType.LoadingHide);
                        mView.createServiceOrderSuccess(data);
                    }
                }, 300);
            }

            @Override
            public void onFail(final String msg) {
                postDelayed(new CheckAttachRunnable() {
                    @Override
                    public void runAttached() {
                        mView.showTips(null, IView.TipType.LoadingHide);
                        mView.showTips(msg, IView.TipType.DialogError);
                    }
                }, 300);

            }
        });
    }
}
