package com.cpigeon.app.modular.matchlive.view.adapter;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.app.R;
import com.cpigeon.app.modular.matchlive.model.bean.MatchReportXH;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/4/17.
 */

public class ChaZuAdapter extends BaseQuickAdapter<HashMap<String, Object>,BaseViewHolder>{
    private int loadType;//设置加载数据的类型，0-插组报到；1-插组指定
    public ChaZuAdapter(List<HashMap<String, Object>> data,int loadType) {
        super(R.layout.listitem_pigeons_groups,data);
        this.loadType = loadType;
    }

    @Override
    protected void convert(BaseViewHolder helper, HashMap<String, Object> item) {

        helper.setText(R.id.pigeons_groups_group_name,String.valueOf(item.get("group")) + "组");
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
        SpannableStringBuilder builder = new SpannableStringBuilder("参赛" + String.valueOf(item.get("sfys")) + "羽");
        builder.setSpan(redSpan, 2, builder.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        helper.setText(R.id.pigeons_groups_group_join_count,builder);
        switch (loadType)
        {
            case 0://插组报到

                helper.setVisible(R.id.pigeons_groups_group_homing_count,true);
                builder = new SpannableStringBuilder("归巢" + String.valueOf(item.get("gcys")) + "羽");
                builder.setSpan(redSpan, 2, builder.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.pigeons_groups_group_homing_count,builder);
                break;
            case 1://插组指定
                helper.setVisible(R.id.pigeons_groups_group_homing_count,false);
                break;
        }
    }
}
