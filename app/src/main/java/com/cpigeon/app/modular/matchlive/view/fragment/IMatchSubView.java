package com.cpigeon.app.modular.matchlive.view.fragment;

import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/4/7.
 */

public interface IMatchSubView  extends IView{
    void showGPData(List<MatchInfo> matchInfoList, int type);
    void showXHData(List<MatchInfo> matchInfoList,int type);
    void setLoadType(int type);

}
