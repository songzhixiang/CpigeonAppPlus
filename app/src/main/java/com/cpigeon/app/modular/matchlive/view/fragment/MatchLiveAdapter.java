package com.cpigeon.app.modular.matchlive.view.fragment;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.app.R;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/4/7.
 */

public class MatchLiveAdapter extends BaseQuickAdapter<MatchInfo, BaseViewHolder> {


    public MatchLiveAdapter(List<MatchInfo> data) {
        super(R.layout.listitem_race_info, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MatchInfo item) {
        String bsmc = item.computerBSMC().trim();
        helper.setText(R.id.race_info_raceName, bsmc);
        if ("bs".equals(item.getDt())) {
            helper.setText(R.id.race_info_race_right_text, item.compuberGcys(true));
            helper.setText(R.id.race_info_raceOrg, item.getMc());
            if ("gp".equals(item.getLx()) && !item.isMatch()) {
                helper.getView(R.id.race_info_raceType).setVisibility(View.VISIBLE);
                helper.setText(R.id.race_info_raceType, "训");
                helper.getView(R.id.race_info_raceType).setBackgroundResource(R.drawable.background_text_view_matchtype_x);
            } else if ("gp".equals(item.getLx()) && item.isMatch()) {
                helper.getView(R.id.race_info_raceType).setVisibility(View.VISIBLE);
                helper.setText(R.id.race_info_raceType, "赛");
                helper.getView(R.id.race_info_raceType).setBackgroundResource(R.drawable.background_text_view_matchtype_s);
            } else {
                helper.getView(R.id.race_info_raceType).setVisibility(View.GONE);
            }
            helper.setTextColor(R.id.race_info_race_right_text, mContext.getResources().getColor(R.color.light_red));
            helper.getView(R.id.race_info_race_right_image_expand).setVisibility(View.VISIBLE);
            helper.setTextColor(R.id.race_info_raceName, mContext.getResources().getColor(R.color.textColor_gray));
        } else {
            //集鸽数据
//            helper.getView(R.id.race_info_race_right_image_expand).setVisibility(View.GONE);
            helper.setText(R.id.race_info_race_right_text, item.compuberSLYS());
            helper.setTextColor(R.id.race_info_race_right_text, mContext.getResources().getColor(R.color.light_green));
            helper.setTextColor(R.id.race_info_raceName, mContext.getResources().getColor(R.color.light_green));
            helper.setText(R.id.race_info_raceOrg, item.getMc());
            if ("gp".equals(item.getLx())) {
                helper.getView(R.id.race_info_raceType).setVisibility(View.VISIBLE);
                helper.setText(R.id.race_info_raceType, "集");
                helper.getView(R.id.race_info_raceType).setBackgroundResource(R.drawable.background_text_view_matchtype_long);
            } else {
                helper.getView(R.id.race_info_raceType).setVisibility(View.GONE);

            }

        }
//        helper.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int pos = helper.getAdapterPosition();
//                expand(pos);
//            }
//        });
//
//        if (!TextUtils.isEmpty(item.getArea())) {
//            helper.getView(R.id.tv_sifangdidian).setVisibility(View.VISIBLE);
//            helper.setText(R.id.tv_sifangdidian, item.getArea());
//
//        } else {
//            helper.getView(R.id.tv_sifangdidian).setVisibility(View.GONE);
//        }
//        if (!TextUtils.isEmpty(item.getSt())) {
//            helper.getView(R.id.tv_sifangshijian).setVisibility(View.VISIBLE);
//            helper.setText(R.id.tv_sifangshijian, item.getSt());
//        } else {
//            helper.getView(R.id.tv_sifangshijian).setVisibility(View.GONE);
//        }
//
//        helper.setText(R.id.tv_cankaokongju, item.getBskj() + "KM");
//
//        if (!TextUtils.isEmpty(item.getTq())) {
//            helper.getView(R.id.tv_weather).setVisibility(View.VISIBLE);
//            helper.setText(R.id.tv_weather, item.getTq());
//        } else {
//            helper.getView(R.id.tv_weather).setVisibility(View.GONE);
//        }
//        if (!TextUtils.isEmpty(item.computerSFZB())) {
//            helper.getView(R.id.tv_sifangzuobiao).setVisibility(View.VISIBLE);
//            helper.setText(R.id.tv_sifangzuobiao, item.computerSFZB());
//
//        } else {
//            helper.getView(R.id.tv_sifangzuobiao).setVisibility(View.GONE);
//        }
//        if (!TextUtils.isEmpty(item.compuberSLYS())) {
//            helper.getView(R.id.tv_sifangyushu).setVisibility(View.VISIBLE);
//            helper.setText(R.id.tv_sifangyushu, item.compuberSLYS());
//        } else {
//            helper.getView(R.id.tv_sifangyushu).setVisibility(View.GONE);
//        }
    }


}
