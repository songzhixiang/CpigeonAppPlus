package com.cpigeon.app.commonstandard.model.dao;

import java.util.Map;

/**
 * Created by chenshuai on 2017/4/14.
 * 获取用户鸽币余额信息yue,jifen
 */

public interface IGetUserScoreAndBalance {
    void getUserScoreAndBalance(int userid, IBaseDao.OnCompleteListener<Map<String, Object>> onCompleteListener);
}
