package com.cpigeon.app.commonstandard.view.activity;

import java.util.List;

/**
 * Created by chenshuai on 2017/4/15.
 * 视图支持分页的标准接口
 */

public interface IPageTurn<DataBean> {
    /**
     * 获取页码
     *
     * @return
     */
    int getPageIndex();

    /**
     * 获取每页的个数
     *
     * @return
     */
    int getPageSize();

    /**
     * 初始化分页数据，可用作刷新时的初始化
     */
    void iniPageAndAdapter();

    /**
     * 显示分页数据
     */
    void showMoreData(List<DataBean> dataBeanList);

    /**
     * 是否可以加载更多数据
     *
     * @return
     */
    boolean canLoadMoreData();

    /**
     * 是否正在加载更多数据
     *
     * @return
     */
    boolean isMoreDataLoading();

    /**
     * 是否正在刷新数据
     *
     * @return
     */
    boolean isRefreshing();

    /**
     * 加载更多数据完成
     */
    void loadMoreComplete();

    /**
     * 加载更多数据失败
     */
    void loadMoreFail();
}
