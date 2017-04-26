package com.cpigeon.app.modular.matchlive.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.modular.home.model.bean.SearchHistory;
import com.cpigeon.app.utils.CpigeonBaseAdapter;

import java.util.List;

/**
 *
 * Created by Administrator on 2017/4/14.
 */

public class HistroyAdapter extends CpigeonBaseAdapter<SearchHistory> {
    public HistroyAdapter(Context context) {
        super(context);
    }

    @Override
    public boolean isAutoComputerIsNoData() {
        return false;
    }

    public HistroyAdapter(Context context, List dataList) {
        super(context, dataList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HistoryViewHolder vh;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_search_history, null);
            vh = new HistoryViewHolder();
            vh.init(convertView);
        } else {
            vh = (HistoryViewHolder) convertView.getTag();
        }
        vh.bindData(getItem(position));
        return convertView;
    }

    class HistoryViewHolder implements CpigeonBaseAdapter.ViewHolder<SearchHistory> {

        TextView tv_search_key;

        @Override
        public void init(View view) {
            view.setTag(this);
            tv_search_key = (TextView) view.findViewById(R.id.tv_search_key);
        }

        @Override
        public void bindData(SearchHistory data) {
            tv_search_key.setText(data.getSearchKey());
        }
    }

}
