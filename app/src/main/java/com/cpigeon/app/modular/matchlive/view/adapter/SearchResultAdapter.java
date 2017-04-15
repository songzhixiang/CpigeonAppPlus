package com.cpigeon.app.modular.matchlive.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.utils.BaseRecyclerViewAdapter;
import com.cpigeon.app.utils.BaseRecyclerViewViewHolder;

import org.xutils.view.annotation.ViewInject;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/14.
 */

public class SearchResultAdapter extends BaseRecyclerViewAdapter<Map<String, Object>> {
    public static final int VIEWTYPE_MATCHINFO = 1;
    public static final int VIEWTYPE_GROUP_TITLE = 2;

    public SearchResultAdapter(Context context) {
        super(context);
    }

    public SearchResultAdapter(Context context, List<Map<String, Object>> dataList) {
        super(context, dataList);
    }

    @Override
    public int getItemViewType(int position) {
        if (getStatus() != STATUS_DEFAULT) {
            return super.getItemViewType(position);
        }
        if (getItem(position).get("viewtype").equals(VIEWTYPE_GROUP_TITLE)) {
            return VIEWTYPE_GROUP_TITLE;
        } else if (getItem(position).get("viewtype").equals(VIEWTYPE_MATCHINFO)) {
            return VIEWTYPE_MATCHINFO;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder createHolder(ViewGroup parent, int viewType) {
        View v;
        BaseRecyclerViewViewHolder holder = null;
        if (viewType == VIEWTYPE_NODATA) {
            v = getLayoutInflater().inflate(R.layout.item_search_not_result, null);
            holder = new BaseRecyclerViewViewHolder(v);
        } else if (viewType == VIEWTYPE_MATCHINFO) {
            v = getLayoutInflater().inflate(R.layout.item_search_result_matchinfo, null);
            holder = new MatchInfoViewHolder(v);
        } else if (viewType == VIEWTYPE_GROUP_TITLE) {
            v = getLayoutInflater().inflate(R.layout.item_search_group_title, null);
            holder = new GroupTitleViewHolder(v);
        }
        return holder;
    }

    public class GroupTitleViewHolder extends BaseRecyclerViewViewHolder<Map<String, Object>> {
        @ViewInject(R.id.tv_group_title_name)
        TextView title_name;
        @ViewInject(R.id.tv_group_title_prompt)
        TextView title_prompt;

        public GroupTitleViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Map<String, Object> data,int pos) {
            super.bindData(data,pos);
            if (title_name != null && data.containsKey("name"))
                title_name.setText(data.get("name").toString());
            if (title_prompt != null && data.containsKey("prompt"))
                title_prompt.setText(data.get("prompt").toString());
        }
    }

    public class MatchInfoViewHolder extends BaseRecyclerViewViewHolder<Map<String, Object>> {
        @ViewInject(R.id.race_info_raceOrg)
        TextView raceOrg;
        @ViewInject(R.id.race_info_raceType)
        TextView raceType;
        @ViewInject(R.id.race_info_raceName)
        TextView raceName;
        @ViewInject(R.id.race_info_race_right_text)
        TextView right_text;


        public MatchInfoViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Map<String, Object> data,int pos) {
            super.bindData(data,pos);
            MatchInfo matchInfo = data.containsKey("data") ? (MatchInfo) data.get("data") : null;

            if (matchInfo == null) return;
            if (raceOrg != null)
                raceOrg.setText(matchInfo.getMc());

            if (raceName != null)
                raceName.setText(matchInfo.computerBSMC());
            if (right_text != null)
                right_text.setText(matchInfo.compuberGcys(true));

            if (raceType != null) {
                if ("xh".equals(matchInfo.getLx())) {
                    raceType.setVisibility(View.GONE);
                    return;
                }
                raceType.setVisibility(View.VISIBLE);
                raceType.setText("bs".equals(matchInfo.getDt()) ? matchInfo.isMatch() ? "赛" : "训" : "集");
                raceType.setBackgroundResource("bs".equals(matchInfo.getDt()) ? matchInfo.isMatch() ?
                        R.drawable.background_text_view_matchtype_s :
                        R.drawable.background_text_view_matchtype_x : R.drawable.background_text_view_matchtype_long);
            }
        }
    }
}