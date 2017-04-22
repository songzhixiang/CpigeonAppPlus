package com.cpigeon.app.modular.matchlive.view.adapter;

import android.text.TextUtils;
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
 * 插组报道的详情数据（包含xh和gp）
 * Created by Administrator on 2017/4/19.
 */

public class ChaZuBaoDaoDetailsAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private String mathType;//加载的数据类型，xh-协会，gp-公棚
    private int ranking;
    private String name;
    private String footNumber;
    public ChaZuBaoDaoDetailsAdapter(String mathType) {
        super(null);
        this.mathType = mathType;
        addItemType(TYPE_TITLE, R.layout.listitem_report_info);
        if ("xh".equals(mathType))
        {
            addItemType(TYPE_DETIAL, R.layout.listitem_chazubaodao_xh_info_expand);
        }else if ("gp".equals(mathType))
        {
            addItemType(TYPE_DETIAL, R.layout.listitem_chazubaodao_gp_info_expand);
        }

    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {

        int xuhao = helper.getLayoutPosition() + 1;
        switch (helper.getItemViewType()) {
            case TYPE_TITLE:
                helper.setVisible(R.id.report_info_item_mc_img, false);
                helper.setVisible(R.id.report_info_item_mc, true);
                helper.setVisible(R.id.report_info_item_rank, true);
                if ("xh".equals(mathType)) {
                    ChaZuBaoDaoDetailsAdapter.MatchTitleXHItem titleItem = (ChaZuBaoDaoDetailsAdapter.MatchTitleXHItem) item;
                    ranking = titleItem.getMatchReportXH().getMc();
                    name = titleItem.getMatchReportXH().getName();
                    footNumber = EncryptionTool.decryptAES(titleItem.getMatchReportXH().getFoot());

                } else {
                    ChaZuBaoDaoDetailsAdapter.MatchTitleGPItem titleItem = (ChaZuBaoDaoDetailsAdapter.MatchTitleGPItem) item;
                    ranking = titleItem.getMatchReportGP().getMc();
                    name = titleItem.getMatchReportGP().getName();
                    footNumber = EncryptionTool.decryptAES(titleItem.getMatchReportGP().getFoot());
                }
                switch (xuhao) {
                    case 1:
                        helper.setVisible(R.id.report_info_item_mc, false);
                        helper.getView(R.id.report_info_item_mc_img).setVisibility(View.VISIBLE);
                        helper.setImageResource(R.id.report_info_item_mc_img, R.drawable.svg_ic_order_frist);
                        break;
                    case 2:
                        helper.setVisible(R.id.report_info_item_mc, false);
                        helper.getView(R.id.report_info_item_mc_img).setVisibility(View.VISIBLE);
                        helper.setImageResource(R.id.report_info_item_mc_img, R.drawable.svg_ic_order_second);
                        break;
                    case 3:
                        helper.setVisible(R.id.report_info_item_mc, false);
                        helper.getView(R.id.report_info_item_mc_img).setVisibility(View.VISIBLE);
                        helper.setImageResource(R.id.report_info_item_mc_img, R.drawable.svg_ic_order_thrid);
                        break;
                    default:
                        helper.setText(R.id.report_info_item_mc, xuhao + "");
                        break;
                }
                helper.setText(R.id.report_info_item_rank, ranking + "");
                Logger.e("Ranking "+ ranking);
                helper.setText(R.id.report_info_item_xm, name);
                helper.setText(R.id.report_info_item_hh, footNumber);
                break;
            case TYPE_DETIAL:
                if ("xh".equals(mathType))
                {
                    final ChaZuBaoDaoDetailsAdapter.MatchDetialXHItem detialItem = (ChaZuBaoDaoDetailsAdapter.MatchDetialXHItem) item;
                    helper.setText(R.id.tv_kongju, "空距:" + detialItem.getSubItem(0).getSp() + "KM");
                    helper.setText(R.id.tv_huiyuanpenghao, "会员棚号:" + detialItem.getSubItem(0).getPn()+"");
                    helper.setText(R.id.tv_saigefenshu, "赛鸽分速:" + detialItem.getSubItem(0).getSpeed() + "M");
                    helper.setText(R.id.tv_guichaoshijian, "归巢时间:" + detialItem.getSubItem(0).getArrive());
                    helper.setText(R.id.tv_dengjizuobiao, "登记坐标:" + detialItem.getSubItem(0).getZx() + "/" + detialItem.getSubItem(0).getZy());
                    helper.setText(R.id.tv_saomiaozuobiao, "扫描坐标:" + detialItem.getSubItem(0).getDczx() + "/" + detialItem.getSubItem(0).getDczy());
                    helper.setText(R.id.tv_chazubaodao, TextUtils.isEmpty( detialItem.getSubItem(0).CZtoString())?"插组报道:无":"插组报道 :"+detialItem.getSubItem(0).CZtoString());
                }else if ("gp".equals(mathType)){
                    final ChaZuBaoDaoDetailsAdapter.MatchDetialGPItem detialItem = (ChaZuBaoDaoDetailsAdapter.MatchDetialGPItem) item;
                    helper.setText(R.id.tv_saigecolor, "赛鸽羽色:" + detialItem.getSubItem(0).getColor()+"");
                    helper.setText(R.id.tv_saigefenshu, "赛鸽分速:" + detialItem.getSubItem(0).getSpeed() + "M");
                    helper.setText(R.id.tv_guichaoshijian, "归巢时间:" + detialItem.getSubItem(0).getArrive());
                    helper.setText(R.id.tv_suoshuarea, "所属地区:" + detialItem.getSubItem(0).getArea());
                    helper.setText(R.id.tv_dianzihuanhao, "电子环号:" + detialItem.getSubItem(0).getRing());
                    helper.setText(R.id.tv_suoshutuandui, "所属团队:" + detialItem.getSubItem(0).getTtzb());
                    helper.setText(R.id.tv_chazubaodao, TextUtils.isEmpty( detialItem.getSubItem(0).CZtoString())?"插组报道:无":"插组报道 :"+detialItem.getSubItem(0).CZtoString());
                }
                break;

        }
    }


    public static List<MultiItemEntity> getXH(List<MatchReportXH> data) {
        List<MultiItemEntity> result = new ArrayList<>();
        if (data == null)
            return result;
        ChaZuBaoDaoDetailsAdapter.MatchTitleXHItem titleItem;
        ChaZuBaoDaoDetailsAdapter.MatchDetialXHItem detialItem;
        for (MatchReportXH matchInfo : data) {
            titleItem = new ChaZuBaoDaoDetailsAdapter.MatchTitleXHItem(matchInfo);

            detialItem = new ChaZuBaoDaoDetailsAdapter.MatchDetialXHItem();
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
        ChaZuBaoDaoDetailsAdapter.MatchTitleGPItem titleItem;
        ChaZuBaoDaoDetailsAdapter.MatchDetialGPItem detialItem;
        for (MatchReportGP matchInfo : data) {
            titleItem = new ChaZuBaoDaoDetailsAdapter.MatchTitleGPItem(matchInfo);

            detialItem = new ChaZuBaoDaoDetailsAdapter.MatchDetialGPItem();
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
