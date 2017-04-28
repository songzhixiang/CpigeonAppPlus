package com.cpigeon.app.modular.matchlive.view.adapter;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.cpigeon.app.R;
import com.cpigeon.app.modular.matchlive.model.bean.MatchPigeonsGP;
import com.cpigeon.app.modular.matchlive.model.bean.MatchPigeonsXH;
import com.cpigeon.app.utils.EncryptionTool;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.cpigeon.app.modular.matchlive.view.adapter.MatchLiveExpandAdapter.TYPE_DETIAL;
import static com.cpigeon.app.modular.matchlive.view.adapter.MatchLiveExpandAdapter.TYPE_TITLE;

/**
 * 插组指定Adapter(包含协会和公棚)
 * Created by Administrator on 2017/4/21.
 */

public class ChaZuZhiDingDetailsAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private String mathType;//加载的数据类型，xh-协会，gp-公棚
    private String name;//姓名
    private String footNumber;//足环

    public ChaZuZhiDingDetailsAdapter(String mathType) {
        super(null);
        this.mathType = mathType;
        addItemType(TYPE_TITLE, R.layout.listitem_report_info);
        if ("xh".equals(mathType)) {
            addItemType(TYPE_DETIAL, R.layout.listitem_chazuzhiding_xh_info_expand);
        } else if ("gp".equals(mathType)) {
            addItemType(TYPE_DETIAL, R.layout.listitem_chazuzhiding_gp_info_expand);
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        int xuhao = helper.getLayoutPosition() + 1;//序号
        switch (helper.getItemViewType()) {
            case TYPE_TITLE:
                helper.setVisible(R.id.report_info_item_mc_img, false);
                helper.setVisible(R.id.report_info_item_mc, true);
                helper.setVisible(R.id.report_info_item_rank, false);
                if ("xh".equals(mathType)) {
                    ChaZuZhiDingDetailsAdapter.MatchTitleXHItem titleItem = (ChaZuZhiDingDetailsAdapter.MatchTitleXHItem) item;
                    name = titleItem.getMatchPigeonsXH().getName();
                    footNumber = EncryptionTool.decryptAES(titleItem.getMatchPigeonsXH().getFoot());
                    xuhao = titleItem.getMatchPigeonsXH().getOrder();
                } else {
                    ChaZuZhiDingDetailsAdapter.MatchTitleGPItem titleItem = (ChaZuZhiDingDetailsAdapter.MatchTitleGPItem) item;
                    name = titleItem.getMatchPigeonsGP().getName();
                    footNumber = EncryptionTool.decryptAES(titleItem.getMatchPigeonsGP().getFoot());
                    xuhao = titleItem.getMatchPigeonsGP().getOrder();
                }

                helper.setText(R.id.report_info_item_mc, xuhao + "");
                helper.setText(R.id.report_info_item_xm, name);
                helper.setText(R.id.report_info_item_hh, footNumber);
                break;
            case TYPE_DETIAL:
                if ("xh".equals(mathType)) {
                    final ChaZuZhiDingDetailsAdapter.MatchDetialXHItem detialItem = (ChaZuZhiDingDetailsAdapter.MatchDetialXHItem) item;
                    helper.setText(R.id.tv_huiyuanpenghao, "会员棚号:" + detialItem.getSubItem(0).getPn() + "");
                    helper.setText(R.id.tv_shanglongshijian, "上笼时间:" + detialItem.getSubItem(0).getJgtime() + "");
                    helper.setText(R.id.tv_dengjizuobiao, "登记坐标:" + detialItem.getSubItem(0).getZx() + "/" + detialItem.getSubItem(0).getZy());
                    helper.setText(R.id.tv_chazuzhiding, TextUtils.isEmpty(detialItem.getSubItem(0).CZtoString()) ? "插组指定:无" : "插组指定 :" + detialItem.getSubItem(0).CZtoString());
                } else if ("gp".equals(mathType)) {
                    final ChaZuZhiDingDetailsAdapter.MatchDetialGPItem detialItem = (ChaZuZhiDingDetailsAdapter.MatchDetialGPItem) item;
                    helper.setText(R.id.tv_saigecolor, "赛鸽羽色:" + detialItem.getSubItem(0).getColor() + "");
                    helper.setText(R.id.tv_suoshuarea, "所属地区:" + detialItem.getSubItem(0).getArea());
                    helper.setText(R.id.tv_dianzihuanhao, "电子环号:" + detialItem.getSubItem(0).getRing());
                    helper.setText(R.id.tv_suoshutuandui, "所属团队:" + detialItem.getSubItem(0).getTtzb());
                    helper.setText(R.id.tv_shanglongshijian, "上笼时间:" + detialItem.getSubItem(0).getJgtime());
                    helper.setText(R.id.tv_shangchuanshijian, "上传时间:" + detialItem.getSubItem(0).getUptime());
                    helper.setText(R.id.tv_chazuzhiding, TextUtils.isEmpty(detialItem.getSubItem(0).CZtoString()) ? "插组报到:无" : "插组报到 :" + detialItem.getSubItem(0).CZtoString());
                }
                break;

        }
    }


    public static List<MultiItemEntity> getXH(List<MatchPigeonsXH> data) {
        List<MultiItemEntity> result = new ArrayList<>();
        if (data == null)
            return result;
        ChaZuZhiDingDetailsAdapter.MatchTitleXHItem titleItem;
        ChaZuZhiDingDetailsAdapter.MatchDetialXHItem detialItem;
        for (MatchPigeonsXH matchInfo : data) {
            titleItem = new ChaZuZhiDingDetailsAdapter.MatchTitleXHItem(matchInfo);

            detialItem = new ChaZuZhiDingDetailsAdapter.MatchDetialXHItem();
            detialItem.addSubItem(matchInfo);

            titleItem.addSubItem(detialItem);
            result.add(titleItem);
        }
        return result;
    }

    public static List<MultiItemEntity> getGP(List<MatchPigeonsGP> data) {
        List<MultiItemEntity> result = new ArrayList<>();
        if (data == null)
            return result;
        ChaZuZhiDingDetailsAdapter.MatchTitleGPItem titleItem;
        ChaZuZhiDingDetailsAdapter.MatchDetialGPItem detialItem;
        for (MatchPigeonsGP matchInfo : data) {
            titleItem = new ChaZuZhiDingDetailsAdapter.MatchTitleGPItem(matchInfo);

            detialItem = new ChaZuZhiDingDetailsAdapter.MatchDetialGPItem();
            detialItem.addSubItem(matchInfo);

            titleItem.addSubItem(detialItem);
            result.add(titleItem);
        }
        return result;
    }

    public static class MatchTitleXHItem extends AbstractExpandableItem<ChaZuZhiDingDetailsAdapter.MatchDetialXHItem> implements MultiItemEntity {

        MatchPigeonsXH matchPigeonsXH;

        public MatchTitleXHItem(MatchPigeonsXH matchPigeonsXH) {
            this.matchPigeonsXH = matchPigeonsXH;
        }

        public MatchPigeonsXH getMatchPigeonsXH() {
            return matchPigeonsXH;
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

    public static class MatchDetialXHItem extends AbstractExpandableItem<MatchPigeonsXH> implements MultiItemEntity {

        @Override
        public int getItemType() {
            return TYPE_DETIAL;
        }

        @Override
        public int getLevel() {
            return 1;
        }
    }

    public static class MatchTitleGPItem extends AbstractExpandableItem<ChaZuZhiDingDetailsAdapter.MatchDetialGPItem> implements MultiItemEntity {

        MatchPigeonsGP matchPigeonsGP;

        public MatchTitleGPItem(MatchPigeonsGP matchPigeonsGP) {
            this.matchPigeonsGP = matchPigeonsGP;
        }

        public MatchPigeonsGP getMatchPigeonsGP() {
            return matchPigeonsGP;
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

    public static class MatchDetialGPItem extends AbstractExpandableItem<MatchPigeonsGP> implements MultiItemEntity {

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
