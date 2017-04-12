package com.cpigeon.app.modular.usercenter.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.app.R;
import com.cpigeon.app.modular.order.model.bean.CpigeonOrderInfo;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by chenshuai on 2017/4/11.
 */

public class OrderItemAdapter extends BaseQuickAdapter<CpigeonOrderInfo, BaseViewHolder> {
    public OrderItemAdapter(int layoutResId, List<CpigeonOrderInfo> data) {
        super(layoutResId, data);
    }

    public OrderItemAdapter(List<CpigeonOrderInfo> data) {
        super(data);
    }

    public OrderItemAdapter(int layoutResId) {
        super(layoutResId);
    }

    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void convert(BaseViewHolder helper, CpigeonOrderInfo item) {
        if (item == null) {
            return;
        }

    }
}
