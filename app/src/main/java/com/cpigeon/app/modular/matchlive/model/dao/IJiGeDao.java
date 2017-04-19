package com.cpigeon.app.modular.matchlive.model.dao;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */

public interface IJiGeDao extends IBaseDao {

    void laodJiGeData(String matchType,
                      String ssid,
                      String foot,
                      String name,
                      boolean hascz,
                      int pager,
                      int pagesize,
                      int czIndex,
                      String sKey, OnCompleteListener<List> listOnCompleteListener);
}
