package com.cpigeon.app.modular.matchlive.view.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.cpigeon.app.R;
import com.cpigeon.app.modular.matchlive.model.bean.MatchReportGP;
import com.cpigeon.app.modular.matchlive.model.bean.MatchReportXH;
import com.cpigeon.app.utils.EncryptionTool;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.cpigeon.app.modular.matchlive.view.adapter.MatchLiveExpandAdapter.TYPE_DETIAL;
import static com.cpigeon.app.modular.matchlive.view.adapter.MatchLiveExpandAdapter.TYPE_TITLE;

/**
 * Created by Administrator on 2017/4/17.
 */

public class RaceReportAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    String mathType;

    public RaceReportAdapter(String mathType) {
        super(null);
        this.mathType = mathType;
        addItemType(TYPE_TITLE, R.layout.listitem_report_info);
        addItemType(TYPE_DETIAL, R.layout.listitem_report_info_expand);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        Logger.d(item);
        switch (helper.getItemViewType()) {
            case TYPE_TITLE:
                if ("xh".equals(mathType)) {
                    MatchTitleXHItem titleItem = (MatchTitleXHItem) item;
                    helper.setText(R.id.report_info_item_mc, titleItem.getMatchReportXH().getMc() + "");
                    helper.setText(R.id.report_info_item_xm, titleItem.getMatchReportXH().getName());
                    helper.setText(R.id.report_info_item_hh, EncryptionTool.decryptAES(titleItem.getMatchReportXH().getFoot()));
                } else {
                    MatchTitleGPItem titleItem = (MatchTitleGPItem) item;
                    helper.setText(R.id.report_info_item_mc, titleItem.getMatchReportGP().getMc() + "");
                    helper.setText(R.id.report_info_item_xm, titleItem.getMatchReportGP().getName());
                    helper.setText(R.id.report_info_item_hh, EncryptionTool.decryptAES(titleItem.getMatchReportGP().getFoot()));
                }
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

    public static List<MultiItemEntity> getGP(List<MatchReportGP> data) {
        List<MultiItemEntity> result = new ArrayList<>();
        if (data == null)
            return result;
        MatchTitleGPItem titleItem;
        MatchDetialGPItem detialItem;
        for (MatchReportGP matchInfo : data) {
            titleItem = new MatchTitleGPItem(matchInfo);

            detialItem = new MatchDetialGPItem();
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


    public static class MatchTitleGPItem extends AbstractExpandableItem<MatchDetialGPItem> implements MultiItemEntity {

        MatchReportGP matchReportGP;

        public MatchTitleGPItem(MatchReportGP matchReport) {
            this.matchReportGP = matchReport;
        }

        public MatchReportGP getMatchReportGP() {
            return matchReportGP;
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

    public static class MatchDetialGPItem extends AbstractExpandableItem<MatchReportGP> implements MultiItemEntity {

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
