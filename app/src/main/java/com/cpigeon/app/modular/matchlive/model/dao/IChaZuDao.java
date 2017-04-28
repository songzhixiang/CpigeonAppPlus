package com.cpigeon.app.modular.matchlive.model.dao;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/4/18.
 */

public interface IChaZuDao extends IBaseDao{
    /**
     * 加载插组统计
     * @param lx
     * @param ssid
     * @param onCompleteListener
     */
    void loadChaZuTongJi(String lx, String ssid, IBaseDao.OnCompleteListener<List<HashMap<String, Object>>> onCompleteListener);

    /**
     * 加载插组报到的详细信息
     * @param matchType
     * @param ssid
     * @param foot
     * @param name
     * @param hascz
     * @param pager
     * @param pagesize
     * @param czIndex
     * @param sKey
     * @param onCompleteListener
     */
    void loadChaZuBaoDaoDetails(String matchType,
                                String ssid,
                                String foot,
                                String name,
                                boolean hascz,
                                int pager,
                                int pagesize,
                                int czIndex,
                                String sKey,
                                OnCompleteListener<List> onCompleteListener);


    void loadChaZhiDingDaoDetails(String matchType,
                                String ssid,
                                String foot,
                                String name,
                                boolean hascz,
                                int pager,
                                int pagesize,
                                int czIndex,
                                String sKey,
                                OnCompleteListener<List> onCompleteListener);

}
