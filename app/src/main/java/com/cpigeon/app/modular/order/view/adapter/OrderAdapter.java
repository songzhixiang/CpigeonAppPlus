package com.cpigeon.app.modular.order.view.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.app.R;
import com.cpigeon.app.modular.order.model.bean.CpigeonOrderInfo;
import com.cpigeon.app.utils.DateTool;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */

public class OrderAdapter extends BaseQuickAdapter<CpigeonOrderInfo, BaseViewHolder> {
    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public OrderAdapter(List<CpigeonOrderInfo> data) {
        super(R.layout.listitem_user_order_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CpigeonOrderInfo item) {
        helper.setText(R.id.tv_item_time, dateTimeFormat.format(DateTool.strToDateTime(item.getOrderTime())));
        helper.setText(R.id.tv_item_name, item.getOrderName());
        helper.setText(R.id.tv_item_order_number, String.format("订单编号：%s", item.getOrderNumber()));
        helper.setText(R.id.tv_item_order_status, item.getStatusName());
        int orderStatusColor = Color.parseColor("#8B8A8A");//默认颜色
        switch (item.getStatusName()) {
            case "交易完成":
                orderStatusColor = mContext.getResources().getColor(R.color.colorBlue);
                break;
            case "待支付":
                orderStatusColor = mContext.getResources().getColor(R.color.colorRed);
                helper.setText(R.id.tv_item_order_status, "待支付，去支付");
                break;
        }
        helper.setTextColor(R.id.tv_item_order_status, orderStatusColor);
        helper.setText(R.id.tv_item_order_price, item.getPrice() > 0 && item.getScores() > 0 ?
                String.format("%.2f元/%d鸽币", item.getPrice(), item.getScores()) :
                item.getPrice() > 0 ? String.format("%.2f元", item.getPrice()) : String.format("%d鸽币", item.getScores()));

        helper.setText(R.id.tv_item_order_payway, String.format("%s", item.getPayWay().replace("积分","鸽币")));
        helper.getView(R.id.tv_item_order_payway).setVisibility(item.ispaid() ? View.VISIBLE : View.GONE);
    }
}
