package com.cpigeon.app.modular.home.view.fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.app.R;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by Administrator on 2017/4/12.
 */

public class HomeAdapter extends BaseQuickAdapter<MatchInfo , BaseViewHolder> {
    private int loadType;
    private int size;

    public HomeAdapter(List<MatchInfo> data, int loadType) {
        super(R.layout.layout_item_home_raceinfo, data);
        this.loadType = loadType;
        this.size = data.size();
    }

    @Override
    protected void convert(BaseViewHolder helper, MatchInfo item) {
        if (loadType == 1) {
            helper.setText(R.id.tv_raceinfo_type, "协会直播");
            helper.setTextColor(R.id.tv_raceinfo_type, mContext.getResources().getColor(R.color.colorPrimary));
        }else if (loadType == 0)
        {
            helper.setText(R.id.tv_raceinfo_type, "公棚直播");
            helper.setTextColor(R.id.tv_raceinfo_type, mContext.getResources().getColor(R.color.light_red2));
        }
        if (item == null ) {
            helper.setText(R.id.tv_raceinfo_count,"今日休战，暂无比赛");
            helper.getView(R.id.layout_raceinfo_list).setVisibility(View.GONE);
            return;
        } else {
            helper.getView(R.id.layout_raceinfo_list).setVisibility(View.VISIBLE);
        }
        helper.setText(R.id.tv_raceinfo_count, String.format("正在直播%d场", size));
        if (helper.getView(R.id.layout_raceinfo_list) == null) {
            Logger.w("无法添加到layout_raceinfo_list");
            return;
        }
        ((LinearLayout)helper.getView(R.id.layout_raceinfo_list)).removeAllViews();
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.layout_item_home_raceinfo_item, null);
        if ("xh".equals(item.getLx())) {

            ((TextView) v.findViewById(R.id.tv_raceinfo_count)).setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        } else {
            ((TextView) v.findViewById(R.id.tv_raceinfo_count)).setTextColor(mContext.getResources().getColor(R.color.light_red3));

        }
        ((TextView) v.findViewById(R.id.tv_raceinfo_title)).setText(item.getMc());
        helper.setText(R.id.tv_raceinfo_count,item.compuberGcys(true));
        ((LinearLayout)helper.getView(R.id.layout_raceinfo_list)).addView(v);

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
