package com.cpigeon.app.modular.usercenter.view.activity;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.commonstandard.view.activity.BasePageTurnActivity;
import com.cpigeon.app.modular.usercenter.model.bean.CpigeonRechargeInfo;
import com.cpigeon.app.modular.usercenter.presenter.UserBalanceListPresenter;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.IUserBalanceListView;
import com.cpigeon.app.modular.usercenter.view.adapter.UserBalanceAdapter;

/**
 * Created by Administrator on 2017/4/10.
 */

public class UserBalanceListActivity extends BasePageTurnActivity<UserBalanceListPresenter, UserBalanceAdapter, CpigeonRechargeInfo.DataBean>
        implements IUserBalanceListView {

    @Override
    public UserBalanceListPresenter initPresenter() {
        return new UserBalanceListPresenter(this);
    }


    @NonNull
    @Override
    public String getTitleName() {
        return "充值记录";
    }

    @Override
    public int getDefaultPageSize() {
        return 10;
    }

    @Override
    protected String getEmptyDataTips() {
        return "还没有充值记录";
    }

    @Override
    public UserBalanceAdapter getNewAdapterWithNoData() {
        return new UserBalanceAdapter(null);
    }

    @Override
    protected void loadDataByPresenter() {
        mPresenter.loadUserBalancePage();
    }
}
