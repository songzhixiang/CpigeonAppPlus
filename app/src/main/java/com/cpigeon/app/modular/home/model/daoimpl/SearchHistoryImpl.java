package com.cpigeon.app.modular.home.model.daoimpl;

import android.text.TextUtils;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.home.model.bean.SearchHistory;
import com.cpigeon.app.modular.home.model.dao.ISearchHistory;
import com.cpigeon.app.modular.matchlive.view.adapter.SearchResultAdapter;
import com.cpigeon.app.utils.CpigeonConfig;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.DateTool;
import com.orhanobut.logger.Logger;

import org.xutils.DbManager;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/14.
 */

public class SearchHistoryImpl implements ISearchHistory {
    private DbManager mDB =x.getDb(CpigeonConfig.getDataDb());
    private List<MatchInfo> data_MatchInfo = null;
    private Map<String, List<Map<String, Object>>> searchResultTempData = new HashMap<>();//用户零时存储搜索的结果

    /**
     * 加载搜索历史
     * @param key
     * @param listener
     */
    @Override
    public void loadSearchHistory(final String key, final IBaseDao.OnCompleteListener<List<SearchHistory>> listener) {
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
                    if (!TextUtils.isEmpty(key))
                        selector.and("searchKey", "like", "%" + key + "%");
                    final List<SearchHistory> data = selector.findAll();
                    listener.onSuccess(data);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void doSearch(final String key, final IBaseDao.OnCompleteListener<List<Map<String, Object>>> listener) {

        //保存搜索记录
        new Thread() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(key)) return;
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
        if (searchResultTempData.containsKey(key)) {
            Logger.i("加载内存中数据... skey=" + key);
            listener.onSuccess(searchResultTempData.get(key));
            return;
        }
        new Thread(new Runnable() {
            public void run() {
                Logger.i("赛事搜索... skey=" + key);
                try {
                    if (!TextUtils.isEmpty(key)) {
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
                    searchResultTempData.put(key, data);
                    listener.onSuccess(data);

                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void deleteHistory() {
        try {
            mDB.delete(SearchHistory.class, WhereBuilder.b("searchUserId", "=", CpigeonData.getInstance().getUserId(MyApp.getInstance())));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


}
