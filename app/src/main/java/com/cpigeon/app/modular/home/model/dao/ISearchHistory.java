package com.cpigeon.app.modular.home.model.dao;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.home.model.bean.SearchHistory;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/14.
 */

public interface ISearchHistory extends IBaseDao{
    /**
     *加载搜索历史
     * @param key
     * @param listener
     */
    void loadSearchHistory(String key,IBaseDao.OnCompleteListener<List<SearchHistory>> listener);


    /**
     * 执行搜索
     * @param key
     * @param listener
     */
    void doSearch(String key,IBaseDao.OnCompleteListener<List<Map<String, Object>>> listener);
    void deleteHistory();
}
