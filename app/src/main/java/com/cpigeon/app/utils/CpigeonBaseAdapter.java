package com.cpigeon.app.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/16.
 */


public abstract class CpigeonBaseAdapter<E> extends BaseAdapter {


    protected LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private List<E> dataList;
    private Context mContext;
    private boolean mIsNoData;
    private OnItemCountChangedListener onItemCountChangedListener;

    public CpigeonBaseAdapter(Context context) {
        this(context, null);
    }

    public CpigeonBaseAdapter(Context context, List<E> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        if (this.dataList == null) this.dataList = new ArrayList<>();
        mIsNoData = isAutoComputerIsNoData() && dataList.size() == 0;
    }

    public boolean isNoData() {
        return isAutoComputerIsNoData() && mIsNoData;
    }

    /**
     * 设置Item修改时的监听
     *
     * @param onItemCountChangedListener
     */
    public void setOnItemCountChangedListener(OnItemCountChangedListener onItemCountChangedListener) {
        this.onItemCountChangedListener = onItemCountChangedListener;
    }

    /**
     * 是否自动计算有无数据
     *
     * @return
     */
    public abstract boolean isAutoComputerIsNoData();

    /**
     * 设置数据列表
     *
     * @param dataList
     */
    public void setDataList(List<E> dataList) {
        int beforeCount = getCount();
        this.dataList = dataList;
        if (this.dataList == null) this.dataList = new ArrayList<>();
        mIsNoData = isAutoComputerIsNoData() && dataList.size() == 0;
        if (onItemCountChangedListener != null) {
            onItemCountChangedListener.onItemCountChanged(beforeCount, getCount());
        }
        this.notifyDataSetChanged();
    }

    public void removeItem(int postion) {
        int beforeCount = getCount();
        this.dataList.remove(postion);
        mIsNoData = isAutoComputerIsNoData() && dataList.size() == 0;
        if (onItemCountChangedListener != null) {
            onItemCountChangedListener.onItemCountChanged(beforeCount, getCount());
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList.size() + (isNoData() ? 1 : 0);
    }

    @Override
    public E getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 个数修改时触发
     */
    public interface OnItemCountChangedListener {
        void onItemCountChanged(int before, int after);
    }

    public interface ViewHolder<VHT> {
        void init(View view);

        void bindData(VHT data);
    }
}
