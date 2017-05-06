package com.cpigeon.app.modular.home.view.fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.app.MyApp;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.AppManager;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by Administrator on 2017/4/12.
 */

public class HomeAdapter extends BaseQuickAdapter<MatchInfo, BaseViewHolder> {
    private int loadType;


    public HomeAdapter(List<MatchInfo> data, int loadType) {
        super(R.layout.layout_item_home_raceinfo_item, data);
        this.loadType = loadType;
    }

    @Override
    protected void convert(BaseViewHolder helper, MatchInfo item) {
        if (loadType == 0) {
            if ("jg".equals(item.getDt()) || TextUtils.isEmpty(item.getMc())) {
                return;
            }
            if ("gp".equals(item.getLx())) {
                helper.setText(R.id.tv_raceinfo_title, item.getMc());
                helper.setText(R.id.tv_raceinfo_count, item.compuberGcys(true));
                helper.setTextColor(R.id.tv_raceinfo_count, mContext.getResources().getColor(R.color.light_red2));

            }
        } else if (loadType == 1) {
            if ("xh".equals(item.getLx())) {
                helper.setText(R.id.tv_raceinfo_title, item.getMc());
                helper.setText(R.id.tv_raceinfo_count, item.compuberGcys(true));
                helper.setTextColor(R.id.tv_raceinfo_count, mContext.getResources().getColor(R.color.colorPrimary));
            }
        }

    }

    @Override
    public int getItemCount() {
        return getData().size() > 3 ? 3 : super.getItemCount();
    }
}
