package com.cpigeon.app.modular.matchlive.model.dao;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.matchlive.model.bean.Bulletin;

import java.util.List;

/**
 * Created by Administrator on 2017/4/18.
 */

public interface IRaceReport extends IBaseDao{
    void updateBulletin(String lx, String ssid, IBaseDao.OnCompleteListener<List<Bulletin>> listOnCompleteListener);
    void queryBulletin(String ssid, IBaseDao.OnCompleteListener<Bulletin> onCompleteListener);
    void addRaceClickCount(String lx,String ssid);
}
