package com.cpigeon.app.modular.matchlive.model.dao;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/4/7.
 */

public interface IMatchInfo extends IBaseDao{
    void loadXHDatas(OnCompleteListener<List<MatchInfo>> listener);
    void loadGPDatas(OnCompleteListener<List<MatchInfo>> listener);

}
