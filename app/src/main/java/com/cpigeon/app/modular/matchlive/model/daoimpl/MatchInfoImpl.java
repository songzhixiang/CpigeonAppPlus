package com.cpigeon.app.modular.matchlive.model.daoimpl;

import com.cpigeon.app.MyApp;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.model.dao.IMatchInfo;
import com.cpigeon.app.utils.CallAPI;
import com.cpigeon.app.utils.CpigeonConfig;

import java.util.List;

/**
 *
 * Created by Administrator on 2017/4/7.
 */

public class MatchInfoImpl implements IMatchInfo {


    @Override
    public void loadXHDatas(final OnCompleteListener<List<MatchInfo>> listener) {
        CallAPI.getMatchInfo(MyApp.getInstance(), CallAPI.DATATYPE.MATCH.XH, CpigeonConfig.LIVE_DAYS_XH, new CallAPI.Callback<List<MatchInfo>>() {
            @Override
            public void onSuccess(List<MatchInfo> data) {
                listener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                listener.onFail(""+errorType);
            }
        });
    }

    @Override
    public void loadGPDatas(final OnCompleteListener<List<MatchInfo>> listener) {
        CallAPI.getMatchInfo(MyApp.getInstance(), CallAPI.DATATYPE.MATCH.GP, CpigeonConfig.LIVE_DAYS_GP, new CallAPI.Callback<List<MatchInfo>>() {
            @Override
            public void onSuccess(List<MatchInfo> data) {
                listener.onSuccess(data);
            }

            @Override
            public void onError(int errorType, Object data) {
                listener.onFail(""+errorType);
            }
        });
    }


}
