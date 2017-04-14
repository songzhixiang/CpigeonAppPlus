package com.cpigeon.app.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/30.
 */

public abstract class BaseRecyclerViewAdapter<E> extends RecyclerView.Adapter {

    public static final int VIEWTYPE_NODATA = -1;
    public static final int STATUS_DEFAULT = 1;
    public static final int STATUS_LOADING = 2;
    public static final int STATUS_HASMORE = 3;
    public static final int STATUS_LOADFAIL = 4;
    public static final int STATUS_NODATA = 5;
    private static final int VIEWTYPE_BOTTOM_LOADMORE = -2;
    private static final int VIEWTYPE_BOTTOM_LOADING = -3;
    private static final int VIEWTYPE_BOTTOM_LOADFAIL = -4;
    private final Context mContext;
    protected ViewGroup.LayoutParams layoutParams_MATCH_WIDTH = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private View promptViewLoadmore;
    private View promptViewLoadFail;
    private View promptViewLoading;
    private int mStatus = STATUS_DEFAULT;//标记数据状态
    private List<E> mDataList;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private OnDataChangedListener onDataChangedListener;
    private Map<Integer, RecyclerView.ViewHolder> viewHolderMap = new HashMap<>();

    public BaseRecyclerViewAdapter(Context context) {
        this(context, null);
    }

    public BaseRecyclerViewAdapter(Context context, List<E> dataList) {
        this.mDataList = dataList;
        this.mContext = context;
        getLayoutInflater();
        initDataList();
    }

    public Map<Integer, RecyclerView.ViewHolder> getViewHolderMap() {
        return viewHolderMap;
    }

    public OnDataChangedListener getOnDataChangedListener() {
        return onDataChangedListener;
    }

    public void setOnDataChangedListener(OnDataChangedListener onDataChangedListener) {
        this.onDataChangedListener = onDataChangedListener;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        this.mStatus = status;
        this.notifyDataSetChanged();
    }

    /**
     * 设置加载更多视图（Bottom）
     *
     * @param promptViewLoadmore
     */
    public void setPromptViewLoadmore(View promptViewLoadmore) {
        this.promptViewLoadmore = promptViewLoadmore;
    }

    /**
     * 设置加载失败视图（Bottom）
     *
     * @param promptViewLoadFail
     */
    public void setPromptViewLoadFail(View promptViewLoadFail) {
        this.promptViewLoadFail = promptViewLoadFail;
    }

    /**
     * 设置加载中视图（Bottom）
     *
     * @param promptViewLoading
     */
    public void setPromptViewLoading(View promptViewLoading) {
        this.promptViewLoading = promptViewLoading;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    private void initDataList() {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
    }

    public void setDataList(List<E> dataList) {
        this.mDataList = dataList;
        initDataList();
        if (onDataChangedListener != null)
            onDataChangedListener.onDataChanged(mDataList);
        this.notifyDataSetChanged();
    }

    public void apendData(E... datas) {
        initDataList();
        for (E o : datas)
            this.mDataList.add(o);
        if (onDataChangedListener != null)
            onDataChangedListener.onDataChanged(mDataList);
        this.notifyItemRangeChanged(this.mDataList.size() - 1 - datas.length, datas.length);
    }

    public E getItem(int position) {
        if (mDataList == null || position >= mDataList.size()) return null;
        return mDataList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (mStatus != STATUS_DEFAULT && (position == getItemCount() - 1 || getItemCount() == 0)) {
            if (mStatus == STATUS_HASMORE)
                return VIEWTYPE_BOTTOM_LOADMORE;
            else if (mStatus == STATUS_LOADING)
                return VIEWTYPE_BOTTOM_LOADING;
            else if (mStatus == STATUS_LOADFAIL)
                return VIEWTYPE_BOTTOM_LOADFAIL;
            else if (mStatus == STATUS_NODATA)
                return VIEWTYPE_NODATA;
        }
        return super.getItemViewType(position);
    }

    @Override
    final public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        if (viewType == VIEWTYPE_BOTTOM_LOADFAIL)
            return new BaseRecyclerViewViewHolder(promptViewLoadFail);
        if (viewType == VIEWTYPE_BOTTOM_LOADING)
            return new BaseRecyclerViewViewHolder(promptViewLoading);
        if (viewType == VIEWTYPE_BOTTOM_LOADMORE)
            return new BaseRecyclerViewViewHolder(promptViewLoadmore);

        final RecyclerView.ViewHolder vh = createHolder(parent, viewType);
        if (vh != null && vh.itemView != null) {
            //点击事件
            if (onItemClickListener != null) {
                vh.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(vh, viewType);
                        }
                    }
                });
            }//长按事件
            if (onItemLongClickListener != null) {
                vh.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (onItemClickListener != null) {
                            return onItemLongClickListener.onItemLongClick(vh, viewType);
                        }
                        return false;
                    }

                });
            }
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null && holder instanceof BaseRecyclerViewViewHolder) {
            ((BaseRecyclerViewViewHolder) holder).bindData(getItem(position),position);
            if (viewHolderMap.containsKey(position))
                viewHolderMap.remove(position);
            viewHolderMap.put(position, holder);
        }
    }

    public abstract RecyclerView.ViewHolder createHolder(ViewGroup parent, int viewType);


    @Override
    public int getItemCount() {
        return mDataList.size() + (mStatus == STATUS_DEFAULT ? 0 : 1);
    }

    public LayoutInflater getLayoutInflater() {
        if (mInflater == null)
            this.mInflater = LayoutInflater.from(mContext);
        return mInflater;
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder viewHolder, int viewType);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(RecyclerView.ViewHolder viewHolder, int viewType);
    }

    public interface OnDataChangedListener<DT> {
        void onDataChanged(List<DT> data);
    }
}
