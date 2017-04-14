package com.cpigeon.app.modular.home.model.dao;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.home.model.bean.HomeAd;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/4/12.
 */

public interface IHomeFragmentDao extends IBaseDao{
    void loadHomeAd(OnCompleteListener<List<HomeAd>> listener);

    void loadMatchInfo(int loadType,OnCompleteListener<List<MatchInfo>> listenr);


}
