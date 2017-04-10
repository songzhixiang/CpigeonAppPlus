package com.cpigeon.app.modular.matchlive.model.dao;

import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/4/7.
 */

public interface IMatchInfo {
    void loadXHDatas(OnLoadCompleteListener onLoadCompleteListener);
    void loadGPDatas(OnLoadCompleteListener onLoadCompleteListener);
    interface OnLoadCompleteListener{
        void loadSuccess(List<MatchInfo> matchInfoList);
        void loadFailed(String msg);
    }
}
