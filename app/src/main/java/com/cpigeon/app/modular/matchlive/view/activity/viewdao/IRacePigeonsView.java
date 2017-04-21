package com.cpigeon.app.modular.matchlive.view.activity.viewdao;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.cpigeon.app.commonstandard.view.activity.IPageTurn;
import com.cpigeon.app.commonstandard.view.activity.IRefresh;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.order.model.bean.CpigeonOrderInfo;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */

public interface IRacePigeonsView extends IView,IRefresh,IPageTurn<MultiItemEntity> {
    MatchInfo getMatchInfo();
    String getSsid();
    String getMatchType();
    String getFoot();
    String getName();
    boolean isHascz();
    int getCzIndex();
    String getSkey();

    void showMenuGroup();

    List<HashMap<String, Object>> getData_CZTJ();
}
