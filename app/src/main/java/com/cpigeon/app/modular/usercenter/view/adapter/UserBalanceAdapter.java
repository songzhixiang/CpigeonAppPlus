package com.cpigeon.app.modular.usercenter.view.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.app.R;
import com.cpigeon.app.modular.usercenter.model.bean.CpigeonRechargeInfo;
import com.cpigeon.app.modular.usercenter.model.bean.UserScore;
import com.cpigeon.app.utils.DateTool;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by chenshuai on 2017/4/13.
 */

public class UserBalanceAdapter extends BaseQuickAdapter<CpigeonRechargeInfo.DataBean, BaseViewHolder> {

    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy.MM.dd");
    int orderStatusColor = Color.parseColor("#8B8A8A");//默认颜色

    public UserBalanceAdapter(List<CpigeonRechargeInfo.DataBean> data) {
        super(R.layout.listitem_user_order_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CpigeonRechargeInfo.DataBean item) {
        if (item == null) {
            return;
        }
//        helper.setText(R.id.tv_item_name, item.getItem());

        helper.setVisible(R.id.tv_item_name, false);
        helper.setText(R.id.tv_item_order_number, String.format("订单编号：%s", item.getNumber()));
        helper.setText(R.id.tv_item_order_status, item.getStatusname());
        helper.setText(R.id.tv_item_order_price, String.format("%.2f元", item.getPrice()));
        helper.setText(R.id.tv_item_order_payway, item.getPayway());

        switch (item.getStatusname()) {
            case "充值完成":
                orderStatusColor = mContext.getResources().getColor(R.color.colorBlue);
                break;
            case "待充值":
                orderStatusColor = mContext.getResources().getColor(R.color.colorRed);
                helper.setText(R.id.tv_item_order_status, "待充值，去支付");
                break;
            default:
                orderStatusColor = Color.parseColor("#8B8A8A");
                break;
        }
        helper.setTextColor(R.id.tv_item_order_status, orderStatusColor);
        helper.setText(R.id.tv_item_time, dateTimeFormat.format(DateTool.strToDate(item.getTime())));
//        helper.setVisible(R.id.tv_item_explain, false);
    }
}
