package com.cpigeon.app.modular.home.view.fragment.viewdao;

import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.home.model.bean.HomeAd;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/4/6.
 */

public interface IHomeView extends IView{
    void showAd(List<HomeAd> homeAdList);
    void showMatchLiveData(List<MatchInfo> list,int type);
}
