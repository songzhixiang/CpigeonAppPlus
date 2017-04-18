package com.cpigeon.app.modular.matchlive.model.dao;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/4/18.
 */

public interface IChaZuDao extends IBaseDao{
    void loadChaZuTongJi(String lx, String ssid, IBaseDao.OnCompleteListener<List<HashMap<String, Object>>> onCompleteListener);
}
