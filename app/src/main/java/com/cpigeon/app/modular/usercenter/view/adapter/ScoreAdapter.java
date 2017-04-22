package com.cpigeon.app.modular.usercenter.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cpigeon.app.R;
import com.cpigeon.app.modular.usercenter.model.bean.UserScore;
import com.cpigeon.app.utils.DateTool;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by chenshuai on 2017/4/13.
 */

public class ScoreAdapter extends BaseQuickAdapter<UserScore, BaseViewHolder> {

    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy.MM.dd");

    public ScoreAdapter(List<UserScore> data) {
        super(R.layout.listitem_score_record, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserScore item) {
        if (item == null) {
            return;
        }
        helper.setText(R.id.tv_item_name, item.getItem().replace("积分","鸽币"));
        helper.setText(R.id.tv_item_time, dateTimeFormat.format(DateTool.strToDate(item.getTime())));
        String str = String.format(item.getScore() > 0 ? "获得鸽币：%d" : "消费鸽币：%d", Math.abs(item.getScore()));
        helper.setText(R.id.tv_item_explain, str);
    }
}
