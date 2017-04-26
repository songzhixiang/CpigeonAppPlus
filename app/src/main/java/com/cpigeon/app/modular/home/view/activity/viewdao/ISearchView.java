package com.cpigeon.app.modular.home.view.activity.viewdao;

import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.home.model.bean.SearchHistory;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/14.
 */

public interface ISearchView extends IView{
    void showLoadSearchResult(List<Map<String, Object>> data);
    void showLoadSearchHistory(List<SearchHistory> data);
    String getSearch();
}
