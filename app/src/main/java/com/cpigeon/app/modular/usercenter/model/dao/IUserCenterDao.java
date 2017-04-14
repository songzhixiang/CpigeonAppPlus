package com.cpigeon.app.modular.usercenter.model.dao;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;

import java.util.Map;

/**
 * Created by Administrator on 2017/4/10.
 */

public interface IUserCenterDao extends IBaseDao{
    void loadUserBalance(IBaseDao.OnCompleteListener<Map<String,Object>> listener);

    void getUserSignStatus(IBaseDao.OnCompleteListener<Boolean> listener);
}
