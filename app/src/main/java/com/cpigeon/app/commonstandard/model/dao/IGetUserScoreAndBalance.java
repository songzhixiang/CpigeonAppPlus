package com.cpigeon.app.commonstandard.model.dao;

/**
 * Created by chenshuai on 2017/4/14.
 */

public interface IGetUserScoreAndBalance {
    void getUserScoreAndBlance(int userid, IBaseDao.OnCompleteListener<Boolean> onCompleteListener);
}
