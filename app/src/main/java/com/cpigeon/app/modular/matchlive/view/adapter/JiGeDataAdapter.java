package com.cpigeon.app.modular.matchlive.view.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.cpigeon.app.R;
import com.cpigeon.app.modular.matchlive.model.bean.MatchPigeonsGP;
import com.cpigeon.app.modular.matchlive.model.bean.MatchPigeonsXH;
import com.cpigeon.app.utils.EncryptionTool;

import java.util.ArrayList;
import java.util.List;

import static com.cpigeon.app.modular.matchlive.view.adapter.MatchLiveExpandAdapter.TYPE_DETIAL;
import static com.cpigeon.app.modular.matchlive.view.adapter.MatchLiveExpandAdapter.TYPE_TITLE;

/**
 * Created by Administrator on 2017/4/19.
 */

public class JiGeDataAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private String matchType;
    private int mc;
    private String name;
    private String footNumber;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     */
    public JiGeDataAdapter(String matchType) {
        super(null);
        this.matchType = matchType;
        addItemType(TYPE_TITLE, R.layout.listitem_report_info);
        if ("xh".equals(matchType))
        {
            addItemType(TYPE_DETIAL, R.layout.listitem_jige_info_expand);
        }else if ("gp".equals(matchType))
        {
            addItemType(TYPE_DETIAL, R.layout.listitem_shanglong_info_expand);
        }



    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_TITLE:
                helper.getView(R.id.report_info_item_mc_img).setVisibility(View.GONE);
                helper.setVisible(R.id.report_info_item_mc, true);
                mc = getParentPosition(item) + 1;
                if ("xh".equals(matchType)) {
                    JiGeTitleItem_XH titleItem = (JiGeTitleItem_XH) item;

                    name = titleItem.getMatchPigeonsXH().getName();
                    footNumber = EncryptionTool.decryptAES(titleItem.getMatchPigeonsXH().getFoot());

                } else {
                    JiGeTitleItem_GP titleItem = (JiGeTitleItem_GP) item;
                    name = titleItem.getMatchPigeonsGP().getName();
                    footNumber = EncryptionTool.decryptAES(titleItem.getMatchPigeonsGP().getFoot());
                }
                helper.setText(R.id.report_info_item_mc,mc+"");
                helper.setText(R.id.report_info_item_xm, name);
                helper.setText(R.id.report_info_item_hh, footNumber);
                break;
            case TYPE_DETIAL:

                if ("xh".equals(matchType)) {

                    final JiGeDetialItem_XH detialItem = (JiGeDetialItem_XH) item;
                    helper.setText(R.id.tv_jige_huiyuanpenghao, "会员棚号:" + detialItem.getSubItem(0).getPn());
                    helper.setText(R.id.tv_jige_saigeshijian, "集鸽时间:" + detialItem.getSubItem(0).getJgtime());
                    helper.setText(R.id.tv_jige_dengjizuobiao, "登记坐标:" + detialItem.getSubItem(0).getZx() + "/" + detialItem.getSubItem(0).getZy());
                    helper.setText(R.id.tv_jige_chazubaodao, "插组指定:" + detialItem.getSubItem(0).CZtoString());
                    break;
                } else {
                    final JiGeDetialItem_GP detialItem = (JiGeDetialItem_GP) item;
                    helper.setText(R.id.tv_shanglong_saigeyuse, "赛鸽羽色:" + detialItem.getSubItem(0).getColor());
                    helper.setText(R.id.tv_shanglong_suoshuarea, "所属地区:" + detialItem.getSubItem(0).getArea());
                    helper.setText(R.id.tv_shanglong_shanglongshijian, "上笼时间:" + detialItem.getSubItem(0).getJgtime());
                    helper.setText(R.id.tv_shanglong_shangchuanshijian, "上传时间:" + detialItem.getSubItem(0).getUptime());
                    helper.setText(R.id.tv_shanglong_dianzihuanhao, "电子环号:" + detialItem.getSubItem(0).getRing());
                    helper.setText(R.id.tv_shanglong_suoshutuandui, "所属团队:" + detialItem.getSubItem(0).getTtzb());
                    helper.setText(R.id.tv_shanglong_chazuzhiding, "插组指定:" + detialItem.getSubItem(0).CZtoString());
                }


        }
    }

    public static List<MultiItemEntity> getXH(List<MatchPigeonsXH> data) {
        List<MultiItemEntity> result = new ArrayList<>();
        if (data == null)
            return result;
        JiGeTitleItem_XH titleItem;
        JiGeDetialItem_XH detialItem;
        for (MatchPigeonsXH matchInfo : data) {
            titleItem = new JiGeTitleItem_XH(matchInfo);

            detialItem = new JiGeDetialItem_XH();
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
        JiGeTitleItem_GP titleItem;
        JiGeDetialItem_GP detialItem;
        for (MatchPigeonsGP matchInfo : data) {
            titleItem = new JiGeTitleItem_GP(matchInfo);

            detialItem = new JiGeDetialItem_GP();
            detialItem.addSubItem(matchInfo);

            titleItem.addSubItem(detialItem);
            result.add(titleItem);
        }
        return result;
    }

    public static class JiGeTitleItem_XH extends AbstractExpandableItem<JiGeDetialItem_XH> implements MultiItemEntity {

        MatchPigeonsXH matchPigeonsXH;

        public JiGeTitleItem_XH(MatchPigeonsXH matchPigeonsXH) {
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

    public static class JiGeDetialItem_XH extends AbstractExpandableItem<MatchPigeonsXH> implements MultiItemEntity {

        @Override
        public int getItemType() {
            return TYPE_DETIAL;
        }

        @Override
        public int getLevel() {
            return 1;
        }
    }


    public static class JiGeTitleItem_GP extends AbstractExpandableItem<JiGeDetialItem_GP> implements MultiItemEntity {

        MatchPigeonsGP matchPigeonsGP;

        public JiGeTitleItem_GP(MatchPigeonsGP matchPigeonsGP) {
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

    public static class JiGeDetialItem_GP extends AbstractExpandableItem<MatchPigeonsGP> implements MultiItemEntity {

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
