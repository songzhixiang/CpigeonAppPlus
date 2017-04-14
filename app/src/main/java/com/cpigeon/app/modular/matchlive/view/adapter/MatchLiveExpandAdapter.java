package com.cpigeon.app.modular.matchlive.view.adapter;

import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.cpigeon.app.R;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenshuai on 2017/4/14.
 */

public class MatchLiveExpandAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    public static final int TYPE_TITLE = 1;
    public static final int TYPE_DETIAL = 2;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MatchLiveExpandAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_TITLE, R.layout.listitem_race_info);
        addItemType(TYPE_DETIAL, R.layout.listitem_race_info_details);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_TITLE:
                final MatchTitleItem titleItem = (MatchTitleItem) item;
                String bsmc = titleItem.matchInfo.computerBSMC().trim();
                helper.setText(R.id.race_info_raceName, bsmc);
                helper.getView(R.id.race_info_race_right_image_expand).setRotation(titleItem.isExpanded() ? 180 : 0);
                if ("bs".equals(titleItem.matchInfo.getDt())) {
                    helper.getView(R.id.race_info_race_right_image_expand).setVisibility(View.GONE);
                    helper.setText(R.id.race_info_race_right_text, titleItem.matchInfo.compuberGcys(true));
                    helper.setText(R.id.race_info_raceOrg, titleItem.matchInfo.getMc());
                    if ("gp".equals(titleItem.matchInfo.getLx()) && !titleItem.matchInfo.isMatch()) {
                        helper.getView(R.id.race_info_raceType).setVisibility(View.VISIBLE);
                        helper.setText(R.id.race_info_raceType, "训");
                        helper.getView(R.id.race_info_raceType).setBackgroundResource(R.drawable.background_text_view_matchtype_x);
                    } else if ("gp".equals(titleItem.matchInfo.getLx()) && titleItem.matchInfo.isMatch()) {
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
                    helper.getView(R.id.race_info_race_right_image_expand).setVisibility(View.GONE);
                    helper.setText(R.id.race_info_race_right_text, titleItem.matchInfo.compuberSLYS());
                    helper.setTextColor(R.id.race_info_race_right_text, mContext.getResources().getColor(R.color.light_green));
                    helper.setTextColor(R.id.race_info_raceName, mContext.getResources().getColor(R.color.light_green));
                    helper.setText(R.id.race_info_raceOrg, titleItem.matchInfo.getMc());
                    if ("gp".equals(titleItem.matchInfo.getLx())) {
                        helper.getView(R.id.race_info_raceType).setVisibility(View.VISIBLE);
                        helper.setText(R.id.race_info_raceType, "集");
                        helper.getView(R.id.race_info_raceType).setBackgroundResource(R.drawable.background_text_view_matchtype_long);
                    } else {
                        helper.getView(R.id.race_info_raceType).setVisibility(View.GONE);
                    }
                }
                break;
            case TYPE_DETIAL:
                final MatchDetialItem detialItem = (MatchDetialItem) item;
                helper.setText(R.id.tv_sifangdidian, "司放地点:" + detialItem.getSubItem(0).getArea());
                helper.setText(R.id.tv_cankaokongju, "参考空距:" + detialItem.getSubItem(0).getBskj());
                break;
        }
    }

    public static List<MultiItemEntity> get(List<MatchInfo> data) {
        List<MultiItemEntity> result = new ArrayList<>();
        if (data == null)
            return result;
        MatchTitleItem titleItem;
        MatchDetialItem detialItem;
        for (MatchInfo matchInfo : data) {
            titleItem = new MatchTitleItem(matchInfo);

            detialItem = new MatchDetialItem();
            detialItem.addSubItem(matchInfo);

            titleItem.addSubItem(detialItem);
            result.add(titleItem);
        }
        return result;
    }

    public static class MatchTitleItem extends AbstractExpandableItem<MatchDetialItem> implements MultiItemEntity {

        MatchInfo matchInfo;

        public MatchTitleItem(MatchInfo matchInfo) {
            this.matchInfo = matchInfo;
        }

        @Override
        public int getItemType() {
            return TYPE_TITLE;
        }

        @Override
        public int getLevel() {
            return 0;
        }
    }

    public static class MatchDetialItem extends AbstractExpandableItem<MatchInfo> implements MultiItemEntity {

        @Override
        public int getItemType() {
            return TYPE_DETIAL;
        }

        @Override
        public int getLevel() {
            return 1;
        }
    }
}
