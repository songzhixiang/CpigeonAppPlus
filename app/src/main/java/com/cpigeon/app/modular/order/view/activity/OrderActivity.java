package com.cpigeon.app.modular.order.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.activity.BasePageTurnActivity;
import com.cpigeon.app.modular.order.model.bean.CpigeonOrderInfo;
import com.cpigeon.app.modular.order.presenter.OrderPre;
import com.cpigeon.app.modular.order.view.activity.viewdao.IOrderView;
import com.cpigeon.app.modular.order.view.adapter.OrderAdapter;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.NetUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/11.
 */

public class OrderActivity extends BasePageTurnActivity<OrderPre, OrderAdapter, CpigeonOrderInfo> implements IOrderView {

    BaseQuickAdapter.OnItemClickListener onItemClickListener = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            CpigeonOrderInfo orderInfo = (CpigeonOrderInfo) adapter.getData().get(position);
            if (!orderInfo.ispaid() && "待支付".equals(orderInfo.getStatusName())) {
                Intent intent = new Intent(mContext, OrderPayActivity.class);
                intent.putExtra(OrderPayActivity.INTENT_DATA_KEY_ORDERINFO, orderInfo);
                startActivity(intent);
            }
        }
    };
    CpigeonData.OnWxPayListener onWxPayListener = new CpigeonData.OnWxPayListener() {
        @Override
        public void onPayFinished(int wxPayReturnCode) {
            if (wxPayReturnCode == ERR_OK)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onRefresh();
                    }
                }, 400);
            else
                showTips("支付失败", TipType.ToastShort);
        }
    };

    @Override
    public void initView() {
        super.initView();
        CpigeonData.getInstance().addOnWxPayListener(onWxPayListener);
    }

    @Override
    public OrderPre initPresenter() {
        return new OrderPre(this);
    }

    @NonNull
    @Override
    public String getTitleName() {
        return "我的订单";
    }

    @Override
    public int getDefaultPageSize() {
        return 8;
    }

    @Override
    protected String getEmptyDataTips() {
        return "您还没有任何订单哦，快去下单吧!";
    }

    @Override
    public String getQuery() {
        return null;
    }

    @Override
    public OrderAdapter getNewAdapterWithNoData() {
        OrderAdapter adapter = new OrderAdapter(null);
        adapter.setOnItemClickListener(onItemClickListener);
        return adapter;
    }

    @Override
    protected void loadDataByPresenter() {
        mPresenter.loadOrder();
    }
}


