package com.cpigeon.app.modular.matchlive.model.daoimpl;

import com.cpigeon.app.MyApp;

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
    private OnLoadCompleteListener listener;

    private CallAPI.Callback<List<MatchInfo>> callback = new  CallAPI.Callback<List<MatchInfo>>() {
        @Override
        public void onSuccess(List<MatchInfo> data) {
            listener.loadSuccess(data);
        }

        @Override
        public void onError(int errorType, Object data) {
            listener.loadFailed("加载失败");
        }
    };
    @Override
    public void loadXHDatas(final OnLoadCompleteListener onLoadCompleteListener) {
        this.listener = onLoadCompleteListener;
        CallAPI.getMatchInfo(MyApp.getInstance(), CallAPI.DATATYPE.MATCH.XH, CpigeonConfig.LIVE_DAYS_XH,callback );
    }

    @Override
    public void loadGPDatas(OnLoadCompleteListener onLoadCompleteListener) {
        this.listener = onLoadCompleteListener;
        CallAPI.getMatchInfo(MyApp.getInstance(),CallAPI.DATATYPE.MATCH.GP,CpigeonConfig.LIVE_DAYS_GP,callback);
    }
}
