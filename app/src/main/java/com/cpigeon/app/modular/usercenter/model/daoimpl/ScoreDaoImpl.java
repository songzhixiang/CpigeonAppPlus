package com.cpigeon.app.modular.usercenter.model.daoimpl;

import android.support.annotation.NonNull;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.usercenter.model.bean.UserScore;
import com.cpigeon.app.modular.usercenter.model.dao.IScoreDao;
import com.cpigeon.app.utils.CallAPI;

import java.util.List;

/**
 * Created by chenshuai on 2017/4/13.
 */

public class ScoreDaoImpl implements IScoreDao {

    @Override
    public void loadScoreRecord(int pageindex, int pagesize, @NonNull final OnCompleteListener<List<UserScore>> onCompleteListener) {
        CallAPI.getUserScoreRecord(MyApp.getInstance(), pagesize, pageindex, "", new CallAPI.Callback<List<UserScore>>() {
            @Override
            public void onSuccess(List<UserScore> data) {
                onCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                onCompleteListener.onFail("加载失败");
            }
        });
    }
}
