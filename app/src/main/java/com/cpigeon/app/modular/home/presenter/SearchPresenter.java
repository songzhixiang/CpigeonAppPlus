package com.cpigeon.app.modular.home.presenter;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.modular.home.model.bean.SearchHistory;
import com.cpigeon.app.modular.home.model.dao.ISearchHistory;
import com.cpigeon.app.modular.home.model.daoimpl.SearchHistoryImpl;
import com.cpigeon.app.modular.home.view.activity.viewdao.ISearchView;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by Administrator on 2017/4/14.
 */

public class SearchPresenter extends BasePresenter<ISearchView,ISearchHistory> {

    public SearchPresenter(ISearchView mView) {
        super(mView);
    }
    @Override
    protected ISearchHistory initDao() {
        return new SearchHistoryImpl();
    }
    public void doSearch()
    {
        mDao.doSearch(mView.getSearch(), new IBaseDao.OnCompleteListener<List<Map<String, Object>>>() {

            @Override
            public void onSuccess(final List<Map<String, Object>> data) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        mView.showLoadSearchResult(data);
                    }
                });
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }
    public void showHistory()
    {
        mDao.loadSearchHistory(mView.getSearch(), new IBaseDao.OnCompleteListener<List<SearchHistory>>() {
            @Override
            public void onSuccess(final List<SearchHistory> data) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        mView.showLoadSearchHistory(data);
                    }
                });

            }

            @Override
            public void onFail(String msg) {

            }
        });
    }
    public void deleteHistory()
    {
        mDao.deleteHistory();
    }
}
