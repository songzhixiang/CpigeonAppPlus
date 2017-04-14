package com.cpigeon.app.modular.matchlive.model.daoimpl;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.model.bean.SearchHistory;
import com.cpigeon.app.modular.matchlive.model.dao.ISearchHistory;
import com.cpigeon.app.modular.matchlive.view.activity.SearchResultAdapter;
import com.cpigeon.app.utils.CommonTool;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.DateTool;
import com.orhanobut.logger.Logger;

import org.xutils.DbManager;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/14.
 */

public class SearchHistoryImpl implements ISearchHistory {
    private DbManager mDB;
    private List<MatchInfo> data_MatchInfo = null;
    @Override
    public void loadSearchHistory(final String key, final IBaseDao.OnCompleteListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Selector selector = mDB.selector(SearchHistory.class);
                    selector.where("searchKey", "!=", "")
                            .and(WhereBuilder.b().or("searchUserId", "=", CpigeonData.getInstance()
                                    .getUserId(MyApp.getInstance()))
                                    .or("searchUserId", "=", 0))
                            .orderBy("searchCount", true)
                            .orderBy("searchTime", true);
                    if (!"".equals(key))
                        selector.and("searchKey", "like", "%" + key + "%");
                    listener.onSuccess(selector.findAll());
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void showSearchResult(IBaseDao.OnCompleteListener listener) {

    }

    @Override
    public void doSearch(final String key, IBaseDao.OnCompleteListener listener) {
        CommonTool.hideIME(MyApp.getInstance());
        //保存搜索记录
        new Thread() {
            @Override
            public void run() {
                if ("".equals(key)) return;
                SearchHistory sh;
                try {
                    sh = mDB.selector(SearchHistory.class).where("searchKey", "=", key).findFirst();
                    if (sh == null) {
                        sh = new SearchHistory();
                        sh.setSearchKey(key);
                        sh.setSearchUserId(CpigeonData.getInstance().getUserId(MyApp.getInstance()));
                        sh.setSearchCount(0);
                    }
                    sh.setSearchCount(sh.getSearchCount() + 1);//增加搜索次数
                    sh.setSearchTime(System.currentTimeMillis());//更新搜索时间
                    mDB.saveOrUpdate(sh);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }

    @Override
    public void searchLocal(final String key, IBaseDao.OnCompleteListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!"".equals(key)) {
                        data_MatchInfo = mDB.selector(MatchInfo.class).where("st", ">", DateTool.dateTimeToStr(new Date(new Date().getTime() - 1000 * 60 * 60 * 24 * 3)))
                                .and(WhereBuilder.b().or("mc", "like", "%" + key + "%").or("bsmc", "like", "%" + key + "%"))
                                .orderBy("st", true)
                                .findAll();
                    } else {
                        if (data_MatchInfo != null)
                            data_MatchInfo.clear();
                    }
                    Logger.i("搜索结果条数=" + (data_MatchInfo == null ? "null" : String.valueOf(data_MatchInfo.size())));
                    final List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

                    if (data_MatchInfo != null && data_MatchInfo.size() > 0) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("viewtype", SearchResultAdapter.VIEWTYPE_GROUP_TITLE);
                        map.put("name", "比赛信息");
                        map.put("prompt", String.format("%d条", data_MatchInfo == null ? 0 : data_MatchInfo.size()));
                        data.add(map);
                        for (MatchInfo matchInfo : data_MatchInfo) {
                            map = new HashMap<String, Object>();
                            map.put("viewtype", SearchResultAdapter.VIEWTYPE_MATCHINFO);
                            map.put("data", matchInfo);
                            data.add(map);
                        }
                    }
//                    searchResultTempData.put(sKey, data);
//                    loadSearchResult(data);

                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
