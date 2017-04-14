package com.cpigeon.app.modular.matchlive.view.activity;

import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.matchlive.model.bean.SearchHistory;

import java.util.List;

/**
 * Created by Administrator on 2017/4/14.
 */

public interface ISearchView {
    void showLoadSearchResult(List data);
    void showLoadSearchHistory(List<SearchHistory> data);
    void saveSearchHistory();
    String getSearch();
}
