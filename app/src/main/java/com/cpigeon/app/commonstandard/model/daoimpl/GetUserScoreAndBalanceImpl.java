package com.cpigeon.app.commonstandard.model.daoimpl;

import com.cpigeon.app.MyApp;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.model.dao.IGetUserScoreAndBalance;
import com.cpigeon.app.utils.CallAPI;

import java.util.Map;

/**
 * Created by chenshuai on 2017/4/14.
 */

public abstract class GetUserScoreAndBalanceImpl implements IGetUserScoreAndBalance {
    @Override
    public void getUserScoreAndBalance(int userid, final IBaseDao.OnCompleteListener<Map<String, Object>> onCompleteListener) {
        CallAPI.getUserYuEAndJiFen(MyApp.getInstance(), new CallAPI.Callback<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> data) {
                onCompleteListener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                onCompleteListener.onFail("");
            }
        });
    }
}
