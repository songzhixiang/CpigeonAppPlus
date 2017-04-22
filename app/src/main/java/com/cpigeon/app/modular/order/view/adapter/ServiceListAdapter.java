package com.cpigeon.app.modular.order.view.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.app.R;
import com.cpigeon.app.modular.order.model.bean.CpigeonServicesInfo;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.DateTool;

import java.util.Date;
import java.util.List;

/**
 * Created by chenshuai on 2017/4/14.
 */

public class ServiceListAdapter extends BaseQuickAdapter<CpigeonServicesInfo, BaseViewHolder> {
    public ServiceListAdapter() {
        super(R.layout.item_service_info, null);
    }

    public ServiceListAdapter(List<CpigeonServicesInfo> data) {
        super(R.layout.item_service_info, data);
    }

    Date openTime, closeTime;
    long openTimeSpan, closeTimeSpan;
    String timeStr;

    @Override
    protected void convert(BaseViewHolder helper, CpigeonServicesInfo item) {
        List<Integer> userSIDS = CpigeonData.getInstance().getUserServicesIds();
        boolean currIsUsed = userSIDS.contains(item.getId());
        helper.setText(R.id.tv_service_item_name, item.getPackageName());
        helper.setText(R.id.tv_service_item_price, String.format("%.2f元/%d鸽币", item.getPrice(), item.getScores()));
        helper.setText(R.id.tv_service_item_explain, item.getBrief());
        helper.setVisible(R.id.tv_service_item_prompt, currIsUsed);
        helper.setVisible(R.id.iv_right, !currIsUsed);
        try {
            openTime = DateTool.strToDateTime(item.getOpentime());
            closeTime = DateTool.strToDateTime(item.getExpiretime());
        } catch (Exception ex) {
            ex.printStackTrace();
            openTime = new Date();
            closeTime = new Date();
        }
        openTimeSpan = System.currentTimeMillis() - openTime.getTime();
        closeTimeSpan = System.currentTimeMillis() - closeTime.getTime();

        if (openTimeSpan < 0) {
            //服务未上架
            timeStr = String.format("上架时间：%s", DateTool.dateToStr(openTime));
        } else if (closeTimeSpan > 0) {
            //服务已下架
            timeStr = String.format("下架时间：%s", DateTool.dateToStr(closeTime));
        } else {
            //服务已上架
            timeStr = "正在销售";
            if (openTimeSpan < 1000l * 60 * 60 * 24 * 3) {
                //服务上架时间在3天之内
                timeStr = String.format("上架时间：%s", DateTool.dateToStr(openTime));
            }
            if (closeTimeSpan >= -1000l * 60 * 60 * 24 * 30) {
                //服务将在30天内下架
                timeStr = String.format("下架时间：%s", DateTool.dateToStr(closeTime));
            }
        }
        helper.setText(R.id.tv_service_item_time, timeStr);
//        helper.setVisible(R.id.tv_service_item_time, !"正在销售".equals(timeStr));
    }
}
