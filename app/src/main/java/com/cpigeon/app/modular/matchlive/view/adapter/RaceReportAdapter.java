package com.cpigeon.app.modular.matchlive.view.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.cpigeon.app.R;
import com.cpigeon.app.modular.matchlive.model.bean.MatchReportXH;

import java.util.ArrayList;
import java.util.List;

import static com.cpigeon.app.modular.matchlive.view.adapter.MatchLiveExpandAdapter.TYPE_DETIAL;
import static com.cpigeon.app.modular.matchlive.view.adapter.MatchLiveExpandAdapter.TYPE_TITLE;

/**
 * Created by Administrator on 2017/4/17.
 */

public class RaceReportAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity,BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public RaceReportAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_TITLE, R.layout.listitem_report_info);
        addItemType(TYPE_DETIAL, R.layout.listitem_report_info_expand);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()){
            case TYPE_TITLE:
                final MatchTitleXHItem titleItem = (MatchTitleXHItem) item;
                helper.setText(R.id.report_info_item_mc,titleItem.getMatchReportXH().getMc());
                helper.setText(R.id.report_info_item_xm,titleItem.getMatchReportXH().getName());
                break;
            case TYPE_DETIAL:

                break;
        }
    }

    public static List<MultiItemEntity> getXH(List<MatchReportXH> data) {
        List<MultiItemEntity> result = new ArrayList<>();
        if (data == null)
            return result;
        MatchTitleXHItem titleItem;
        MatchDetialXHItem detialItem;
        for (MatchReportXH matchInfo : data) {
            titleItem = new MatchTitleXHItem(matchInfo);

            detialItem = new MatchDetialXHItem();
            detialItem.addSubItem(matchInfo);

            titleItem.addSubItem(detialItem);
            result.add(titleItem);
        }
        return result;
    }

    public static class MatchTitleXHItem extends AbstractExpandableItem<MatchDetialXHItem> implements MultiItemEntity {

        MatchReportXH matchReportXH;

        public MatchTitleXHItem(MatchReportXH matchReportXH) {
            this.matchReportXH = matchReportXH;
        }

        public MatchReportXH getMatchReportXH() {
            return matchReportXH;
        }

        @Override
        public int getLevel() {
            return 0;
        }

        @Override
        public int getItemType() {
            return TYPE_TITLE;
        }
    }

    public static class MatchDetialXHItem extends AbstractExpandableItem<MatchReportXH> implements MultiItemEntity {

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
