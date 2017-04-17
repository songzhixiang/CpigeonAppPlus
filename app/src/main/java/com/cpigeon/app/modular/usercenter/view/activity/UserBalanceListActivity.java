package com.cpigeon.app.modular.usercenter.view.activity;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

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
    BaseQuickAdapter.OnItemClickListener listener = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            CpigeonRechargeInfo.DataBean item = ((UserBalanceAdapter) adapter).getData().get(position);
            if ("待充值".equals(item.getStatusname())) {
                showTips("点击了" + item.getNumber(), TipType.ToastShort);
            }
        }
    };

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
        UserBalanceAdapter adapter = new UserBalanceAdapter(null);
        adapter.setOnItemClickListener(listener);
        return adapter;
    }

    @Override
    protected void loadDataByPresenter() {
        mPresenter.loadUserBalancePage();
    }
}
