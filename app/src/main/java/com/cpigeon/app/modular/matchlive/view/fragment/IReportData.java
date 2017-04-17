package com.cpigeon.app.modular.matchlive.view.fragment;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.cpigeon.app.commonstandard.view.activity.IPageTurn;
import com.cpigeon.app.commonstandard.view.activity.IRefresh;
import com.cpigeon.app.commonstandard.view.activity.IView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/17.
 */

public interface IReportData extends IView, IPageTurn<MultiItemEntity>, IRefresh {
    String getMatchType();

    String getSsid();

    String getFoot();

    String getName();

    boolean hascz();

    int czIndex();

    String sKey();
}
