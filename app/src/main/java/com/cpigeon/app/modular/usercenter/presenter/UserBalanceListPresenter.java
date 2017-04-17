package com.cpigeon.app.modular.usercenter.presenter;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.usercenter.model.bean.CpigeonRechargeInfo;
import com.cpigeon.app.modular.usercenter.model.dao.IUserBalanceListDao;
import com.cpigeon.app.modular.usercenter.model.daoimpl.UserBalanceListDaoImpl;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.IUserBalanceListView;

import org.xutils.common.Callback;

import java.util.List;

/**
 * Created by chenshuai on 2017/4/15.
 */

public class UserBalanceListPresenter extends BasePresenter<IUserBalanceListView, IUserBalanceListDao> {

    public UserBalanceListPresenter(IUserBalanceListView mView) {
        super(mView);
    }

    @Override
    protected IUserBalanceListDao initDao() {
        return new UserBalanceListDaoImpl();
    }

    IBaseDao.OnCompleteListener<List<CpigeonRechargeInfo.DataBean>> onCompleteListener = new IBaseDao.OnCompleteListener<List<CpigeonRechargeInfo.DataBean>>() {
        @Override
        public void onSuccess(final List<CpigeonRechargeInfo.DataBean> data) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isAttached()) {
                        mView.hideRefreshLoading();
                        mView.showMoreData(data);
                    }
                }
            }, 300);
        }

        @Override
        public void onFail(final String msg) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isAttached()) {
                        mView.hideRefreshLoading();
                        mView.showTips("加载失败", IView.TipType.View);
                    }
                }
            }, 300);
        }
    };

    public void loadUserBalancePage() {
        if (isDetached()) return;
        if (mView.getPageIndex() == 1) mView.showRefreshLoading();

        Callback.Cancelable cancelable = mDao.getUserBalancePage(mView.getPageIndex(), mView.getPageSize(), onCompleteListener);
        addCancelableIntoMap("loadUserBalancePage", cancelable);
    }
}
