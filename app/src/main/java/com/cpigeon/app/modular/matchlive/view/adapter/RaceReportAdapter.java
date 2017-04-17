package com.cpigeon.app.modular.matchlive.view.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
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
    int mc;
    String name;
    String footNumber;
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
                helper.getView(R.id.report_info_item_mc_img).setVisibility(View.GONE);
                helper.setVisible(R.id.report_info_item_mc,true);
                if ("xh".equals(mathType)) {
                    MatchTitleXHItem titleItem = (MatchTitleXHItem) item;
                    mc = titleItem.getMatchReportXH().getMc();
                    name = titleItem.getMatchReportXH().getName();
                    footNumber = EncryptionTool.decryptAES(titleItem.getMatchReportXH().getFoot());

                } else {
                    MatchTitleGPItem titleItem = (MatchTitleGPItem) item;
                    mc = titleItem.getMatchReportGP().getMc();
                    name = titleItem.getMatchReportGP().getName();
                    footNumber = EncryptionTool.decryptAES(titleItem.getMatchReportGP().getFoot());
                }
                switch (mc)
                {
                    case 1:
                        helper.setVisible(R.id.report_info_item_mc,false);
                        helper.getView(R.id.report_info_item_mc_img).setVisibility(View.VISIBLE);
                        helper.setImageResource(R.id.report_info_item_mc_img, R.drawable.svg_ic_order_frist);
                        break;
                    case 2:
                        helper.setVisible(R.id.report_info_item_mc,false);
                        helper.getView(R.id.report_info_item_mc_img).setVisibility(View.VISIBLE);
                        helper.setImageResource(R.id.report_info_item_mc_img, R.drawable.svg_ic_order_second);
                        break;
                    case 3:
                        helper.setVisible(R.id.report_info_item_mc,false);
                        helper.getView(R.id.report_info_item_mc_img).setVisibility(View.VISIBLE);
                        helper.setImageResource(R.id.report_info_item_mc_img, R.drawable.svg_ic_order_thrid);
                        break;
                    default:

                        helper.setText(R.id.report_info_item_mc,mc+"");
                        break;
                }
                helper.setText(R.id.report_info_item_xm, name);
                helper.setText(R.id.report_info_item_hh, footNumber);
                break;
            case TYPE_DETIAL:
                final MatchDetialXHItem detialItem = (MatchDetialXHItem) item;
                helper.setText(R.id.tv_sifangdidian, "会员棚号:" + detialItem.getSubItem(0).getPn());
                helper.setText(R.id.tv_sifangdidian, "比赛空距:" +detialItem.getSubItem(0).getSp()+"KM");
                helper.setText(R.id.tv_sifangdidian, "赛鸽分速:" + detialItem.getSubItem(0).getSpeed()+"M");
                helper.setText(R.id.tv_sifangdidian, "归巢时间:" + detialItem.getSubItem(0).getArrive());
                helper.setText(R.id.tv_sifangdidian, "登记坐标:" + detialItem.getSubItem(0).getZx()+"/"+detialItem.getSubItem(0).getZy());
                helper.setText(R.id.tv_sifangdidian, "扫描坐标:" + detialItem.getSubItem(0).getDczx()+"/"+detialItem.getSubItem(0).getDczy());
                helper.setText(R.id.tv_sifangdidian, "插组报道:" + detialItem.getSubItem(0).CZtoString());
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
