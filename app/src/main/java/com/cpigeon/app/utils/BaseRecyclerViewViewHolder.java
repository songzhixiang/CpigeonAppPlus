package com.cpigeon.app.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.xutils.x;

/**
 * Created by Administrator on 2016/12/27.
 */

public class BaseRecyclerViewViewHolder<E> extends RecyclerView.ViewHolder {

    private E currData;

    public BaseRecyclerViewViewHolder(View itemView) {
        super(itemView);
        x.view().inject(this, itemView);
    }

    public E getCurrData() {
        return currData;
    }

    public void bindData(E data,int position) {
        currData = data;
    }
}
