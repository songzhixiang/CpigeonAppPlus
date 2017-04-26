package com.cpigeon.app.modular.home.model.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2016/12/9.
 */
@Table(name = "search_history")
public class SearchHistory {
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "searchKey", property = "UNIQUE")
    private String searchKey;
    @Column(name = "searchTime")
    private long searchTime;
    @Column(name = "searchCount", property = "DEFAULT 1")
    private long searchCount;
    @Column(name = "searchUserId")
    private int searchUserId;

    public SearchHistory() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public long getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(long searchTime) {
        this.searchTime = searchTime;
    }

    public long getSearchCount() { return searchCount; }

    public void setSearchCount(long searchCount) { this.searchCount = searchCount; }

    public int getSearchUserId() {
        return searchUserId;
    }

    public void setSearchUserId(int searchUserId) {
        this.searchUserId = searchUserId;
    }

    @Override
    public String toString() {
        return this.getSearchKey();
        // return super.toString();
    }
}
