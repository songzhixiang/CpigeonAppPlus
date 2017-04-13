package com.cpigeon.app.modular.usercenter.model.dao;

import android.support.annotation.NonNull;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.usercenter.model.bean.UserScore;

import java.util.List;

/**
 * Created by chenshuai on 2017/4/13.
 */

public interface IScoreDao extends IBaseDao {

    void loadScoreRecord(int pageindex, int pagesize, @NonNull OnCompleteListener<List<UserScore>> onCompleteListener);
}
