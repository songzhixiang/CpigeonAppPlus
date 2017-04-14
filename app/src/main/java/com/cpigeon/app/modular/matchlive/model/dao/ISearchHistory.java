package com.cpigeon.app.modular.matchlive.model.dao;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;

/**
 * Created by Administrator on 2017/4/14.
 */

public interface ISearchHistory {
    /**
     *加载搜索历史
     * @param key
     * @param listener
     */
    void loadSearchHistory(String key,IBaseDao.OnCompleteListener listener);

    /**
     * 显示搜索的结果
     * @param listener
     */
    void showSearchResult(IBaseDao.OnCompleteListener listener);

    /**
     * 执行搜索
     * @param key
     * @param listener
     */
    void doSearch(String key,IBaseDao.OnCompleteListener listener);

    void searchLocal(String key,IBaseDao.OnCompleteListener listener);
}
